```
$ cat /etc/hosts
127.0.0.1       eureka-1
127.0.0.1       eureka-2
127.0.0.1       logstash
127.0.0.1       postgres
127.0.0.1       zipkin
127.0.0.1       postgres
```

docker run -d -p 9000:9000 --name portainer --restart always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer