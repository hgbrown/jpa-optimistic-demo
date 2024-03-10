package dev.hbrown.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaOptimisticDemoApplication

fun main(args: Array<String>) {
    runApplication<JpaOptimisticDemoApplication>(*args)
}
