package data_models

import java.util.*

data class Income(
    val amount: Double,
    val category: String,
    val description: String?,
    val id: String = UUID.randomUUID().toString(),
)
