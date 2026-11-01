/**
 * ===================================================================
 * File: customer.js
 * Description: Client-side JavaScript handling Customer operations using Fetch API.
 * Syllabus Topics Demonstrated:
 *   - Module IV.1: Calling Spring Boot REST APIs asynchronously
 * ===================================================================
 */

// Base API URL
const API_BASE = '/api/customer';

// DOM Elements
const availableCountElem = document.getElementById('availableCount');
const slotsGrid = document.getElementById('slotsGrid');
const slotSelect = document.getElementById('slotSelect');
const vehicleTypeSelect = document.getElementById('vehicleType');
const reserveForm = document.getElementById('reserveForm');
const checkoutForm = document.getElementById('checkoutForm');
const checkoutResult = document.getElementById('checkoutResult');
const historyForm = document.getElementById('historyForm');
const historyTable = document.getElementById('historyTable');
const historyTableBody = document.getElementById('historyTableBody');
const alertBox = document.getElementById('alertBox');

// In-memory cache for all fetched parking slots
let cachedSlots = [];

/**
 * Display helper for success/error alerts.
 */
function showAlert(message, isSuccess = true) {
    alertBox.textContent = message;
    alertBox.className = 'alert ' + (isSuccess ? 'success' : 'error');
    alertBox.style.display = 'block';
    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 4500);
}

/**
 * Populate slot dropdown based on currently selected vehicle type.
 * Preserves the active selection so auto-refresh does not clear the user's choice.
 */
function populateSlotDropdown() {
    const selectedType = vehicleTypeSelect ? vehicleTypeSelect.value.toUpperCase() : 'CAR';
    const currentSelection = slotSelect.value;

    slotSelect.innerHTML = `<option value="">-- Select Available ${selectedType} Slot --</option>`;

    // Filter available slots that match the selected vehicle type
    const matchingSlots = cachedSlots.filter(slot =>
        slot.status === 'AVAILABLE' &&
        (!slot.vehicleType || slot.vehicleType.toUpperCase() === selectedType)
    );

    matchingSlots.forEach(slot => {
        const opt = document.createElement('option');
        opt.value = slot.slotNumber;
        opt.textContent = `${slot.slotNumber} (${slot.vehicleType})`;
        slotSelect.appendChild(opt);
    });

    // Preserve the user's selected slot if it is still available for this vehicle type
    if (currentSelection && matchingSlots.some(s => s.slotNumber === currentSelection)) {
        slotSelect.value = currentSelection;
    }
}

// Re-filter available slots dynamically when the customer changes vehicle type
if (vehicleTypeSelect) {
    vehicleTypeSelect.addEventListener('change', populateSlotDropdown);
}

/**
 * Feature 1 & 2: Load all parking slots and update the slots grid & reservation dropdown.
 */
function loadSlots() {
    fetch(`${API_BASE}/slots`)
        .then(res => res.json())
        .then(slots => {
            cachedSlots = slots;

            // Render visual slots grid
            slotsGrid.innerHTML = '';
            slots.forEach(slot => {
                const item = document.createElement('div');
                item.className = `slot-item ${slot.status}`;
                item.innerHTML = `
                    <div class="slot-name">${slot.slotNumber}</div>
                    <div class="slot-type">Type: ${slot.vehicleType}</div>
                    <span class="slot-badge">${slot.status}</span>
                `;
                slotsGrid.appendChild(item);
            });

            // Populate dropdown with matching available slots while keeping user's selection
            populateSlotDropdown();
        })
        .catch(err => console.error('Error loading slots:', err));
}

/**
 * Feature 2: Auto-refresh count of available parking slots every 5 seconds.
 */
function loadAvailableCount() {
    fetch(`${API_BASE}/slots/available-count`)
        .then(res => res.json())
        .then(data => {
            availableCountElem.textContent = data.availableCount;
        })
        .catch(err => console.error('Error loading count:', err));
}

/**
 * Feature 3: Reserve a slot on form submission.
 */
reserveForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const payload = {
        customerName: document.getElementById('customerName').value,
        vehicleNumber: document.getElementById('vehicleNumber').value,
        vehicleType: document.getElementById('vehicleType').value,
        slotNumber: slotSelect.value
    };

    fetch(`${API_BASE}/reserve`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(async res => {
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.error || 'Failed to reserve slot');
        }
        return res.json();
    })
    .then(booking => {
        showAlert(`Slot ${booking.slotNumber} successfully reserved for ${booking.customerName}!`, true);
        reserveForm.reset();
        loadSlots();
        loadAvailableCount();
    })
    .catch(err => {
        showAlert(err.message, false);
    });
});

/**
 * Feature 4: Checkout vehicle, compute fare, and generate receipt.
 */
checkoutForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const slotNumber = document.getElementById('checkoutSlotNumber').value.trim();

    fetch(`${API_BASE}/checkout?slotNumber=${encodeURIComponent(slotNumber)}`, {
        method: 'POST'
    })
    .then(async res => {
        if (!res.ok) {
            const errData = await res.json().catch(() => ({}));
            throw new Error(errData.error || 'Checkout failed');
        }
        return res.json();
    })
    .then(booking => {
        // Summary text
        checkoutResult.innerHTML = `
            Check-out complete! Vehicle: <strong>${booking.vehicleNumber}</strong> | 
            Slot: <strong>${booking.slotNumber}</strong> | 
            Total Fare: <strong>Rs. ${booking.fare.toFixed(2)}</strong>
        `;

        // Populate and display the Printable Receipt Card
        document.getElementById('receiptDate').textContent = new Date().toLocaleString();
        document.getElementById('receiptId').textContent = 'REC-' + booking.id;
        document.getElementById('receiptCustomer').textContent = booking.customerName;
        document.getElementById('receiptVehicle').textContent = booking.vehicleNumber;
        document.getElementById('receiptType').textContent = booking.vehicleType;
        document.getElementById('receiptSlot').textContent = booking.slotNumber;
        document.getElementById('receiptEntry').textContent = booking.entryTime ? booking.entryTime.replace('T', ' ').substring(0, 19) : '-';
        document.getElementById('receiptExit').textContent = booking.exitTime ? booking.exitTime.replace('T', ' ').substring(0, 19) : '-';
        document.getElementById('receiptAmount').textContent = 'Rs. ' + booking.fare.toFixed(2);
        document.getElementById('receiptCard').style.display = 'block';

        showAlert(`Slot ${slotNumber} is now free. Total Fare: Rs. ${booking.fare.toFixed(2)}`, true);
        checkoutForm.reset();
        loadSlots();
        loadAvailableCount();
    })
    .catch(err => {
        checkoutResult.textContent = '';
        document.getElementById('receiptCard').style.display = 'none';
        showAlert(err.message, false);
    });
});

/**
 * Feature 5: Search booking history by vehicle number with safe array validation.
 */
historyForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const vNum = document.getElementById('searchVehicleNumber').value.trim();

    fetch(`${API_BASE}/history?vehicleNumber=${encodeURIComponent(vNum)}`)
        .then(async res => {
            if (!res.ok) {
                const errData = await res.json().catch(() => ({}));
                throw new Error(errData.error || 'Failed to fetch history');
            }
            return res.json();
        })
        .then(bookings => {
            historyTableBody.innerHTML = '';
            
            // Check if response is an Array
            if (!Array.isArray(bookings) || bookings.length === 0) {
                showAlert('No booking records found for ' + vNum, false);
                historyTable.style.display = 'none';
                return;
            }

            bookings.forEach(b => {
                const row = document.createElement('tr');
                row.innerHTML = `
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
                historyTableBody.appendChild(row);
            });
            historyTable.style.display = 'table';
            showAlert(`Found ${bookings.length} record(s) for ${vNum}`, true);
        })
        .catch(err => {
            historyTable.style.display = 'none';
            showAlert('Error fetching history: ' + err.message, false);
        });
});

// Initial page load
loadSlots();
loadAvailableCount();

// Auto-refresh real-time count every 5 seconds
setInterval(() => {
    loadAvailableCount();
    loadSlots();
}, 5000);
