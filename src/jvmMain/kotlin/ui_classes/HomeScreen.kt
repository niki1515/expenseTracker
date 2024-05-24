package ui_classes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data_models.Expense
import data_models.Income
import data_models.TransactionHistory
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

class HomeScreen(
    private val onNavigateToAddIncome: () -> Unit,
    private val onNavigateToAddExpense: () -> Unit,
    private val transactionHistory: TransactionHistory
) {

    @Composable
    fun Content() {
        Box(
            modifier = Modifier.size(width = 320.dp, height = 550.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Greeting()
                currentBalance()
                recentTransactions()
                Spacer(Modifier.weight(1f))
                navigationButtons()
            }
        }
    }

    @Composable
    private fun Greeting() {
        Text(
            "Hi Niki!",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 50.dp)
        )
    }

    @Composable
    private fun currentBalance() {
        val balance = transactionHistory.getBalance()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
                .clip(RoundedCornerShape(8.dp))
                .height(150.dp)
                .background(Color(0xFF4B0082))
        ) {
            Text(
                "${formatCurrency(balance)} Ft",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(top = 40.dp, start = 30.dp)
            )
        }
    }

    @Composable
    private fun recentTransactions() {
        val latestTransactions = transactionHistory.getLatestTransactions()
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {
            Text(
                "Recent Transactions:",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            latestTransactions.forEach { transaction ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (transaction) {
                            is Income -> "+ ${formatCurrency(transaction.amount)} Ft"
                            is Expense -> "- ${formatCurrency(transaction.amount)} Ft"
                            else -> "Unknown"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = (transaction as? Expense)?.description ?: (transaction as? Income)?.description ?: "No description",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        return "%,d".format(amount.toInt()).replace(',', ' ')
    }

    @Composable
    private fun navigationButtons() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onNavigateToAddIncome,
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            ) {
                Text("Add Money")
            }
            Button(
                onClick = onNavigateToAddExpense,
                modifier = Modifier.weight(1f)
            ) {
                Text("Expenses")
            }
        }
    }
}