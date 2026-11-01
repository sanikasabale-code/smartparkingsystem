/**
 * ===================================================================
 * File: admin.js
 * Description: Client-side JavaScript for Admin panel operations using Fetch API.
 * Syllabus Topics Demonstrated:
 *   - Module IV.1: Executing RESTful GET, POST, PUT, DELETE requests
 * ===================================================================
 */

const API_ADMIN = '/api/admin';

// DOM Elements
const adminAlertBox = document.getElementById('adminAlertBox');
const statTotalBookings = document.getElementById('statTotalBookings');
const statTotalRevenue = document.getElementById('statTotalRevenue');
const statOccupiedSlots = document.getElementById('statOccupiedSlots');
const statAvailableSlots = document.getElementById('statAvailableSlots');
const currentCarRate = document.getElementById('currentCarRate');
const currentBikeRate = document.getElementById('currentBikeRate');
const priceForm = document.getElementById('priceForm');
const slotForm = document.getElementById('slotForm');
const editSlotId = document.getElementById('editSlotId');
const slotNumber = document.getElementById('slotNumber');
const slotVehicleType = document.getElementById('slotVehicleType');
const slotStatus = document.getElementById('slotStatus');
const slotSubmitBtn = document.getElementById('slotSubmitBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const slotsTableBody = document.getElementById('slotsTableBody');
const bookingsTableBody = document.getElementById('bookingsTableBody');

function showAdminAlert(message, isSuccess = true) {
    adminAlertBox.textContent = message;
    adminAlertBox.className = 'alert ' + (isSuccess ? 'success' : 'error');
    adminAlertBox.style.display = 'block';
    setTimeout(() => {
        adminAlertBox.style.display = 'none';
    }, 4000);
}

/**
 * Feature 4: View simple statistics from /api/admin/statistics
 */
function loadStatistics() {
    fetch(`${API_ADMIN}/statistics`)
        .then(res => res.json())
        .then(data => {
            statTotalBookings.textContent = data.totalBookings;
            statTotalRevenue.textContent = `Rs. ${Number(data.totalRevenue).toFixed(2)}`;
            statOccupiedSlots.textContent = data.currentlyOccupied;
            statAvailableSlots.textContent = data.availableSlots;
        })
        .catch(err => console.error('Error loading stats:', err));
}

/**
 * Feature 2: Load current price settings
 */
function loadPrices() {
    fetch(`${API_ADMIN}/prices`)
        .then(res => res.json())
        .then(prices => {
            currentCarRate.textContent = prices.CAR ? `Rs. ${prices.CAR}/hr` : 'Not Set';
            currentBikeRate.textContent = prices.BIKE ? `Rs. ${prices.BIKE}/hr` : 'Not Set';
        })
        .catch(err => console.error('Error loading prices:', err));
}

/**
 * Feature 2: Update hourly price setting
 */
priceForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const type = document.getElementById('priceVehicleType').value;
    const rate = document.getElementById('pricePerHour').value;

    fetch(`${API_ADMIN}/prices?vehicleType=${encodeURIComponent(type)}&pricePerHour=${encodeURIComponent(rate)}`, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        showAdminAlert(`Updated ${data.vehicleType} rate to Rs. ${data.pricePerHour}/hr`, true);
        priceForm.reset();
        loadPrices();
    })
    .catch(err => showAdminAlert('Failed to update price: ' + err.message, false));
});

/**
 * Feature 1: Load all slots in the Admin table
 */
function loadAdminSlots() {
    fetch(`${API_ADMIN}/slots`)
        .then(res => res.json())
        .then(slots => {
            slotsTableBody.innerHTML = '';
            slots.forEach(s => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${s.id}</td>
                    <td><strong>${s.slotNumber}</strong></td>
                    <td>${s.vehicleType}</td>
                    <td><span class="slot-badge" style="background:${s.status==='AVAILABLE'?'#27ae60':'#e74c3c'}">${s.status}</span></td>
                    <td>
                        <button onclick="startEditSlot(${s.id}, '${s.slotNumber}', '${s.vehicleType}', '${s.status}')" style="padding:4px 8px; font-size:12px;">Edit</button>
                        <button onclick="deleteSlot(${s.id})" class="danger" style="padding:4px 8px; font-size:12px; margin-left:4px;">Delete</button>
                    </td>
                `;
                slotsTableBody.appendChild(tr);
            });
        })
        .catch(err => console.error('Error loading admin slots:', err));
}

/**
 * Feature 1: Add or Update slot via form submit
 */
slotForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const id = editSlotId.value;
    const payload = {
        slotNumber: slotNumber.value.trim(),
        vehicleType: slotVehicleType.value,
        status: slotStatus.value
    };

    if (id) {
        // Concept: HTTP PUT request for updating
        fetch(`${API_ADMIN}/slots/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(() => {
            showAdminAlert(`Slot ${payload.slotNumber} updated successfully!`, true);
            resetSlotForm();
            loadAdminSlots();
            loadStatistics();
        })
        .catch(err => showAdminAlert('Update failed: ' + err.message, false));
    } else {
        // Concept: HTTP POST request for creating
        fetch(`${API_ADMIN}/slots`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(created => {
            showAdminAlert(`Slot ${created.slotNumber} created!`, true);
            resetSlotForm();
            loadAdminSlots();
            loadStatistics();
        })
        .catch(err => showAdminAlert('Creation failed: ' + err.message, false));
    }
});

function startEditSlot(id, number, type, status) {
    editSlotId.value = id;
    slotNumber.value = number;
    slotVehicleType.value = type;
    slotStatus.value = status;
    slotSubmitBtn.textContent = 'Save Changes';
    cancelEditBtn.style.display = 'inline-block';
}

function resetSlotForm() {
    editSlotId.value = '';
    slotForm.reset();
    slotSubmitBtn.textContent = 'Add Slot';
    cancelEditBtn.style.display = 'none';
}

cancelEditBtn.addEventListener('click', resetSlotForm);

/**
 * Feature 1: Delete a slot
 */
function deleteSlot(id) {
    if (!confirm('Are you sure you want to delete this parking slot?')) return;

    // Concept: HTTP DELETE request
    fetch(`${API_ADMIN}/slots/${id}`, {
        method: 'DELETE'
    })
    .then(res => res.json())
    .then(data => {
        showAdminAlert(data.message || 'Slot deleted', true);
        loadAdminSlots();
        loadStatistics();
    })
    .catch(err => showAdminAlert('Delete failed: ' + err.message, false));
}

/**
 * Feature 3: View all booking records
 */
function loadAllBookings() {
    fetch(`${API_ADMIN}/bookings`)
        .then(res => res.json())
        .then(bookings => {
            bookingsTableBody.innerHTML = '';
            bookings.forEach(b => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${b.id}</td>
                    <td>${b.customerName}</td>
                    <td>${b.vehicleNumber}</td>
                    <td>${b.vehicleType}</td>
                    <td>${b.slotNumber}</td>
                    <td>${b.entryTime ? b.entryTime.replace('T', ' ').substring(0, 19) : '-'}</td>
                    <td>${b.exitTime ? b.exitTime.replace('T', ' ').substring(0, 19) : 'Active'}</td>
                    <td>Rs. ${b.fare.toFixed(2)}</td>
                    <td><strong>${b.status}</strong></td>
                `;
                bookingsTableBody.appendChild(tr);
            });
        })
        .catch(err => console.error('Error loading bookings:', err));
}

// Initial loads
loadStatistics();
loadPrices();
loadAdminSlots();
loadAllBookings();
