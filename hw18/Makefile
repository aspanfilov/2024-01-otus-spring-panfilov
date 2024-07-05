IMAGE_NAME=hw18-library

stop:
	docker-compose down
	docker rmi $(IMAGE_NAME)

start:
	cd .. && mvn -pl hw18 clean install -DskipTests
	docker-compose up --build -d

fullstart:
	cd .. && mvn -pl hw18 clean install
	docker-compose up --build -d