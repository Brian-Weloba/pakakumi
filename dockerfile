FROM eclipse-temurin:17

# Set working directory
WORKDIR /pakakumi

# Copy JAR file
COPY target/pakakumi-0.0.1-SNAPSHOT.jar .

# Expose port
EXPOSE 3003


CMD ["java", "-jar", "pakakumi-0.0.1-SNAPSHOT.jar"]