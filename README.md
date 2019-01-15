# Common

docker run -d -p 9000:9000 --name portainer --restart always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer

# Stress testing

wrk -t12 -c150 -d60s --latency http://localhost:8090/api/221f3d2e-da16-410f-8d24-3974e38e54ad/orders