package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalanceData(
    val username: String,
    @Json(name = "full_name") val fullName: String? = null,
    val balance: Double
)

@JsonClass(generateAdapter = true)
data class BalancesResponse(
    val balances: Map<String, Double>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class SettlementData(
    @Json(name = "settlement_id") val settlementId: Int? = null,
    val from: String,
    val to: String,
    val amount: Double,
    val status: String? = null,
    val method: String? = null
)

@JsonClass(generateAdapter = true)
data class SettlementResponse(
    val settlements: List<SettlementData>? = null,
    val balances: Map<String, Double>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class CashSettlementRequest(
    @Json(name = "to_user") val toUser: String,
    val amount: Double
)

@JsonClass(generateAdapter = true)
data class UpiSettlementRequest(
    @Json(name = "to_user") val toUser: String,
    val amount: Double
)

@JsonClass(generateAdapter = true)
data class SettlementActionResponse(
    val success: Boolean? = null,
    val message: String? = null,
    @Json(name = "settlement_id") val settlementId: Int? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class TransactionData(
    @Json(name = "transaction_id") val transactionId: String? = null,
    @Json(name = "payer_id") val fromUser: String,
    @Json(name = "payee_id") val toUser: String,
    val amount: Double,
    @Json(name = "payment_method") val paymentMethod: String? = null,
    @Json(name = "group_id") val groupId: Int? = null,
    @Json(name = "group_name") val groupName: String? = null,
    val timestamp: String? = null,
    @Json(name = "current_hash") val txHash: String? = null,
    @Json(name = "previous_hash") val previousHash: String? = null,
    @Json(name = "payer_name") val payerName: String? = null,
    @Json(name = "payee_name") val payeeName: String? = null
)

@JsonClass(generateAdapter = true)
data class TransactionsResponse(
    val transactions: List<TransactionData>? = null,
    val error: String? = null
)
