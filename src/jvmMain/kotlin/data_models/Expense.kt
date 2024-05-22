package data_models

import java.util.*

data class Expense(
    val amount: Double,
    val category: String,
    val description: String?,
    val id: String = UUID.randomUUID().toString(),
)
