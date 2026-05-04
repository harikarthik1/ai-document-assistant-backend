FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# ✅ FIX: give permission to mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/ai-doc-assistant.jar"]