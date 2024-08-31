package com.receipt.processor.controller

import com.receipt.processor.domain.model.Receipt
import com.receipt.processor.service.ReceiptService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/receipts")
class ReceiptController(private val receiptService: ReceiptService) {

    @PostMapping("/process")
    fun processReceipt(@RequestBody receipt: Receipt): ResponseEntity<Map<String, String>> {
        val id = receiptService.processReceipt(receipt)
        return ResponseEntity.ok(mapOf("id" to id))
    }

    @GetMapping("/{id}/points")
    fun getPoints(@PathVariable id: String): ResponseEntity<Map<String, Int?>> {
        val points = receiptService.getPoints(id)
        return if (points != null) {
            ResponseEntity.ok(mapOf("points" to points))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
