package com.splitsmart.app.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExpenseData(
    @Json(name = "id") val expenseId: Int,
    val name: String,
    val amount: Double,
    @Json(name = "paid_by") val paidBy: String,
    @Json(name = "split_type") val splitType: String,
    val category: String? = null,
    @Json(name = "created_at") val createdAt: String? = null,
    val splits: List<ExpenseSplitData>? = null
)

@JsonClass(generateAdapter = true)
data class ExpenseSplitData(
    @Json(name = "user_id") val username: String,
    val amount: Double,
    val percentage: Double? = null
)

@JsonClass(generateAdapter = true)
data class ExpenseListResponse(
    val expenses: List<ExpenseData>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateExpenseRequest(
    val name: String,
    val amount: Double,
    @Json(name = "paid_by") val paidBy: String,
    @Json(name = "split_type") val splitType: String = "equal",
    val category: String? = null,
    val splits: List<SplitInput>? = null
)

@JsonClass(generateAdapter = true)
data class SplitInput(
    val username: String,
    val amount: Double? = null,
    val percentage: Double? = null
)

@JsonClass(generateAdapter = true)
data class CreateExpenseResponse(
    val success: Boolean? = null,
    @Json(name = "expense_id") val expenseId: Int? = null,
    val message: String? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class DeleteExpenseResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val error: String? = null
)
