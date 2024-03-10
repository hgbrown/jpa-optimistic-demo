package dev.hbrown.demo.customer

import jakarta.persistence.*

@Entity
@Table(name = "customer")
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    var name: String,

    @Version
    var version: Long = 0,
) {
    override fun toString(): String {
        return "Customer(id=$id, name='$name', version=$version)"
    }

}
