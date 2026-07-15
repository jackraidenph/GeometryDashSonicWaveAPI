plugins {
    id("java")
    id("maven-publish")
    application
}

group = "dev.jackraidenph"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.jackraidenph.Main")
}

tasks.named<Jar>("jar") {
    manifest.attributes["Main-Class"] = application.mainClass
}

tasks.register<Jar>("publishjar") {
    description = "Create a JAR artifact for Maven publishing"
    excludes.add(application.mainClass.get())
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.jackraidenph"
            artifactId = "gd-sonicwave-api"
            version = "1.0.0"

            artifact(tasks.named("publishjar")) {
                classifier = ""
            }
        }
    }
}

tasks.withType<PublishToMavenLocal> {
    doLast {
        val artifact = publication.groupId + ":" + publication.artifactId + ":" + publication.version;
        println("Published artifact [%s] to Maven Local".format(artifact))
    }
}