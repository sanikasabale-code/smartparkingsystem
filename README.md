# Smart Parking System

## 1. Project Overview

The Smart Parking System is an academic Java application developed using Spring Boot. It is intended to support parking management by organizing parking-related information and helping users manage parking operations more efficiently.

> Update this section if your implemented application has a different scope. Describe only features that are actually present in your project.

## 2. Objectives

- Simplify parking management.
- Maintain parking-related information.
- Support efficient handling of parking operations.
- Reduce manual effort in managing parking information.

## 3. Technologies Used

- **Programming Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Database:** MySQL (if configured in the project)
- **Data Access:** Spring Data JPA (if used)
- **Testing:** Browser / application interface

## 4. Project Structure

```text
CIE4_JAVAF/
├── .github/
├── .vscode/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── target/
├── .gitattributes
├── .gitignore
├── HELP.md
├── pom.xml
└── README.md
```

The exact Java package and class structure is available inside the `src` directory.

## 5. Prerequisites

- Java Development Kit (JDK)
- Maven (or the Maven Wrapper included with the project)
- MySQL, if the application is configured to use it
- A web browser

## 6. Setup and Execution

### Step 1: Open the project

Open the project root folder containing `pom.xml` in Visual Studio Code or a terminal.

### Step 2: Configure the database

If the project uses MySQL, make sure the MySQL server is running. Check `src/main/resources/application.properties` and update the database URL, username, and password to match your local setup.

Do not publish real database passwords or other secrets in this README or in a public repository.

### Step 3: Build the project

On Windows, run:

```powershell
.\mvnw.cmd clean install
```

If the Maven Wrapper is unavailable, use:

```powershell
mvn clean install
```

### Step 4: Start the application

On Windows, run:

```powershell
.\mvnw.cmd spring-boot:run
```

Or, if Maven is installed:

```powershell
mvn spring-boot:run
```

### Step 5: Open the application

Check the startup messages in the terminal for the port number. If it starts on the default Spring Boot port, open:

```text
http://localhost:8080
```

If your project defines REST API endpoints, open the specific endpoint URL rather than only the root address. The root URL may show an error if no home page or root mapping has been implemented.

## 7. Main Features

List the features that are implemented in the current version. Possible examples (include only those present in your code):

- Parking slot information management
- Checking parking slot availability
- Vehicle information management
- Parking slot booking or allocation
- Input validation

## 8. Testing

Test the application using its available web interface or API endpoints.

| Test Case | Test Input / Action | Expected Result |
|---|---|---|
| TC01 – Valid input | Submit complete, valid parking information. | The application should accept and process the information. |
| TC02 – Missing input | Submit the form or request with a required field left blank. | The application should show a validation message or reject the request. |
| TC03 – Invalid URL | Open an endpoint that does not exist. | The application should return an appropriate not-found response. |

Record the actual result and Pass/Fail status only after running each test.

## 9. Screenshots

Insert 2–3 screenshots captured from your running application, for example:

1. Application successfully running on localhost.
2. A successful parking-related operation.
3. A validation message or unsuccessful request.

Add screenshots here after testing the application.

## 10. Future Enhancements

Possible future improvements include:

- Real-time parking availability updates
- Online parking reservations
- QR-code-based entry and exit
- Online payment integration
- A mobile-friendly interface

These are suggestions for future work, not claims about existing functionality.

## 11. Conclusion

The Smart Parking System is intended to make parking management more organized and convenient. The project uses Java and Spring Boot, with Maven for build management. Its final capabilities depend on the features implemented in the application.

---

**Project:** Smart Parking System  
**Project folder:** `CIE4_JAVAF`  
**Backend:** Java / Spring Boot  
**Build tool:** Maven

