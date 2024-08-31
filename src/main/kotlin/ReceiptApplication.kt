package com.receipt.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReceiptProcessor

fun main(args: Array<String>) {
    runApplication<ReceiptProcessor>(*args)
}
