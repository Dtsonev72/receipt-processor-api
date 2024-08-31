package com.receipt.processor.domain

import com.receipt.processor.domain.model.Receipt
import org.springframework.stereotype.Component
import kotlin.math.ceil

@Component
class PointsCalculator {
    // Calculating the points based on the imposed rules
    fun calculatePoints(receipt: Receipt): Int {
        var points = 0

        // One point for every alphanumeric character in the retailer name
        points += receipt.retailer.filter { it.isLetterOrDigit() }.length

        // 50 points if the total is a round dollar amount with no cents
        if (receipt.total.toDouble() % 1 == 0.0) {
            points += 50
        }

        // 25 points if the total is a multiple of 0.25
        if (receipt.total.toDouble() % 0.25 == 0.0) {
            points += 25
        }

        // 5 points for every two items on the receipt
        points += (receipt.items.size / 2) * 5

        // If item description length is a multiple of 3, add points based on price
        receipt.items.forEach { item ->
            val trimmedDescLength = item.shortDescription.trim().length
            if (trimmedDescLength % 3 == 0) {
                points += ceil(item.price.toDouble() * 0.2).toInt()
            }
        }

        // 6 points if the day in the purchase date is odd
        val day = receipt.purchaseDate.split("-").last().toInt()
        if (day % 2 != 0) {
            points += 6
        }

        // 10 points if the time is between 2:00pm and 4:00pm
        val time = receipt.purchaseTime.split(":")[0].toInt()
        if (time == 14 || time == 15 ) {
            points += 10
        }

        return points
    }
}
