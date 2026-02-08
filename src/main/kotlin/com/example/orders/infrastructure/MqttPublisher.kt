package com.example.orders.infrastructure

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class MqttPublisher(
    @Value("\${mqtt.brokerUrl}") private val brokerUrl: String,
    @Value("\${mqtt.clientId:api-orders}") private val clientIdPrefix: String,
    @Value("\${mqtt.username:}") private val mqttUserName: String,
    @Value("\${mqtt.password:}") private val mqttPassword: String
) {

    private val log = LoggerFactory.getLogger(MqttPublisher::class.java)
    private lateinit var client: MqttClient

    @PostConstruct
    fun init() {
        val clientId = "$clientIdPrefix-${UUID.randomUUID()}"
        client = MqttClient(brokerUrl, clientId, MemoryPersistence())

        val opts = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
            if (mqttUserName.isNotBlank()) this.userName = mqttUserName
            if (mqttPassword.isNotBlank()) this.password = mqttPassword.toCharArray()
            connectionTimeout = 10
            keepAliveInterval = 20
        }

        log.info("Conectando ao broker MQTT em $brokerUrl ...")
        client.connect(opts)
        log.info("Conectado ao MQTT.")
    }

    fun publish(topic: String, payload: String, qos: Int = 1, retained: Boolean = false) {
        if (!client.isConnected) {
            log.warn("Cliente MQTT desconectado. Tentando reconectar...")
            client.reconnect()
        }
        val msg = MqttMessage(payload.toByteArray(StandardCharsets.UTF_8)).apply {
            this.qos = qos
            this.isRetained = retained
        }
        client.publish(topic, msg)
        log.info("Publicado no tópico=$topic payload=${payload.take(120)}...")
    }

    @PreDestroy
    fun close() {
        try {
            if (client.isConnected) client.disconnect()
            client.close()
        } catch (ex: Exception) {
            log.warn("Erro ao fechar MQTT: ${ex.message}")
        }
    }
}