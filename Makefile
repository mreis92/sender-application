.PHONY=build run test

build:
	mvn clean package

run:
	java -Xmx1G -jar -Dspring.profiles.active=default ./target/*.jar $(SENDER) $(PORT)

test:
	mvn verify -B
