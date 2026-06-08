# Kubernetes deployment

Manifests to run fittrack-api on a local cluster ([kind](https://kind.sigs.k8s.io/)
or [minikube](https://minikube.sigs.k8s.io/)). All resources live in the `fittrack`
namespace.

| File | Resources |
|------|-----------|
| `00-namespace.yaml` | Namespace |
| `10-postgres.yaml`  | Secret, PVC, Deployment, Service (PostgreSQL) |
| `20-app.yaml`       | ConfigMap, Deployment (2 replicas, liveness/readiness probes), Service |
| `30-ingress.yaml`   | Ingress (`fittrack.local`) |

## Deploy with kind

```bash
# 1. Create a cluster
kind create cluster --name fittrack

# 2. Build the image and load it into the cluster (no registry needed)
docker build -t fittrack-api:latest ..
kind load docker-image fittrack-api:latest --name fittrack

# 3. Apply everything (order is encoded in the file name prefixes)
kubectl apply -f .

# 4. Watch it come up
kubectl -n fittrack get pods -w
```

The app `Deployment` uses Spring Boot Actuator's dedicated probes
(`/actuator/health/liveness`, `/actuator/health/readiness`), so Kubernetes only
routes traffic once Flyway has migrated and the datasource is reachable.

## Smoke test

```bash
kubectl -n fittrack port-forward svc/fittrack-api 8080:80
curl localhost:8080/api/exercises
```

## Validate the manifests without a cluster

```bash
docker run --rm -v "$PWD":/m ghcr.io/yannh/kubeconform:latest -strict -summary /m
```

## Notes / limitations

- The DB password lives in a plain `Secret` for demo simplicity. In a real
  cluster it would come from a sealed secret or an external secret store.
- Single PostgreSQL replica with a `Recreate` strategy — fine for a demo, not HA.
- The Prometheus pod annotations on the app assume a Prometheus configured with
  the `kubernetes_sd` pod role; the `monitoring/` stack covers the Compose case.
