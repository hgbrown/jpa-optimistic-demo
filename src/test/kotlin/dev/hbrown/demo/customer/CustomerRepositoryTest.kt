package dev.hbrown.demo.customer

import dev.hbrown.demo.TestJpaOptimisticDemoApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.test.context.TestConstructor
import java.util.concurrent.Executors

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(TestJpaOptimisticDemoApplication::class)
class CustomerRepositoryTest @Autowired constructor(
    val customerRepository: CustomerRepository,
) {
    @Test
    fun `test optimistic locking with concurrent updates`() {
        val dispatcher1 = Executors.newSingleThreadExecutor()
        val dispatcher2 = Executors.newSingleThreadExecutor()

        val customer = customerRepository.save(Customer(name = "John Doe"))
        val customerId = customer.id!!

        dispatcher1.execute {
            val c1 = customerRepository.findById(customerId).get()
            println("*** 1. Read customer in thread: ${Thread.currentThread().name}: $c1")

            c1.name = "Jane Doe"
            customerRepository.saveAndFlush(c1)
            println("*** 1. Completed save of c1 in thread: ${Thread.currentThread().name}")
            Thread.sleep(1_000)
            println("*** 1. Done in thread: ${Thread.currentThread().name}")
        }

        dispatcher2.execute {
            val c2 = customerRepository.findById(customerId).get()
            println("*** 2. Read customer in thread: ${Thread.currentThread().name}: $c2")
            c2.name = "Jake Doe"
            Thread.sleep(1_000) // give first thread chance to complete
            val e = assertThrows<OptimisticLockingFailureException> {
                println("*** 2. About to attempt save in thread: ${Thread.currentThread().name}: $c2")
                customerRepository.saveAndFlush(c2)
            }
            println("*** 2. Caught exception in thread: ${Thread.currentThread().name}: $e")
            println("*** 2. Done in thread: ${Thread.currentThread().name}")
        }

        dispatcher1.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)
        dispatcher2.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)
        println("Final value for customer is: ${customerRepository.findById(customerId).get()}")
    }
}
