🌱 Village Compost Exchange System
A web-based platform that connects households, farmers, compost collectors, compost centres, and administrators to promote sustainable waste management and organic compost exchange.
The system helps manage waste collection, compost requests, inventory, compost allocation, notifications, and related activities through a centralized platform.
________________________________________
📌 Table of Contents
1.	About the Project
2.	Features
3.	Technology Stack
4.	Project Architecture
5.	Prerequisites
6.	Clone the Repository
7.	PostgreSQL Database Setup
8.	Backend Setup – Spring Boot
9.	Frontend Setup – HTML, CSS, JavaScript
10.	Running the Complete Application
11.	User Roles
12.	Application Workflow
13.	API Configuration
14.	Troubleshooting
15.	Common Commands
16.	Security Notes
17.	Future Enhancements
18.	Contributing
________________________________________
1. About the Project
The Village Compost Exchange System is designed to improve waste management by connecting different participants in a village-level compost ecosystem.
Households can make waste pickup requests, collectors can manage collection activities, farmers can request compost, and administrators can manage the overall system.
The platform provides a centralized way to coordinate these activities and encourage recycling and sustainable agriculture.
Objectives
•	Encourage proper waste segregation and recycling.
•	Connect households with waste collectors.
•	Support farmers in obtaining organic compost.
•	Maintain compost inventory and allocation records.
•	Provide notifications about important activities.
•	Improve transparency in village-level waste management.
________________________________________
2. Features
Authentication and Authorization
•	User registration and login.
•	JWT-based authentication.
•	Role-based access control.
•	Password encryption using BCrypt.
Household
•	Register and log in.
•	Submit waste pickup requests.
•	View pickup request information.
•	Track pickup history.
•	Receive notifications.
Collector
•	View assigned pickup activities.
•	Manage waste collection activities.
•	Update pickup status.
•	View relevant collection information.
Farmer
•	Submit compost requests.
•	View personal compost requests.
•	View compost allocations.
•	Track allocation status.
•	Receive notifications about approvals, rejections, and allocations.
Administrator
•	Manage system activities.
•	View compost requests.
•	Approve or reject compost requests.
•	Manage compost inventory.
•	Allocate compost to farmers.
•	View relevant system records.
•	Receive notifications about important activities.
Notification System
•	Notifications for new compost requests.
•	Notifications when requests are approved or rejected.
•	Notifications when compost is allocated.
•	Notifications when an allocation is completed.
•	Unread notification count.
________________________________________
3. Technology Stack
Component	Technology
Frontend	HTML5
Styling	CSS3
Frontend scripting	JavaScript
Backend	Java, Spring Boot
Backend architecture	REST API
Database	PostgreSQL
ORM	Spring Data JPA / Hibernate
Authentication	Spring Security, JWT
Password encryption	BCrypt
Build tool	Maven
Development IDE	IntelliJ IDEA / VS Code
Frontend development server	VS Code Live Server
________________________________________
4. Project Architecture
The application follows a three-layer architecture.
                 USERS
                   |
                   v
       HTML + CSS + JavaScript
               FRONTEND
                   |
              REST API
                   |
                   v
          SPRING BOOT BACKEND
                   |
          Spring Security + JWT
                   |
          Service and Repository
                   |
                   v
              POSTGRESQL
               DATABASE
Frontend
The frontend communicates with the backend through HTTP REST API requests.
Backend
The Spring Boot application handles:
•	Business logic.
•	User authentication.
•	Authorization.
•	Database operations.
•	Notification management.
•	Request and allocation management.
Database
PostgreSQL stores user accounts and application data.
________________________________________
5. Prerequisites
Install the following software before running the project.
Software	Purpose
Git	Clone the repository
Java JDK	Run Spring Boot
PostgreSQL	Database
pgAdmin (optional)	Manage PostgreSQL
IntelliJ IDEA or VS Code	Backend development
VS Code Live Server	Run frontend
Verify installations
Open a terminal and execute:
Git
git --version
Java
java -version
PostgreSQL
psql --version
Maven
The project includes a Maven wrapper, so a separate Maven installation is not required.
Java must match the version configured in the backend pom.xml. This project was developed with Java 24; use a compatible JDK when building it.
________________________________________
6. Clone the Repository
Open a terminal in the directory where you want to store the project.
git clone YOUR_GITHUB_REPOSITORY_URL
Move into the project directory:
cd Village-Compost-Exchange-System
Replace YOUR_GITHUB_REPOSITORY_URL with the actual GitHub repository URL.
The project contains separate frontend and backend directories. The exact directory names should match the repository.
________________________________________
7. PostgreSQL Database Setup
Step 1: Start PostgreSQL
Make sure the PostgreSQL server is running.
You can use:
•	PostgreSQL command-line tools.
•	pgAdmin.
•	PostgreSQL services on Windows or Linux.
Step 2: Create the database
Open pgAdmin and connect to your PostgreSQL server.
Open the Query Tool and execute:
CREATE DATABASE village_compost;
Alternatively, use the PostgreSQL terminal:
CREATE DATABASE village_compost;
Step 3: Configure database credentials
Open the backend project.
Navigate to:
src/main/resources/application.properties
Configure the PostgreSQL connection using the credentials created on your computer.
Example:
spring.datasource.url=jdbc:postgresql://localhost:5432/village_compost
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
Replace the username and password with your actual PostgreSQL credentials.
Important: The database name must remain village_compost unless you also update the application configuration.
Step 4: Configure Hibernate
Check the existing Hibernate configuration in application.properties.
For example:
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
Use the schema-generation setting already intended by the project.
If the project uses update, Hibernate can create or update mapped tables when the application starts. If it uses another setting, follow that configuration and provide the required SQL scripts or migration files.
Do not assume that creating the empty database alone will create all tables.
________________________________________
8. Backend Setup – Spring Boot
Step 1: Open the backend
Open the backend directory in IntelliJ IDEA or VS Code.
The backend should contain files similar to:
backend/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── village/
        │           └── compost/
        │
        └── resources/
            └── application.properties
The Java package root is:
com.village.compost
Step 2: Check application.properties
Make sure the PostgreSQL connection details are correct.
The backend normally runs on:
http://localhost:8080
If a different port is configured, update the frontend API base URL accordingly.
Step 3: Build the backend
Windows:
Open Command Prompt or PowerShell inside the backend directory.
.\mvnw.cmd clean install
Linux / macOS:
./mvnw clean install
Step 4: Run the backend
Windows:
.\mvnw.cmd spring-boot:run
Linux / macOS:
./mvnw spring-boot:run
Alternatively, run the main Spring Boot application class from IntelliJ IDEA.
Step 5: Verify backend startup
Check the terminal logs.
The application should start without database connection or configuration errors.
Backend address:
http://localhost:8080
________________________________________
9. Frontend Setup – HTML, CSS, JavaScript
The frontend uses plain HTML, CSS, and JavaScript. React, Angular, or another frontend framework is not required.
Step 1: Open the frontend directory
Open the frontend folder in Visual Studio Code.
A typical structure is:
village-compost-frontend/
│
├── index.html
├── register.html
├── household.html
├── collector.html
├── farmer.html
├── admin.html
│
├── css/
│   ├── common.css
│   ├── auth.css
│   └── role-specific stylesheets
│
├── js/
│   ├── api.js
│   ├── auth.js
│   ├── notifications.js
│   ├── household.js
│   ├── collector.js
│   ├── farmer.js
│   └── admin.js
│
└── assets/
The exact files may vary according to the latest repository version.
Step 2: Install Live Server
1.	Open Visual Studio Code.
2.	Open Extensions.
3.	Search for Live Server.
4.	Install the extension.
Step 3: Configure the API URL
Open:
js/api.js
Find the API base URL.
It should point to the backend running on the same computer:
const API_BASE_URL = "http://localhost:8080";
If your backend runs on another port, update this value.
Step 4: Start the frontend
1.	Open index.html.
2.	Right-click inside the editor.
3.	Select Open with Live Server.
The frontend should open at an address similar to:
http://127.0.0.1:5500
or:
http://localhost:5500
________________________________________
10. Running the Complete Application
Follow this order whenever you start the application.
Step 1: Start PostgreSQL
Ensure the PostgreSQL server is running.
Step 2: Start Spring Boot
Run:
.\mvnw.cmd spring-boot:run
Wait until the application starts successfully.
Step 3: Start the frontend
Open index.html using Live Server.
Step 4: Register a user
Open the registration page and create an account.
Step 5: Log in
Log in using the registered account.
The application uses JWT authentication. The frontend sends the authentication token with protected API requests.
Step 6: Use the appropriate dashboard
Access the dashboard corresponding to the account's role.
________________________________________
11. User Roles
The application defines the following roles:
Role	Responsibility
HOUSEHOLD	Waste pickup requests
COLLECTOR	Waste collection activities
FARMER	Compost requests and allocations
ADMIN	Overall system administration
COMPOST_CENTER	Compost-centre-related role, subject to implemented permissions
Access to protected operations is controlled by Spring Security.
Administrator account: Create or configure an administrator using the project's supported registration or account-provisioning process. Do not assume that public registration automatically grants administrator privileges.
________________________________________
12. Application Workflow
Waste Collection Workflow
Household
    |
    v
Submit Waste Pickup Request
    |
    v
Collector
    |
    v
Manage Collection
    |
    v
Update Pickup Status
Compost Request Workflow
Farmer
   |
   v
Submit Compost Request
   |
   v
Admin Receives Notification
   |
   v
Admin Reviews Request
   |
   +-------------------+
   |                   |
   v                   v
 Approve              Reject
   |                   |
   v                   v
Farmer Notified     Farmer Notified
   |
   v
Admin Allocates Compost
   |
   v
Farmer Receives Allocation
   |
   v
Farmer Completes Allocation
   |
   v
Admin Receives Notification
________________________________________
13. API Configuration
The backend exposes REST APIs.
Default backend URL:
http://localhost:8080
Example API endpoints include:
Method	Endpoint	Purpose
GET	/api/notifications/my	Get current user's notifications
GET	/api/notifications/unread-count	Get unread notification count
PUT	/api/notifications/{id}/read	Mark notification as read
Other authentication, household, collector, farmer, compost, inventory, and allocation endpoints are defined by their respective backend controllers.
JWT Authentication
For protected endpoints, send the token in the Authorization header:
Authorization: Bearer YOUR_JWT_TOKEN
The frontend API utility should attach the token automatically after login.
CORS Configuration
The backend must allow requests from the frontend development server.
For example:
http://localhost:5500
http://127.0.0.1:5500
If you access the frontend through a LAN IP address, that exact origin must also be permitted in the backend CORS configuration.
________________________________________
14. Troubleshooting
Problem 1: PostgreSQL connection refused
Possible reason: PostgreSQL is not running.
Solution:
•	Start the PostgreSQL service.
•	Check the port, usually 5432.
•	Verify the database URL and credentials.
Problem 2: Database does not exist
Error may indicate:
database "village_compost" does not exist
Solution:
Create it:
CREATE DATABASE village_compost;
Problem 3: Failed to determine a suitable driver class
Possible reasons:
•	PostgreSQL JDBC dependency is missing.
•	Database configuration is incorrect.
•	Spring Boot cannot load the configured driver.
Solution:
Check the PostgreSQL dependency in pom.xml and the datasource settings in application.properties.
Problem 4: CORS error in browser
Possible reason: The frontend origin is not allowed by Spring Security CORS configuration.
Solution:
Add the exact Live Server origin to the backend's allowed origins and restart Spring Boot.
Problem 5: 401 Unauthorized
Possible reasons:
•	User is not logged in.
•	JWT token is missing.
•	Token has expired or is invalid.
Solution:
Log in again and check whether the frontend sends the Authorization header.
Problem 6: 403 Forbidden
Possible reasons:
•	The account does not have the required role.
•	The requested operation is not permitted for that role.
•	CSRF, security matcher, or authorization configuration blocks the request.
Solution:
Check the Spring Security configuration and the logged-in user's role.
Problem 7: Frontend cannot connect to backend
Solution:
•	Confirm Spring Boot is running.
•	Check API_BASE_URL in js/api.js.
•	Verify the backend port.
•	Check the browser developer console.
Problem 8: Tables are not created
Possible reason: Hibernate schema generation is disabled or the project requires SQL migrations.
Solution:
Check spring.jpa.hibernate.ddl-auto and any database initialization or migration scripts included in the repository.
Problem 9: Notifications are empty
Possible reason: No notification has been created for the logged-in user.
Solution:
Perform the relevant action, such as submitting a compost request, approving a request, or allocating compost. Then call:
GET /api/notifications/my
Make sure you use the intended recipient's authentication token.
________________________________________
15. Common Commands
Clone the project
git clone YOUR_GITHUB_REPOSITORY_URL
Build backend – Windows
.\mvnw.cmd clean install
Run backend – Windows
.\mvnw.cmd spring-boot:run
Build backend – Linux / macOS
./mvnw clean install
Run backend – Linux / macOS
./mvnw spring-boot:run
Package backend
.\mvnw.cmd clean package
Run packaged application
java -jar target/YOUR_APPLICATION_JAR.jar
Check Git status
git status
________________________________________
16. Security Notes
•	Never upload database passwords to a public GitHub repository.
•	Never expose JWT signing secrets.
•	Use BCrypt for password storage.
•	Keep role-based authorization enabled.
•	Do not commit personal database credentials.
•	Use environment variables or an untracked local configuration file for private credentials.
•	Do not expose administrator privileges through unrestricted public registration.
•	In production, use HTTPS and configure secure CORS origins.
•	Avoid using development-only configuration in production.
________________________________________
17. Future Enhancements
Potential future improvements include:
•	Real-time notifications.
•	GPS-based waste pickup tracking.
•	Advanced compost inventory management.
•	Analytics and reporting dashboards.
•	Mobile-friendly user interface.
•	Village-level waste management statistics.
•	Integration with local government services.
________________________________________
18. Contributing
Contributions and suggestions are welcome.
To contribute:
1.	Fork the repository.
2.	Create a new branch.
3.	Make your changes.
4.	Test the application locally.
5.	Commit your changes.
6.	Submit a pull request.
git checkout -b feature/your-feature-name

git add .

git commit -m "Add your feature"

git push origin feature/your-feature-name
________________________________________
👨‍💻 Project Information
Project Name: Village Compost Exchange System
Backend: Java + Spring Boot
Frontend: HTML + CSS + JavaScript
Database: PostgreSQL
Architecture: REST API-based Full Stack Application
Build Tool: Maven
________________________________________
🌍 Project Vision
To create a cleaner and greener village environment by connecting households, waste collectors, farmers, compost centres, and administrators through a centralized digital compost exchange platform.
Reduce Waste. Create Compost. Build a Greener Village. 🌱

