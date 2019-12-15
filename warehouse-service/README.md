# Warehouse service

## Endpoints

* GET /api/v1/\{itemId} / 200 OK<br>Get item info.
* POST /api/v1 / 200 OK<br>Take one item (find in storage in decrease available count).
* DELETE /api/v1/\{itemId} / 204 No content<br>Remove orderItem and increase item available count.
* POST /api/v1/\{itemId}/warranty / 204 No content<br>Check item exists and send warranty request to Warranty service. _Write request to Warranty service._ 

## Exceptions

* 400 Bad Request - request serialization failed (MethodArgumentNotValidException).
* 404 Not Found – item with id not found (EntityNotFoundException). 
* 409 Conflict – request to Warranty service failed (WarrantyProcessException) or items not available (available count < 0) (ItemNotAvailableException).
* 500 Internal Server Error – everything fails (Exception).

## Configuration

* Register itself on Eureka with name `warehouse-service` (in [bootstrap.yml](/src/main/resources/bootstrap.properties)).
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
available_count: 5
name: Lego 8070
size: M
#2:
available_count: 5
name: Lego 8880
size: L
#3:
available_count: 5
name: Lego 42070
size: L
```