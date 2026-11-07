# Chat System

A simple, monolithic chat-backend API built with Spring Boot and MongoDB. It supports user registration, credential verification, persistent direct messages, and chronological conversation history.

> **Project status:** This is an early backend implementation. Authentication currently validates a username and password only; it does not issue a token or protect message endpoints yet.

## Features

- Register users with BCrypt-hashed passwords
- Verify user credentials through a login endpoint
- Send direct messages between two user IDs
- Persist users and messages in MongoDB
- Retrieve a two-user conversation in chronological order
- Return a consistent JSON response shape
- Handle invalid arguments and unexpected server errors centrally

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 3.5.7 |
| Database | MongoDB |
| Data access | Spring Data MongoDB |
| Password hashing | BCrypt (`spring-security-crypto`) |
| Build tool | Maven |

## Project structure

```text
src/main/java/com/chat
├── controller/       # HTTP API endpoints
├── dto/              # Request and response payloads
├── exception/        # Centralized exception handling
├── model/            # MongoDB document models
├── repository/       # Spring Data repositories
├── service/          # Application logic
└── util/             # Password and logging configuration
```

## Prerequisites

- Java 25
- Maven 3.9+
- MongoDB running locally on port `27017`

## Configuration

The default database connection is defined in [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/chatdb
```

To use another database or a managed MongoDB instance, replace this value with the appropriate MongoDB connection URI. Keep credentials out of version control by supplying the URI through your environment or a local configuration file.

## Run locally

1. Start MongoDB.
2. From the project root, start the application:

   ```bash
   mvn spring-boot:run
   ```

3. The API is served on `http://localhost:8080` by default.

### Current startup note

The application entry point currently declares `static void main(...)`. Java launchers require it to be declared `public static void main(...)`, so the entry point must be corrected before the application can start. This README documents the intended API and local workflow; no code change is included here.

## API reference

All endpoints accept and return JSON. Responses use this envelope:

```json
{
  "success": true,
  "message": "Description of the result",
  "httpCode": "OK",
  "data": {}
}
```

### Register a user

`POST /api/auth/register`

```bash
curl --request POST http://localhost:8080/api/auth/register \
  --header 'Content-Type: application/json' \
  --data '{
    "username": "alice",
    "password": "change-me"
  }'
```

Returns `201 Created` when the username is new, or `409 Conflict` when it already exists.

A successful registration returns the ID needed to send messages:

```json
{
  "success": true,
  "message": "User registered successfully",
  "httpCode": "CREATED",
  "data": {
    "id": "6910d7ab4c9e2e3a1f123456",
    "username": "alice"
  }
}
```

Use `data.id` as `senderId` or `receiverId`. The password and its hash are never included in the response.

### Log in

`POST /api/auth/login`

```bash
curl --request POST http://localhost:8080/api/auth/login \
  --header 'Content-Type: application/json' \
  --data '{
    "username": "alice",
    "password": "change-me"
  }'
```

Returns `200 OK` for valid credentials and `401 Unauthorized` otherwise. This endpoint does not currently return an access token or establish a session.

### Send a message

`POST /api/messages`

```bash
curl --request POST http://localhost:8080/api/messages \
  --header 'Content-Type: application/json' \
  --data '{
    "senderId": "<alice-user-id>",
    "receiverId": "<bob-user-id>",
    "content": "Hello, Bob!"
  }'
```

Example successful response:

```json
{
  "success": true,
  "message": "Message sent successfully",
  "httpCode": "CREATED",
  "data": {
    "id": "...",
    "senderId": "...",
    "receiverId": "...",
    "content": "Hello, Bob!",
    "timestamp": 1794038400000
  }
}
```

### Get a conversation

`GET /api/messages/conversation/{userId1}/{userId2}`

```bash
curl http://localhost:8080/api/messages/conversation/<alice-user-id>/<bob-user-id>
```

Returns all messages exchanged by the two users, ordered from oldest to newest.

## Data model

### User

| Field | Description |
| --- | --- |
| `id` | MongoDB document ID |
| `username` | User's login name |
| `password` | BCrypt-hashed password |

### Message

| Field | Description |
| --- | --- |
| `id` | MongoDB document ID |
| `senderId` | Sending user's ID |
| `receiverId` | Receiving user's ID |
| `content` | Message body |
| `timestamp` | Unix timestamp in milliseconds, set when the message is created |

## Current limitations and next steps

- Login does not create a session or issue JWT/API credentials.
- Message routes are not authenticated or authorized.
- Request fields are not yet validated for empty or malformed values.
- Message creation does not verify that either user exists.
- Usernames should be enforced as unique at the database level.
- The WebSocket dependency is included but real-time messaging is not implemented.
- Automated tests have not yet been added.

Useful next enhancements would be Spring Security with token-based authentication, Bean Validation, MongoDB indexes, integration tests, pagination for long conversations, and WebSocket/STOMP support for real-time delivery.

## License

No license has been specified for this project.
