plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "6.0.1.5171"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

sonar {
    properties {
        property("sonar.projectKey", "adpro-eshop")
        property("sonar.projectName", "adpro-eshop")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"
val junitJupiterVersion = "5.9.1"

dependencies {
    // Implementation dependencies
    implementation(
        "org.springframework.boot:spring-boot-starter-thymeleaf",
        "org.springframework.boot:spring-boot-starter-web"
    )

    // Compile-only dependencies
    compileOnly("org.projectlombok:lombok")

    // Development-only dependencies
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Annotation processor dependencies
    annotationProcessor(
        "org.springframework.boot:spring-boot-configuration-processor",
        "org.projectlombok:lombok"
    )

    // Test implementation dependencies
    testImplementation(
        "org.springframework.boot:spring-boot-starter-test",
        "org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion",
        "io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion",
        "io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion",
        "org.junit.jupiter:junit-jupiter:$junitJupiterVersion"
    )

    // Test runtime-only dependencies
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register<Test>("unitTest") {
    description = "Runs unit tests."
    group = "verification"

    filter {
        excludeTestsMatching("*FunctionalTest")
    }
}

tasks.register<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    filter {
        includeTestsMatching("*FunctionalTest")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.test {
    filter {
        excludeTestsMatching("*FunctionalTest")
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}