# Warranty service

## Endpoints

* GET /api/v1/\{itemId} / 200 OK<br>Get warranty info.
* POST /api/v1/\{itemId} / 201 Created<br>Start warranty.
* DELETE /api/v1/\{itemId} / 204 No content<br>Remove item from warranty.
* POST /api/v1/\{itemId}/warranty / 200 OK<br>Get item and update it's warranty status.
If item on warranty and warranty is active, then check item count, else refuse.
If it's more than zero, than return, else fixing.

## Exceptions

* 400 Bad Request - request serialization failed (MethodArgumentNotValidException).
* 404 Not Found – item with id not found (EntityNotFoundException).
* 500 Internal Server Error – everything fails (Exception).

## Configuration

* Register itself on Eureka with name `warranty-service` (in [bootstrap.yml](/src/main/resources/bootstrap.properties)).
* Send logs to ELK (custom [logback-spring.xml](/src/main/resources/logback-spring.xml))
* Expose metrics for Prometheus.
* Secure /manage endpoint with `manager / test` credentials.
* Send every trace statistics to Zipkin (`spring.sleuth.sampler.probability=1.0`).
* Swagger api doc `/api-doc`.

## Database
### Structure

```sql
```

### Initial data
DB initialized with following items:
```
#1:
item_id: 9b4a2300-884d-4ba5-a2e5-ac624214f994
status: ON_WARRANTY
```