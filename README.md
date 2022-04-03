# Sender Application

The sender application is a Maven-based project using Spring Boot, that receives messages from the user input, and sends
the message through sockets to a receiver application.

# Dependencies

You will need Maven installed in your local machine to compile the application and run the tests.

Alternatively, you can run the application with the provided .jar file, and the following instruction (parameters can be
modified):

```
java -Xmx1G -jar sender-application.jar sender-id 6666
```

Note that a SocketServer should be running, in order for the SenderApplication to establish a connection.

# Setup

A makefile has been created to simplify the commands to be executed.

To compile:

```
make build
```

To run the application:

```
make run SENDER=sender-id PORT=6666
```

To run the tests:

```
make test
```