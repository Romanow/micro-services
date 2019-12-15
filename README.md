# Micro services example

## Project structure

1. [Store service](/store-service) 
2. [Order service](/order-service) 
3. [Warehouse service](/warehouse-service) 
4. [Warranty service](/warranty-service)
5. [Eureka](/eureka)
 
## Infrastructure

Using:
* Eureka for service discovery.
* Grafana and Prometheus for collecting metrics.
* PostgreSQL.
* ELK for logging.
* Turbine for monitoring Hystrix.
* Spring Sleuth for distributed tracing (using Zipkin protocol and Jaeger to collect data).

### Portainer for monitoring docker images
```bash
docker run -d \
  -p 9000:9000 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v portainer_data:/data \
  --name portainer \
  portainer/portainer
```

## Stress testing
```bash
wrk -t12 -c150 -d60s --latency http://localhost:8090/api/v1/221f3d2e-da16-410f-8d24-3974e38e54ad/orders
```

## Local run

Add ips to `/etc/hosts`:
```
127.0.0.1	postgres
127.0.0.1	tracing
127.0.0.1	eureka-1
127.0.0.1	eureka-2
127.0.0.1	logstash
127.0.0.1	warehouse-service
127.0.0.1	warrany-service
127.0.0.1	order-service
```

Run base `docker-compose up -d`.