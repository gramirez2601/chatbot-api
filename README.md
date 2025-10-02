# Chatbot API - Backend (Spring Boot)

This project is the main backend service for the chatbot application, developed as part of a technical assessment for Xumtech.

## Architecture

This service is the final layer in a 3-component architecture:

**Angular (UI) → BFF in Python (Gateway/Security) → Spring Boot (Business Logic)**

This backend is designed to be secure and not directly exposed to the internet. It only accepts requests that have been previously signed by the BFF with a shared secret (HMAC-SHA256).

## Features

-   REST API for chatbot logic.
-   Answer search with fuzzy matching using Levenshtein distance.
-   Security validation via HMAC-SHA256 signature and timestamp to prevent replay attacks.
-   In-memory database (H2) initialized at startup.

## How to Run

### Prerequisites
-   Java 17
-   Maven

### Configuration
The `application.properties` file requires the following variables:
-   `chatbot.api.key`: The public API key.
-   `chatbot.api.secret`: The shared secret with the BFF for HMAC validation.

### Execution
From the project root, run the following command:
```bash
./mvnw spring-boot:run