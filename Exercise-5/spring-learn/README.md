# spring-learn

Cognizant Spring Boot module assignment.

## Part 1 — Spring Core (doc 1)
- Spring Boot project setup, XML bean loading (`date-format.xml`), logging, `Country` bean
  with constructor/setter injection, singleton vs. prototype scope, `List<Country>` bean

## Part 2 — RESTful Web Services (doc 2)
- `GET /hello`, initial `Country`/`Employee`-style GET services, exception handling,
  MockMvc tests

## Part 3 — Employee & Department REST Services (doc 3)
- `employee.xml` / `department.xml` static lists, DAO → Service → Controller layers,
  `GET /employees`, `GET /departments`

## Part 4 — POST/PUT/DELETE, Validation, Global Exception Handling (doc 4)
- **CountryController** rebuilt to follow REST resource-naming conventions: class-level
  `@RequestMapping("/countries")`, with `GET`, `GET /{code}`, and `POST` all under one base URL
  (the earlier standalone `GET /country` endpoint was removed since it didn't fit this convention)
- **Country validation** – `@NotNull` / `@Size(min=2, max=2)` on `code`
- **GlobalExceptionHandler** (`@ControllerAdvice`, extends `ResponseEntityExceptionHandler`) —
  handles `MethodArgumentNotValidException` (validation failures) and
  `HttpMessageNotReadableException` (e.g. a string sent for a numeric field) globally,
  so no controller needs its own manual validation code
- **Employee / Department / Skill validation** – `@NotNull`, `@NotBlank`, `@Size(min=1,max=30)`
  on name fields; `@Min(0)` on salary; `@JsonFormat(pattern="dd/MM/yyyy")` on `dateOfBirth`.
  Added `salary`, `permanent`, `dateOfBirth`, and `skills` fields to `Employee` (not present
  in the earlier docs) plus a new `Skill` model, since this doc's validation spec required them
- **EmployeeNotFoundException** (`@ResponseStatus(HttpStatus.NOT_FOUND)`)
- **PUT /employees** → `EmployeeController.updateEmployee(@RequestBody @Valid Employee)` —
  the id to update comes from the payload, per the doc's exact method signature
- **DELETE /employees/{id}** → `EmployeeController.deleteEmployee(id)`
- **MockMvc tests** – validation errors, 404 for unknown employee id, malformed numeric id,
  delete-not-found

## Part 5 — Spring Security + JWT Authentication (JWT hands-on doc)
- **`spring-boot-starter-security`** added — by default this locks down every endpoint
  behind HTTP Basic auth with a randomly generated password (visible in the startup logs)
- **`SecurityConfig`** (`com.cognizant.springlearn.security`, `@Configuration @EnableWebSecurity`,
  extends `WebSecurityConfigurerAdapter`) — defines two in-memory users, `admin`/`pwd` (ROLE_ADMIN)
  and `user`/`pwd` (ROLE_USER), with `BCryptPasswordEncoder`
- **`AuthenticationController`** — `GET /authenticate` reads the `Authorization: Basic ...`
  header, decodes the username, and returns a signed JWT (`{"token": "..."}`) valid for 20 minutes
- **`jjwt` 0.9.0** dependency used to build/sign the token (`HS256`, signing key `"secretkey"`
  — hardcoded for this learning exercise only, per the doc's note)
- **`JwtAuthorizationFilter`** (extends `BasicAuthenticationFilter`) — intercepts every request;
  if the `Authorization` header starts with `Bearer `, it validates the JWT and marks the
  request authenticated in the Spring Security context
- Final `SecurityConfig` state: HTTP Basic stays enabled (used only by `/authenticate`),
  every other endpoint requires `anyRequest().authenticated()` via the JWT filter

### Testing the security flow manually
```bash
# 1. Everything is locked down by default
curl -s http://localhost:8090/countries
# -> 401 Unauthorized

# 2. Authenticate with HTTP Basic to get a token
curl -s -u user:pwd http://localhost:8090/authenticate
# -> {"token":"eyJhbGciOiJIUzI1NiJ9...."}

# 3. Use the token as a Bearer header for any other endpoint
curl -s -H "Authorization: Bearer <token from step 2>" http://localhost:8090/countries

# 4. A tampered/expired token is rejected
curl -s -H "Authorization: Bearer not.a.real.token" http://localhost:8090/countries
# -> 401 Unauthorized
```

Automated equivalents are in `SpringLearnApplicationTests` (`spring-security-test`'s
`httpBasic()` request post-processor covers the Basic-auth cases; the JWT tests
call `/authenticate` first, then reuse the returned token as a Bearer header).


```
spring-learn/
├── pom.xml
├── src/main/java/com/cognizant/springlearn/
│   ├── SpringLearnApplication.java
│   ├── GlobalExceptionHandler.java
│   ├── Country.java
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   └── JwtAuthorizationFilter.java
│   ├── model/
│   │   ├── Employee.java
│   │   ├── Department.java
│   │   └── Skill.java
│   ├── dao/
│   │   ├── EmployeeDao.java
│   │   └── DepartmentDao.java
│   ├── service/
│   │   ├── CountryService.java
│   │   ├── EmployeeService.java
│   │   ├── DepartmentService.java
│   │   └── exception/
│   │       ├── CountryNotFoundException.java
│   │       └── EmployeeNotFoundException.java
│   └── controller/
│       ├── HelloController.java
│       ├── CountryController.java
│       ├── EmployeeController.java
│       ├── DepartmentController.java
│       └── AuthenticationController.java
├── src/main/resources/
│   ├── application.properties   (logging config, server.port=8090)
│   ├── date-format.xml
│   ├── country.xml
│   ├── employee.xml
│   └── department.xml
└── src/test/java/com/cognizant/springlearn/
    └── SpringLearnApplicationTests.java
```

## How to run

```bash
mvn clean package
mvn spring-boot:run
```

Then try:
```bash
curl http://localhost:8090/countries
curl http://localhost:8090/countries/in
curl -i -H 'Content-Type: application/json' -X POST -s -d '{"code":"IN","name":"India"}' http://localhost:8090/countries
curl -i -H 'Content-Type: application/json' -X POST -s -d '{"code":"I","name":"India"}' http://localhost:8090/countries  # 400 validation error

curl http://localhost:8090/employees
curl -i -H 'Content-Type: application/json' -X PUT -s -d '{"id":1,"firstName":"John","lastName":"Doe","salary":60000,"permanent":true,"dateOfBirth":"15/06/1990"}' http://localhost:8090/employees
curl -i -X DELETE http://localhost:8090/employees/1

curl http://localhost:8090/departments
```

## Run tests

```bash
mvn clean test
```

## Notes

- **Placeholder Employee data**: doc 3 asked to reuse real employee sample data from an
  earlier Angular module that wasn't included in any of these docs — `employee.xml` still
  has 4 placeholder employees (now with salary, permanent, dateOfBirth, and skills added
  for this doc's validation requirements). Swap in your real data if it differs.
- **URL convention change**: the standalone `GET /country` endpoint (hardcoded to return
  India) from doc 2 was removed in this update since it didn't fit the "same base URL for
  all methods" naming convention this doc introduces. Use `GET /countries/in` instead.
- The doc's MockMVC snippet in doc 2 used `status().isBadRequest()` / reason
  `"Country Not found"` for the country-not-found case; the actual `@ResponseStatus` is
  `NOT_FOUND` / `"Country not found"` — tests here match the real implementation.
- Cognizant's internal Maven proxy flags (`-Dhttp.proxyHost=proxy.cognizant.com ...`)
  are only needed on Cognizant's internal network — drop them on a personal machine.
