package dev.hbrown.demo.customer

import org.springframework.data.jpa.repository.JpaRepository
interface CustomerRepository : JpaRepository<Customer, Long>
