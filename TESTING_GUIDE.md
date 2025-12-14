# JUnit Testing Guide for Clinic System

## Overview
This guide explains how to run and understand the JUnit tests for the Clinic Management System microservices.

## Test Structure

### Services with Tests
```
├── doctor-service/src/test/java/
│   └── DoctorControllerTest.java
├── patient-service/src/test/java/
│   └── PatientControllerTest.java
├── appointment-service/src/test/java/
│   └── AppointmentServiceTest.java
└── billing-service/src/test/java/
    └── BillingServiceTest.java
```

## Running Tests

### Run All Tests for a Service
```bash
# Doctor Service
cd doctor-service
mvnw test

# Patient Service
cd patient-service
mvnw test

# Appointment Service
cd appointment-service
mvnw test

# Billing Service
cd billing-service
mvnw test
```

### Run All Tests for All Services
```powershell
# PowerShell script to run all tests
Get-ChildItem -Directory -Filter "*-service" | ForEach-Object {
    Write-Host "Testing $($_.Name)..." -ForegroundColor Cyan
    Set-Location $_.FullName
    .\mvnw test
    Set-Location ..
}
```

### Run Specific Test Class
```bash
mvnw test -Dtest=DoctorControllerTest
```

### Run Specific Test Method
```bash
mvnw test -Dtest=DoctorControllerTest#testAddDoctor
```

## Test Coverage

### Doctor Service Tests
**File:** `DoctorControllerTest.java`

| Test Method | Purpose | Coverage |
|------------|---------|----------|
| `testAddDoctor` | Verify doctor creation | POST /doctors |
| `testGetAllDoctors` | Verify retrieving all doctors | GET /doctors |
| `testGetDoctorById` | Verify getting single doctor | GET /doctors/{id} |
| `testGetDoctorById_NotFound` | Verify 404 handling | Error case |
| `testUpdateDoctor` | Verify doctor update | PUT /doctors/{id} |
| `testUpdateDoctor_NotFound` | Verify update 404 | Error case |
| `testDeleteDoctor` | Verify doctor deletion | DELETE /doctors/{id} |
| `testSearchBySpecialization` | Verify search by specialty | GET /doctors/search |
| `testGetAvailableDoctors` | Verify availability filter | GET /doctors/available |

**Mocking Strategy:**
- Uses `@WebMvcTest` for controller testing
- Mocks `DoctorRepository` with `@MockBean`
- Tests HTTP layer with `MockMvc`

### Patient Service Tests
**File:** `PatientControllerTest.java`

| Test Method | Purpose | Coverage |
|------------|---------|----------|
| `testAddPatient` | Verify patient creation | POST /patients |
| `testGetAllPatients` | Verify retrieving all patients | GET /patients |
| `testGetPatientById` | Verify getting single patient | GET /patients/{id} |
| `testGetPatientById_NotFound` | Verify 404 handling | Error case |
| `testUpdatePatient` | Verify patient update | PUT /patients/{id} |
| `testDeletePatient` | Verify patient deletion | DELETE /patients/{id} |
| `testSearchByName` | Verify name search | GET /patients/search |
| `testGetPatientsByAge` | Verify age filter | GET /patients/age/{age} |

**Mocking Strategy:**
- Uses `@WebMvcTest` for controller testing
- Mocks `PatientRepository` with `@MockBean`
- Tests HTTP layer with `MockMvc`

### Appointment Service Tests
**File:** `AppointmentServiceTest.java`

| Test Method | Purpose | Coverage |
|------------|---------|----------|
| `testBookAppointment_Success` | Verify full booking flow | Orchestration |
| `testBookAppointment_NotificationContent` | Verify notification message | Notification |
| `testBookAppointment_BillingServiceCall` | Verify billing creation | Billing integration |
| `testBookAppointment_ValidatesPatient` | Verify patient validation | Patient check |
| `testBookAppointment_ValidatesDoctor` | Verify doctor validation | Doctor check |
| `testBookAppointment_SavesAppointment` | Verify appointment save | Persistence |
| `testBookAppointment_FutureDate` | Verify date handling | Date logic |

**Mocking Strategy:**
- Uses `@ExtendWith(MockitoExtension.class)` for service testing
- Mocks `AppointmentRepository` and `RestTemplate`
- Tests microservice orchestration
- Verifies inter-service communication

### Billing Service Tests
**File:** `BillingServiceTest.java`

| Test Method | Purpose | Coverage |
|------------|---------|----------|
| `testCreateBill` | Verify bill creation | Bill generation |
| `testGetBillById` | Verify bill retrieval | GET operation |
| `testGetBillById_NotFound` | Verify 404 handling | Error case |
| `testGetBillsByPatient` | Verify patient bills | Patient history |
| `testGetBillsByPatient_NoBills` | Verify empty result | Empty case |
| `testMarkAsPaid` | Verify payment marking | Status update |
| `testMarkAsPaid_NotFound` | Verify update error | Error case |
| `testDeleteBill` | Verify bill deletion | DELETE operation |
| `testCreateBill_MultipleForSamePatient` | Verify multiple bills | Multiple bills |
| `testMarkAsPaid_AlreadyPaid` | Verify idempotency | Status logic |

**Mocking Strategy:**
- Uses `@ExtendWith(MockitoExtension.class)` for service testing
- Mocks `BillRepository`
- Tests business logic layer

## Test Patterns Used

### 1. AAA Pattern (Arrange-Act-Assert)
All tests follow this structure:
```java
@Test
void testExample() {
    // Given - Setup test data and mocks
    Doctor doctor = new Doctor();
    when(repository.save(any())).thenReturn(doctor);
    
    // When - Execute the action
    Doctor result = service.addDoctor(doctor);
    
    // Then - Verify the outcome
    assertNotNull(result);
    verify(repository).save(any());
}
```

### 2. Mock-based Testing
Using Mockito to isolate units:
```java
@Mock
private DoctorRepository doctorRepository;

@InjectMocks
private DoctorController doctorController;
```

### 3. HTTP Layer Testing
Using MockMvc for controller tests:
```java
mockMvc.perform(post("/doctors")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(doctor)))
    .andExpect(status().isOk());
```

## Understanding Test Results

### Success Output
```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Failure Output
```
[ERROR] Tests run: 10, Failures: 1, Errors: 0, Skipped: 0
[ERROR] testAddDoctor  Time elapsed: 0.123 s  <<< FAILURE!
Expected status:200 but was:400
```

## Common Test Commands

### Generate Test Reports
```bash
mvnw test
# Reports generated in: target/surefire-reports/
```

### Run Tests with Coverage (if Jacoco is configured)
```bash
mvnw clean test jacoco:report
# View report: target/site/jacoco/index.html
```

### Skip Tests During Build
```bash
mvnw clean package -DskipTests
```

### Run Tests in Debug Mode
```bash
mvnw test -Dmaven.surefire.debug
```

## Best Practices

### ✅ DO
- Run tests before committing code
- Write tests for new features
- Keep tests independent
- Use descriptive test names
- Test both success and error cases
- Mock external dependencies

### ❌ DON'T
- Depend on test execution order
- Use actual databases in unit tests
- Make network calls in tests
- Share state between tests
- Skip writing tests for "simple" code

## Integration with CI/CD

### GitHub Actions Example
```yaml
name: Test Microservices

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Test Doctor Service
        run: cd doctor-service && ./mvnw test
      - name: Test Patient Service
        run: cd patient-service && ./mvnw test
      - name: Test Appointment Service
        run: cd appointment-service && ./mvnw test
      - name: Test Billing Service
        run: cd billing-service && ./mvnw test
```

## Troubleshooting

### Tests Fail to Run
**Problem:** Maven wrapper not executable
```bash
# Solution (Git Bash/Linux)
chmod +x mvnw

# Solution (Windows)
# Use mvnw.cmd instead
```

### Dependency Issues
**Problem:** Missing test dependencies
```xml
<!-- Add to pom.xml if missing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Port Already in Use
**Problem:** Tests fail due to port conflicts
```java
// Solution: Use random port in test properties
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
```

## Test Metrics

### Current Coverage
- **Doctor Service:** 9 tests covering all CRUD operations
- **Patient Service:** 8 tests covering all CRUD operations
- **Appointment Service:** 7 tests covering orchestration logic
- **Billing Service:** 11 tests covering billing logic
- **Total:** 35 unit tests

### Goals
- Maintain 80%+ code coverage
- All critical paths tested
- All error cases handled
- All API endpoints tested

## Next Steps

### Recommended Additions
1. **Integration Tests:** Test services together
2. **Performance Tests:** Load testing with JMeter
3. **Security Tests:** Authentication/Authorization testing
4. **Contract Tests:** API contract verification

### Example Integration Test
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AppointmentIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testBookAppointmentEndToEnd() {
        // Create patient
        Patient patient = restTemplate.postForObject(
            "/patients", patientRequest, Patient.class);
        
        // Create doctor
        Doctor doctor = restTemplate.postForObject(
            "/doctors", doctorRequest, Doctor.class);
        
        // Book appointment
        Appointment appointment = restTemplate.postForObject(
            "/appointments", appointmentRequest, Appointment.class);
        
        assertNotNull(appointment.getId());
    }
}
```

## Resources

### Documentation
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

### Commands Quick Reference
```bash
# Run all tests
mvnw test

# Run specific test class
mvnw test -Dtest=ClassName

# Run specific test method
mvnw test -Dtest=ClassName#methodName

# Run tests with verbose output
mvnw test -X

# Clean and test
mvnw clean test

# Package without tests
mvnw package -DskipTests
```

---

## Summary
You now have a comprehensive JUnit test suite covering:
- ✅ Controller layer (HTTP/REST API)
- ✅ Service layer (Business logic)
- ✅ Orchestration logic (Inter-service communication)
- ✅ Error handling and edge cases
- ✅ Data validation

**Total: 35 unit tests across 4 microservices**

Run `mvnw test` in each service directory to execute the tests!
