[![Java CI with Maven](https://github.com/lhducc/testng/actions/workflows/ci.yml/badge.svg)](https://github.com/lhducc/testng/actions/workflows/ci.yml)

# Blood Donation Support System

This is a Spring Boot project for Blood Donation Support System, providing user registration, authentication, and blood donation management.

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- TestNG for unit testing
- Mockito for mocking
- Maven for build & dependency management
- GitHub Actions for CI/CD

---

## 🚀 Project Structure

src
└── main
└── java
└── com.example.BloodDonationSupportSystem
├── controller
├── service
├── repository
├── entity
└── dto
└── test
└── java
└── com.example.BloodDonationSupportSystem
└── service.authaccountservice


---

## ⚙️ Running the project

### 1️⃣ Prerequisites

- JDK 17+
- Maven 3.8+
- Database: SQL Server (or any compatible DB)

### 2️⃣ Run locally

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run


## Running Tests
The project uses TestNG and Mockito for unit testing.

mvn test

## CI/CD with GitHub Actions
This project includes a simple CI pipeline that automatically:

Builds the project

Runs unit tests on every push and pull request to main or master.

You can find the GitHub Actions workflow file at:
.github/workflows/ci.yml

## Future Improvements
Add integration tests.

Add full deployment pipeline (CD).

Add Docker support.

Add monitoring and logging.

### Connect me via: pduc2200@gmail.com

#### &#169; 2025

