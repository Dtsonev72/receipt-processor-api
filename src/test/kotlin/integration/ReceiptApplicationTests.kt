package integration

import com.receipt.processor.ReceiptApplication
import com.receipt.processor.domain.PointsCalculator
import com.receipt.processor.domain.model.Item
import com.receipt.processor.domain.model.Receipt
import com.receipt.processor.service.ReceiptService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ReceiptApplication::class])
class ReceiptApplicationTests {

    @Autowired
    private lateinit var pointsCalculator: PointsCalculator

    @Autowired
    private lateinit var receiptService: ReceiptService

    @BeforeEach
    fun setup() {
        receiptService = ReceiptService(pointsCalculator)
    }

    @Test
    fun `should return the correct points based on the mock data`() {
        val receipt = Receipt(
            retailer = "Target",
            purchaseDate = "2022-01-01",
            purchaseTime = "13:01",
            items = listOf(
                Item("Mountain Dew 12PK", "6.49"),
                Item("Emils Cheese Pizza", "12.25"),
                Item("Knorr Creamy Chicken", "1.26"),
                Item("Doritos Nacho Cheese", "3.35"),
                Item("Klarbrunn 12-PK 12 FL OZ", "12.00")
            ),
            total = "35.35"
        )

        val id = receiptService.processReceipt(receipt)
        val points = receiptService.getPoints(id)

        assertEquals(28, points)
    }
}
