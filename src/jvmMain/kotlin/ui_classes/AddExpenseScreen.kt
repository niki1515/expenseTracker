package ui_classes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data_models.Expense
import data_models.TransactionHistory
import java.util.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


class AddExpenseScreen(
    private val onNavigateHome: () -> Unit,
    private val transactionHistory: TransactionHistory
) {
    private var expenseAmount by mutableStateOf("")
    private var expenseCategory by mutableStateOf("Select a Category")
    private var expenseDescription by mutableStateOf("")
    private var expanded by mutableStateOf(false)

    private val categories = listOf(
        "groceries", "entertainment", "payment", "transportation",
        "travels", "health", "restaurants & bars", "shopping", "other"
    )

    @Composable
    fun Content() {
        val expensesByCategory = transactionHistory.getCategoryExpenses()
        val totalExpenses = transactionHistory.getTotalExpenses()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Expenses",
                style = TextStyle(fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Amount") }
            )
            categoryDropdown()
            OutlinedTextField(
                value = expenseDescription,
                onValueChange = { expenseDescription = it },
                label = { Text("Description") }
            )
            Button(
                onClick = { addExpense() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add")
            }
            Text(
                "Total Expenses: ${formatCurrency(totalExpenses)} Ft",
                style = TextStyle(fontWeight = FontWeight.Medium)
            )
            pieChart(expensesByCategory, totalExpenses)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onNavigateHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Home")
            }
        }
    }

    @Composable
    fun pieChart(expensesByCategory: Map<String, Double>, totalExpenses: Double) {
        // Color mapping for each category
        val categoryColors = mapOf(
            "groceries" to Color(0xFFE53883),  // Pink
            "entertainment" to Color(0xFFC71585),  // Purple
            "payment" to Color(0xFF800080),  // Dark Purple
            "transportation" to Color(0xFF4B0082),  // Blue
            "travels" to Color(0xFF0000CD),  // Dodger Blue
            "health" to Color(0xFF1E90FF),  // Dark Turquoise
            "restaurants & bars" to Color(0xFF00CED1),  // Medium Aquamarine
            "shopping" to Color(0xFF66CDAA),  // Light Green
            "other" to Color(0xFF90EE90)  // White Smoke for others
        )

        Canvas(modifier = Modifier.size(150.dp)) {
            if (totalExpenses == 0.0) return@Canvas
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2
            var startAngle = 0f

            expensesByCategory.entries.forEachIndexed { index, entry ->
                val color = categoryColors[entry.key] ?: Color.Gray // Use Gray as default if no color is specified for the category
                val sweepAngle = (entry.value / totalExpenses).toFloat() * 360f
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
                startAngle += sweepAngle
            }
        }
    }

    @Composable
    fun categoryDropdown() {
        // Color mapping for each category
        val categoryColors = mapOf(
            "groceries" to Color(0xFFE53883),  // Pink
            "entertainment" to Color(0xFFC71585),  // Purple
            "payment" to Color(0xFF800080),  // Dark Purple
            "transportation" to Color(0xFF4B0082),  // Blue
            "travels" to Color(0xFF0000CD),  // Dodger Blue
            "health" to Color(0xFF1E90FF),  // Dark Turquoise
            "restaurants & bars" to Color(0xFF00CED1),  // Medium Aquamarine
            "shopping" to Color(0xFF66CDAA),  // Light Green
            "other" to Color(0xFF90EE90)  // White Smoke for others
        )

        OutlinedTextField(
            value = expenseCategory,
            onValueChange = { },
            label = { Text("Category") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.ArrowDropDown, "drop down")
                }
            },
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    expenseCategory = category
                    expanded = false
                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(categoryColors[category] ?: Color.Transparent)  // Fallback to transparent if no color is mapped
                    ) {
                        Text(
                            text = category,
                            color = if (categoryColors[category] == Color(0xFFF5F5F5)) Color.Black else Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun addExpense() {
        val amount = expenseAmount.toDoubleOrNull()
        if (amount != null && expenseCategory != "Select a Category") {
            val newExpense = Expense(amount, expenseCategory, expenseDescription, UUID.randomUUID().toString())
            transactionHistory.addTransaction(newExpense)
            println("Added expense: $newExpense")
            clearFields()
        } else {
            println("Invalid amount or category not selected.")
        }
    }

    private fun clearFields() {
        expenseAmount = ""
        expenseCategory = "Select a Category"
        expenseDescription = ""
    }

    private fun formatCurrency(amount: Double): String {
        return "%,d".format(amount.toInt()).replace(',', ' ')
    }
}