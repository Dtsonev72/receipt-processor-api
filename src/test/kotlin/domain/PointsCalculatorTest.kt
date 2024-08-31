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
            retailer = "Retailer123!",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = emptyList(),
            total = "0.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 10 alphanumeric characters
        assertEquals(10, points)
    }

    @Test
    fun `should return 50 points if total is a round dollar amount`() {
        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = emptyList(),
            total = "100.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 50 points for round total + 8 for retailer name
        assertEquals(50 + 8, points)
    }

    @Test
    fun `should return 25 points if total is a multiple of 25 cents`() {
        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = emptyList(),
            total = "25.25"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 25 points for total multiple of 0.25 + 8 for retailer name
        assertEquals(25 + 8, points)
    }

    @Test
    fun `should return 5 points for every two items`() {
        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = listOf(
                Item("Item 1", "1.00"),
                Item("Item 2", "2.00"),
                Item("Item 3", "3.00"),
                Item("Item 4", "4.00")
            ),
            total = "10.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 10 points for 4 items (2 pairs of 2) + 8 for retailer name
        assertEquals(8 + 10, points)
    }

    @Test
    fun `should add points based on item description length being a multiple of 3`() {

        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = listOf(
                Item("Item123", "10.00") // Description length is 7 (not a multiple of 3)
            ),
            total = "10.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        assertEquals(8, points) // 8 for retailer name only
    }

    @Test
    fun `should return additional points if the purchase date is odd`() {
        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "10:00",
            items = emptyList(),
            total = "10.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 8 for retailer name + 6 for odd day
        assertEquals(8 + 6, points)
    }

    @Test
    fun `should return additional points if the purchase time is between 2-4 pm`() {
        val receipt = Receipt(
            retailer = "Retailer",
            purchaseDate = "2023-01-01",
            purchaseTime = "14:30", // Between 2:00 pm and 4:00 pm
            items = emptyList(),
            total = "10.00"
        )

        val points = pointsCalculator.calculatePoints(receipt)

        // 8 for retailer name + 10 for time between 2-4 pm
        assertEquals(8 + 10, points)
    }

    @Test
    fun `should correctly combine all rules`() {
        // Given
        val receipt = Receipt(
            retailer = "Retailer123!",
            purchaseDate = "2023-01-01", // Odd day
            purchaseTime = "14:30", // Between 2:00 pm and 4:00 pm
            items = listOf(
                Item("Item 1", "1.00"),
                Item("Item123", "12.00") // Description length is a multiple of 3
            ),
            total = "25.25" // Multiple of 0.25
        )
        val points = pointsCalculator.calculatePoints(receipt)

        // 10 for retailer name + 25 for total multiple of 0.25 + 10 for time
        // + 6 for odd day + 5 for 2 items + 3 for item description length multiple of 3
        assertEquals(10 + 25 + 10 + 6 + 5 + 3, points)
    }
}
