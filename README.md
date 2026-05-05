# AI Document Assistant Backend

A Spring Boot-based REST API for document management and AI-powered document analysis using Google's Gemini AI.

## Overview

This backend service provides:
- Document upload and management
- AI-powered document summarization
- Semantic search with embedding-based similarity matching
- User authentication and authorization
- Secure document storage and retrieval

## Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA** - Database ORM
- **Spring Security** - Authentication & Authorization
- **Google Gemini API** - AI/ML capabilities
- **MySQL** - Database
- **Maven** - Build tool
- **Docker** - Containerization

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Google Gemini API key
- Docker (optional, for containerized deployment)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ai-doc-assistant
```

### 2. Configure Environment Variables

Create an `application.properties` file or set environment variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ai_doc_assistant
spring.datasource.username=<db_username>
spring.datasource.password=<db_password>
spring.jpa.hibernate.ddl-auto=update

# Gemini API Configuration
gemini.api.key=<your-gemini-api-key>
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models

# File Upload Configuration
file.upload.dir=./uploads
file.max.size=10485760

# Server Configuration
server.port=8080
```

### 3. Install Dependencies

```bash
mvn clean install
```

## Building and Running

### Build the Project
```bash
mvn clean package
```

### Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or using the JAR file
java -jar target/ai-doc-assistant-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

### Run with Docker
```bash
docker build -t ai-doc-assistant .
docker run -p 8080:8080 ai-doc-assistant
```

## Project Structure

```
src/main/java/com/example/ai_doc_assistant/
├── ai/                     # AI/ML services (Gemini, embeddings)
├── config/                 # Configuration classes (Security, etc.)
├── controller/             # REST API endpoints
├── dto/                    # Data Transfer Objects
├── entity/                 # JPA Entity classes
├── exception/              # Custom exceptions
├── repository/             # Data access layer
├── security/               # Security-related classes
└── service/                # Business logic layer
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Documents
- `GET /api/documents` - Get all documents for current user
- `POST /api/documents/upload` - Upload new document
- `GET /api/documents/{id}` - Get document details
- `DELETE /api/documents/{id}` - Delete document

### Search & Analysis
- `POST /api/documents/search` - Semantic search across documents
- `POST /api/documents/{id}/summarize` - Generate summary using AI

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=YourTestClass

# Run with coverage
mvn test jacoco:report
```

## Database Schema

The application uses Hibernate JPA for ORM. Key entities include:
- **User** - User accounts and credentials
- **Document** - Uploaded documents metadata
- **DocumentEmbedding** - Vector embeddings for semantic search

Run migrations with:
```bash
mvn flyway:migrate
```

## Configuration

### Application Properties

See `src/main/resources/application.properties` for all configuration options.

Key configurations:
- `spring.jpa.hibernate.ddl-auto` - Database schema generation strategy
- `spring.security.user.*` - Default security settings
- `gemini.api.key` - Gemini API authentication

## Security

- JWT-based authentication
- Role-based access control (RBAC)
- CORS configuration for frontend integration
- Secure password hashing with BCrypt

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Database Connection Issues
- Ensure MySQL is running
- Verify connection URL and credentials
- Check database exists: `CREATE DATABASE ai_doc_assistant;`

### API Key Issues
- Verify Gemini API key is valid
- Check API quotas and billing

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add new feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Create a Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue in the repository.
