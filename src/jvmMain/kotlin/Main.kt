import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import data_models.TransactionHistory
import ui_classes.*

enum class CurrentScreen {
    Home, AddIncome, AddExpense
}

@Composable
fun App(currentScreen: MutableState<CurrentScreen>, transactionHistory: TransactionHistory) {
    when (currentScreen.value) {
        CurrentScreen.Home -> HomeScreen(
            onNavigateToAddIncome = {
                println("Navigating to Add Income Screen")
                currentScreen.value = CurrentScreen.AddIncome
            },
            onNavigateToAddExpense = {
                println("Navigating to Add Expense Screen")
                currentScreen.value = CurrentScreen.AddExpense
            },
            transactionHistory = transactionHistory
        ).Content()
        CurrentScreen.AddIncome -> AddIncomeScreen(
            onNavigateHome = {
                println("Navigating to Home Screen from Add Income")
                currentScreen.value = CurrentScreen.Home
            },
            transactionHistory = transactionHistory
        ).Content()
        CurrentScreen.AddExpense -> AddExpenseScreen(
            onNavigateHome = {
                println("Navigating to Home Screen from Add Expense")
                currentScreen.value = CurrentScreen.Home
            },
            transactionHistory = transactionHistory
        ).Content()
    }
}

fun main() = application {
    val currentScreen = remember { mutableStateOf(CurrentScreen.Home) }
    val transactionHistory = TransactionHistory()

    Window(
        title = "",
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 320.dp, height = 550.dp)
    ) {
        App(currentScreen, transactionHistory)
    }
}
