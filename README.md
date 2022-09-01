# Dapr Java gRPC Project Template

Java Template

## Add proto files

Copy your proto file to folder `./src/main/proto` and run before command to compile proto

```
make package
```

## Run with [Dapr CLI](https://docs.dapr.io/getting-started/install-dapr-cli/)

Run before command to run with dapr cli

```
make run-dapr
```

## Build or deploy Docker image

Update service version before docker build in file [Makefile](./Makefile) and run before command to:

Build docker image:
```
make build
```

Deploy docker image: 

```
make deploy
```