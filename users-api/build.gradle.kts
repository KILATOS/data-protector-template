plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.master-leonardo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"

dependencies {
    //logstash
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    //jwt
    implementation("com.auth0:java-jwt:4.4.0")
    //validation
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    //security
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.3")
    //h2
    implementation("com.h2database:h2:2.2.224")
    //for web
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    //for microservices
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    //testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //config
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.0")
    //MQ
    implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    //PostgreSQL
    implementation("org.postgresql:postgresql")




}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
