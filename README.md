Employee CRUD REST API with File Export (YAML FILE ATTACHED)
This is a comprehensive Spring Boot application that provides a complete RESTful API for managing employee data with advanced file export capabilities.

Key Features:
Employee Management

Full CRUD operations (Create, Read, Update, Delete)

Support for partial updates using PATCH

Employee search by first name

MySQL database integration

Multi-Format File Export

Standard Formats: PDF, Excel, Word documents

JasperReports Integration: Professional report generation with pre-designed templates

Base64 Encoded Responses: Files delivered as encoded strings for easy client consumption

JasperReports Capabilities

Pre-designed report templates (Simple_Blue_1.jrxml/.jasper)

Dynamic parameter support for custom report titles

On-the-fly report compilation

Support for filtered reports based on employee criteria

Technical Stack:
Backend: Spring Boot 3.x, Spring Data JPA, Spring MVC

Database: MySQL with Hibernate ORM

Reporting: JasperReports for professional document generation

File Export: Apache POI (Excel/Word), iText (PDF)

API Documentation: OpenAPI 3.0 (Swagger)

Architecture:
The application follows a layered architecture with clear separation of concerns:

Controller Layer: REST endpoints handling HTTP requests

Service Layer: Business logic and transaction management

Repository Layer: Data access and persistence

Entity Layer: JPA entities mapping to database tables

File Management: Export functionality with multiple format support

