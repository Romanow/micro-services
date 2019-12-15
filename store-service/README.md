# Store service

## Endpoints

* GET /api/v1/\{userId}/orders <br>Get all user orders info. _Read request to Order, WH and Warranty services._
* GET /api/v1/\{userId}/{orderId}<br>Get user order info. _Read request to Order, WH and Warranty services._
* /api/v1/\{userId}/purchase<br>Create new order. _Write request to Order service._
* /api/v1/\{userId}/{orderId}/refund<br>Refund created order. _Write request to Order service._
* /api/v1/\{userId}/{orderId}/warranty<br>

## Exceptions

* 400 Bad Request - request serialization failed (MethodArgumentNotValidException).
* 404 Not Found – item with id not found (EntityNotFoundException).
* 409 Conflict – request to Warranty service failed (WarrantyProcessException) or request to Order service failed (OrderProcessException).  
* 500 Internal Server Error – everything fails (Exception).

## Configuration

* Register itself on Eureka with name `store-service` (in [bootstrap.yml](/src/main/resources/bootstrap.properties)).
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
name: Ronin
uid: 221f3d2e-da16-410f-8d24-3974e38e54ad
```
