# **Employee CRUD REST API + File Export**

This app manages employees and lets you export their data as files.

It started as a basic CRUD project but grew into something more practical .. now it can generate PDF, Excel, and Word reports, and even use JasperReports for fancy custom PDFs.

---

The project follows a clean code, layered architecture:

- Controller handles HTTP requests.
- Service layer keeps the business logic clear and testable.
- DAO/Repository handles database access.
- fileManagement package is fully separated to keep file export logic modular and easy to extend.

---

# **What’s inside**

- Full CRUD for employees (add, view, edit, delete).
- Simple search by ID or first name.
- Export employees to:
    - PDF
    - Excel
    - Word
    - Jasper-powered PDFs with custom templates.
- Add your own file name and report title through query parameters.
- Get the file either as a direct download or as a Base64 string if you need to embed it somewhere.

---

# You will find these endpoints in the yaml file:

| **Method** | **Endpoint** | **Use case** |
| --- | --- | --- |
| GET | /api/employees | Get all employees |
| GET | /api/employees/{id} | Get one employee by ID |
| GET | /api/employeesByFN/{FN} | Get employees by first name |
| POST | /api/employees | Add a new employee |
| PUT | /api/employees | Update an employee |
| DELETE | /api/employees/{id} | Delete an employee |
| GET | /api/export | Export employees (PDF/Excel/Word) |
| GET | /api/export/filtered | Export with filters + Jasper reports |
| POST | /api/compile-report | Compile Jasper templates |
| GET | /api/export/jasper-test | Quick Jasper test report |

 **Example: exporting a PDF**

GET http://localhost:8081/api/export?format=pdf&fileName=employees&reportTitle=Employee%20List

- format → pdf | excel | word | jasper-pdf
- fileName → your custom file name
- reportTitle → optional report title shown in Jasper PDF

You’ll get a ready-to-download file (or Base64 if that’s how you want it).

---

# **JasperReports**

- Jasper is used here to create nicely formatted PDF reports.
- The report layout comes from a .jrxml template.. you’ll find an example at:

src/main/resources/reports/Simple_Blue_1.jrxml

You can open this file and customize the look (add logos, colors, headers, change fonts, etc.).

**Compilation??**

When you modify a .jrxml file or add a new one, it must be compiled into a .jasper file before it can be used.

That’s what the /api/compile-report endpoint is for:

POST /api/compile-report?reportName=Simple_Blue_1

This will take your updated Simple_Blue_1.jrxml and turn it into Simple_Blue_1.jasper.

**How to use the Compiled Reports??**

Once it’s compiled, the export APIs like:

- /api/export
- /api/export/filtered

can use the compiled .jasper file to generate PDFs (or other formats) directly.

You don’t need to compile again unless you change the template.

---

# **How to run it?**

1. Clone the project.
2. Connect to your database.
3. Create Employee table like our entity.
4. Run CrudrestApplication.java.
5. Import the yaml file using postman.
6. Test using Postman at:
http://localhost:8081/api
