package data_models
import java.io.File

class TransactionHistory {
    private val transactions = mutableListOf<Any>()
    private val file = File("transactions.txt")

    init {
        loadTransactions()
    }

    private fun saveTransactions() {
        file.printWriter().use { out ->
            transactions.forEach {
                when (it) {
                    is Expense -> out.println("Expense,${it.amount},${it.category},${it.description}")
                    is Income -> out.println("Income,${it.amount},${it.category},${it.description}")
                }
            }
        }
    }

    private fun loadTransactions() {
        if (!file.exists()) return
        file.forEachLine {
            val parts = it.split(',')
            when (parts[0]) {
                "Expense" -> transactions.add(Expense(parts[1].toDouble(), parts[2], parts[3]))
                "Income" -> transactions.add(Income(parts[1].toDouble(), parts[2], parts[3]))
            }
        }
    }

    fun addTransaction(transaction: Any) {
        transactions.add(transaction)
        saveTransactions()
    }

    fun getLatestTransactions(limit: Int = 5): List<Any> {
        return transactions.takeLast(limit).reversed()
    }

    fun getTotalIncome(): Double = transactions.filterIsInstance<Income>().sumOf { it.amount }

    fun getBalance(): Double = getTotalIncome() - getTotalExpenses()

    fun getCategoryExpenses(): Map<String, Double> {
        return transactions.filterIsInstance<Expense>()
            .groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
    }

    fun getTotalExpenses(): Double {
        return getCategoryExpenses().values.sum()
    }

    fun getCategoryIncomes(): Map<String, Double> {
        return transactions.filterIsInstance<Income>()
            .groupBy { it.category }
            .mapValues { (_, incomes) -> incomes.sumOf { it.amount } }
    }

    fun getTotalIncomes(): Double = getCategoryIncomes().values.sum()

}
