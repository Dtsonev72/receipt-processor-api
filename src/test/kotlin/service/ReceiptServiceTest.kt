package service

import com.receipt.processor.domain.PointsCalculator
import com.receipt.processor.domain.model.Receipt
import com.receipt.processor.service.ReceiptService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ReceiptServiceTest {

    private lateinit var pointsCalculator: PointsCalculator
    private lateinit var receiptService: ReceiptService

    // Initialize the service with the mocked PointsCalculator
    @BeforeEach
    fun setup() {
        pointsCalculator = mockk()
        receiptService = ReceiptService(pointsCalculator)
    }

    @Test
    fun `should return null when points for a non-existent receipt ID are requested`() {
        val points = receiptService.getPoints(UUID.randomUUID().toString())
        assertEquals(null, points)
    }

    @Test
    fun `should return points for existing ID`() {
        val receipt = mockk<Receipt>()
        val expectedPoints = 42

        every { pointsCalculator.calculatePoints(receipt) } returns expectedPoints

        val receiptId = receiptService.processReceipt(receipt)

        val actualPoints = receiptService.getPoints(receiptId)
        assertEquals(expectedPoints, actualPoints)

        // Verify that calculatePoints was called with the mock receipt
        verify { pointsCalculator.calculatePoints(receipt) }
    }
}
