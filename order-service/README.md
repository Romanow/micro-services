# Order service

## Endpoints

* GET /api/v1/\{userId}/\{orderId} / 200 OK<br>Get order info for user.
* GET /api/v1/\{userId} / 200 OK<br>Get user orders info.
* POST /api/v1/\{userId} / 201 Created<br>Create order for user. Take item from Warehouse service and start warranty on Warranty service. _Write request to Warranty and Warehouse services._
* DELETE /api/v1/\{orderId} / 204 No content<br>Cancel order and cancel warranty on Warranty Service and return item on Warehouse service. _Write request to Warranty and Warehouse services._
* POST /api/v1/\{orderId}/warranty / 200 OK<br>Check available count on Warehouse service and change warranty status on Warranty service. _Read request to Warehouse services and write request to Warranty service._

## Exceptions

* 400 Bad Request - request serialization failed (MethodArgumentNotValidException).
* 404 Not Found – item with id not found (EntityNotFoundException).
* 409 Conflict – request to Warranty service failed (WarrantyProcessException), request to Warehouse service failed (WarehouseProcessingException) or can't create order (CreateOrderException).
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

Nothing to initialize.
