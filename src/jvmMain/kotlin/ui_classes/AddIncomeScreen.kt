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
import data_models.Income
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

class AddIncomeScreen(
    private val onNavigateHome: () -> Unit,
    private val transactionHistory: TransactionHistory
) {
    private var incomeAmount by mutableStateOf("")
    private var incomeCategory by mutableStateOf("Select a Category")
    private var incomeDescription by mutableStateOf("")
    private var expanded by mutableStateOf(false)

    private val categories = listOf("salary", "pocketmoney", "scholarship", "investment", "other")

    @Composable
    fun Content() {
        val incomesByCategory = transactionHistory.getCategoryIncomes()
        val totalIncomes = transactionHistory.getTotalIncomes()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Add Money",
                style = TextStyle(fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = incomeAmount,
                onValueChange = { incomeAmount = it },
                label = { Text("Amount") }
            )
            categoryDropdown()
            OutlinedTextField(
                value = incomeDescription,
                onValueChange = { incomeDescription = it },
                label = { Text("Description") }
            )
            Button(
                onClick = { addIncome() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add")
            }
            Text(
                "Total Income: ${formatCurrency(totalIncomes)} Ft",
                style = TextStyle(fontWeight = FontWeight.Medium)
            )
            pieChart(incomesByCategory, totalIncomes)
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
    fun pieChart(incomesByCategory: Map<String, Double>, totalIncomes: Double) {
        // Define color mapping similar to the categoryDropdown
        val categoryColors = mapOf(
            "salary" to Color(0xFFC71585),  // Pink
            "pocketmoney" to Color(0xFF800080),  // Purple
            "scholarship" to Color(0xFF4B0082),  // Dark Purple
            "investment" to Color(0xFF0000CD),  // Blue
            "other" to Color(0xFF1E90FF)  // Light Blue
        )

        Canvas(modifier = Modifier.size(150.dp)) {
            if (totalIncomes == 0.0) return@Canvas
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2
            var startAngle = 0f

            incomesByCategory.entries.forEachIndexed { index, entry ->
                val color = categoryColors[entry.key] ?: Color.Gray // Default to Gray if color not found
                val sweepAngle = (entry.value / totalIncomes).toFloat() * 360f
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
        val categoryColors = mapOf(
            "salary" to Color(0xFFC71585),
            "pocketmoney" to Color(0xFF800080),
            "scholarship" to Color(0xFF4B0082),
            "investment" to Color(0xFF0000CD),
            "other" to Color(0xFF1E90FF)
        )

        OutlinedTextField(
            value = incomeCategory,
            onValueChange = { },
            label = { Text("Category") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
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
                    incomeCategory = category
                    expanded = false
                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(categoryColors[category] ?: Color.Transparent)
                    ) {
                        Text(
                            text = category,
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun addIncome() {
        val amount = incomeAmount.toDoubleOrNull()
        if (amount != null && incomeCategory != "Select a Category") {
            val newIncome = Income(amount, incomeCategory, incomeDescription, UUID.randomUUID().toString())
            transactionHistory.addTransaction(newIncome)
            println("Added income: $newIncome")
            clearFields()
        } else {
            println("Invalid amount or category not selected.")
        }
    }

    private fun clearFields() {
        incomeAmount = ""
        incomeCategory = "Select a Category"
        incomeDescription = ""
    }

    private fun formatCurrency(amount: Double): String {
        return "%,d".format(amount.toInt()).replace(',', ' ')
    }
}