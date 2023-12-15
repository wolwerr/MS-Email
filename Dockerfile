# Use a base image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /usr/app

# Copy the jar file to the working directory
COPY target/email-0.0.1-SNAPSHOT.jar app.jar

# Define the command to start your app
CMD ["java", "-jar", "app.jar"]