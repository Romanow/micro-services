# Store service

## Endpoints

* /{userId}/orders <br>
get all user orders info. Read request to Order, WH and Warranty services.
* /{userId}/{orderId}<br>
get user order info. Read request to Order, WH and Warranty services.
* /{userId}/purchase<br>
Create new order. Write request to Order service.
* /{userId}/{orderId}/refund<br>
Refund created order. Write request to Order service.
* /{userId}/{orderId}/warranty<br>

## Configuration

* Register itself on Eureka with name `store-service` (in [bootstrap.yml](/src/main/resources/bootstrap.properties)).
* Send logs to ELK (custom [logback-spring.xml](/src/main/resources/logback-spring.xml))
* Expose metrics for Prometheus.
* Secure /manage endpoint with `manager / test` credentials.
* Send every trace statistics to Zipkin (`spring.sleuth.sampler.probability=1.0`).
* Swagger api doc `/api-doc`. 