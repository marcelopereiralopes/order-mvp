package com.example.orders.infrastructure

import com.example.orders.domain.Order
import com.example.orders.domain.OrderRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository

@Repository
class MqttOrderRepository(
    private val publisher: MqttPublisher,
    private val objectMapper: ObjectMapper
) : OrderRepository {

    override fun save(order: Order) {
        val json = objectMapper.writeValueAsString(order)
        // Mantendo o tópico original "orders/new"
        publisher.publish("orders/new", json, qos = 1)
    }
}