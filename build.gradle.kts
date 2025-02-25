plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.kotlin.kapt") version "1.9.10"
}

group = "com.leksss.tarobot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

kapt {
    correctErrorTypes = true // Опционально, исправляет некоторые ошибки компиляции
}

dependencies {
    testImplementation(kotlin("test"))
    // Telegram bot api
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.6")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Dagger
    implementation("com.google.dagger:dagger:2.48")
    kapt("com.google.dagger:dagger-compiler:2.48")

    //DB Hibernate Core
    implementation("org.hibernate:hibernate-core:6.3.0.Final")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.3.1.Final") // Проверьте актуальную версию

    // JPA API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}