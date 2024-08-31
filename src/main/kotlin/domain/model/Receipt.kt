package com.receipt.processor.domain.model

data class Receipt(
    val retailer: String,
    val purchaseDate: String,
    val purchaseTime: String,
    val items: List<Item>,
    val total: String
)
