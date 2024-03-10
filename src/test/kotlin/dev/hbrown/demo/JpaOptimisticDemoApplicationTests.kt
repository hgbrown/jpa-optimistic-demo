package dev.hbrown.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(TestJpaOptimisticDemoApplication::class)
class JpaOptimisticDemoApplicationTests {

    @Test
    fun contextLoads() {
    }

}
