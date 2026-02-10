package com.example.orders.api

import com.example.orders.application.OrderService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@Validated
@RestController
@RequestMapping("/orders")
class OrderController(
    private val service: OrderService
) {

    @PostMapping
    fun create(@Valid @RequestBody req: OrderRequest): ResponseEntity<OrderResponse> {
        val id = service.createOrder(req)
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse(id))
    }
}

data class OrderRequest(
    @field:NotBlank(message = "Terminal é obrigatório")
    @field:Pattern(
        regexp = "^\\d{8}-\\d{1}$",
        message = "Terminal deve estar no formato 12345678-9"
    )
    val terminal: String,

    @field:NotBlank(message = "PaymentType é obrigatório")
    val paymentType: String,

    @field:NotNull(message = "Items não pode ser nulo")
    val items: List<OrderItemReq>
)
data class OrderItemReq(
    @field:Min(1) val quantity: Int,
    @field:NotNull val unitPrice: BigDecimal
)

data class OrderResponse(val id: UUID)