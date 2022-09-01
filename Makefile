APP_VERSION := v1.0.0
DOCKER_TAG := dapr.io/daprjavagrpc:$(APP_VERSION)

package:
	mvn -DskipTests package

clean:
	mvn clean

build:
	mvn clean install -DskipTests
	docker build . -t $(DOCKER_TAG)

push:
	docker push $(DOCKER_TAG)

deploy:
	make build
	make push

run:
	mvn clean spring-boot:run

run-dapr:
	dapr run -a daprjavagrpc -p 8089 -P grpc -- mvn clean spring-boot:run