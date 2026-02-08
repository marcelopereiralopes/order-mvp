package com.example.orders.domain

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class Order(
    val id: UUID = UUID.randomUUID(),
    val paymentType: String,
    val terminal: String,
    val items: List<OrderItem>,
    val createdAt: OffsetDateTime = OffsetDateTime.now()
) {
    // Aqui poderíamos ter métodos de negócio, ex: calcularTotal(), validar(), etc.
    fun total(): BigDecimal = items.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.total()) }
}

data class OrderItem(
    val quantity: Int,
    val unitPrice: BigDecimal
) {
    fun total(): BigDecimal = unitPrice.multiply(BigDecimal(quantity))
}