package com.example.orders.domain

interface OrderRepository {
    fun save(order: Order)
}