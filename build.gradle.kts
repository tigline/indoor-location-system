import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.lgguan.iot"
version = "1.0.6"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.register("saveVersion") {
    println("saveVersion: $version")
    doLast {
        File("version.txt").writeText(version.toString())
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.integration:spring-integration-mqtt:6.0.2")
    implementation("org.eclipse.paho:org.eclipse.paho.mqttv5.client:1.2.5")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.1")
    implementation("io.github.nefilim.kjwt:kjwt-core:0.5.1")
    implementation("io.arrow-kt:arrow-core-jvm:1.0.1")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("cn.hutool:hutool-all:5.8.11")
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.0.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.14")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("commons-io:commons-io:2.8.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName("jar") {
    enabled = false
}

//remotes {
//    webServer {
//        role 'appServer'
//        hostname 'your-server-hostname'
//        username 'your-ssh-username'
//        privateKey file('path/to/your/private/key')
//    }
//}
