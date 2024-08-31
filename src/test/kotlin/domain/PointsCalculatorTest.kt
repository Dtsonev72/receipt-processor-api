package domain

import com.receipt.processor.domain.PointsCalculator
import com.receipt.processor.domain.model.Item
import com.receipt.processor.domain.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PointsCalculatorTest {

    private val pointsCalculator = PointsCalculator()

    @Test
    fun `should return points based on alphanumeric characters in retailer name`() {
        val receipt = Receipt(
            retailer = "Retailer123!", // 11 points
            purchaseDate = "2023-01-01", // 6 points
            purchaseTime = "10:00",
            items = emptyList(),
            total = "0.00" // 75 points
        )

        val points = pointsCalculator.calculatePoints(receipt)

        assertEquals(11 + 6 + 75, points)
    }

    @Test
    fun `should return 5 points for every two items`() {
        val receipt = Receipt(
            retailer = "Retailer", // 8 points
            purchaseDate = "2023-02-14",
            purchaseTime = "14:50", // 10 points
            items = listOf(
                Item("Item 1", "1.00"), // 1 point
                Item("Item 2", "2.00"), // 1 point
                Item("Item 3", "3.00"), // 1 point
                Item("Item 4", "10.00") // 2 points
            ), // 10 points
            total = "10.00" // 75 points
        )

        val points = pointsCalculator.calculatePoints(receipt)

        assertEquals(8 + 10 + 1 + 1 + 1 + 2 + 10 + 75, points)
    }

    @Test
    fun `should correctly combine all rules`() {
        val receipt = Receipt(
            retailer = "Retailer123!", // 11 points
            purchaseDate = "2023-01-01", // 6 points
            purchaseTime = "14:30", // 10 points
            items = listOf(
                Item("Item 1", "1.00"), // 0.2 points ~ 1 point
                Item("Item123", "12.00")
            ), // 5 points
            total = "25.25" // 25 points
        )
        val points = pointsCalculator.calculatePoints(receipt)

        assertEquals(11 + 6 + 10 + 1 + 5 + 25, points)
    }

    @Test
    fun `should return the correct total points` () {
        val receipt = Receipt(
            retailer = "M&M Corner Market",
            purchaseDate = "2022-03-20",
            purchaseTime = "14:33",
            items = listOf(
                Item("Gatorade", "2.25"),
                Item("Gatorade", "2.25"),
                Item("Gatorade", "2.25"),
                Item("Gatorade", "2.25"),
                Item("Gatorade", "2.25"),
            ),
            total = "9.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // Total Points: 109
        // Breakdown:
        //    50 points - total is a round dollar amount
        //    25 points - total is a multiple of 0.25
        //    14 points - retailer name (M&M Corner Market) has 14 alphanumeric characters
        //                note: '&' is not alphanumeric
        //    10 points - 2:33pm is between 2:00pm and 4:00pm
        //    10 points - 4 items (2 pairs @ 5 points each)
        //  + ---------
        //  = 109 points

        assertEquals(109, points)
    }

    @Test
    fun `should return the multiple item scenario points` () {
        val receipt = Receipt(
            retailer = "Target",
            purchaseDate = "2022-01-01",
            purchaseTime = "13:01",
            items = listOf(
                Item("Mountain Dew 12PK", "6.49"),
                Item("Emils Cheese Pizza", "12.25"),
                Item("Knorr Creamy Chicken", "1.26"),
                Item("Doritos Nacho Cheese", "3.35"),
                Item("   Klarbrunn 12-PK 12 FL OZ  ", "12.00")
            ),
            total = "35.35"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        //        Total Points: 28
        //  Breakdown:
        //     6 points - retailer name has 6 characters
        //    10 points - 4 items (2 pairs @ 5 points each)
        //     3 Points - "Emils Cheese Pizza" is 18 characters (a multiple of 3)
        //                item price of 12.25 * 0.2 = 2.45, rounded up is 3 points
        //     3 Points - "Klarbrunn 12-PK 12 FL OZ" is 24 characters (a multiple of 3)
        //                item price of 12.00 * 0.2 = 2.4, rounded up is 3 points
        //     6 points - purchase day is odd
        //  + ---------
        //  = 28 points
        assertEquals(28, points)
    }
}
