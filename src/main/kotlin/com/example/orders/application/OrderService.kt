package com.example.orders.application

import com.example.orders.api.OrderRequest
import com.example.orders.domain.Order
import com.example.orders.domain.OrderItem
import com.example.orders.domain.OrderRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderService(
    private val repository: OrderRepository
) {

    fun createOrder(req: OrderRequest): UUID {
        val order = Order(
            terminal = req.terminal,
            paymentType = req.paymentType,
            items = req.items.map { OrderItem(it.quantity, it.unitPrice) }
        )
        
        repository.save(order)
        
        return order.id
    }
}