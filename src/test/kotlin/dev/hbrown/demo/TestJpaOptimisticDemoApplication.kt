package dev.hbrown.demo

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestJpaOptimisticDemoApplication {
    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("db-jpa-optimistic-demo")
            .withUsername("dbuser")
            .withPassword("s3ckr!t")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("schema.sql"),
                "/docker-entrypoint-initdb.d/schema.sql"
            )
            .also {
                it.setPortBindings(listOf("15442:5432"))
            }
    }

}

fun main(args: Array<String>) {
    fromApplication<JpaOptimisticDemoApplication>().with(TestJpaOptimisticDemoApplication::class).run(*args)
}
