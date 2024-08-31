package com.receipt.processor.service

import com.receipt.processor.domain.model.Receipt
import com.receipt.processor.domain.PointsCalculator
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReceiptService (private val pointsCalculator: PointsCalculator) {
    private val receipts = mutableMapOf<String, Int>()

    fun processReceipt(receipt: Receipt): String {
        val id = UUID.randomUUID().toString()
        val points = pointsCalculator.calculatePoints(receipt)
        receipts[id] = points
        return id
    }

    fun getPoints(id: String): Int? {
        return receipts[id]
    }
}
