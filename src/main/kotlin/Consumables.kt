import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

enum class UnitType(val value: String) {
    ML("ml"),
    GM("g"),
    UNIT("")
}

sealed class Volume(var name: String, var quantity: UInt, var unit: UnitType)

class Water(name: String, quantity: UInt, unit: UnitType) : Volume(name, quantity, unit)

class Milk(name: String, quantity: UInt, unit: UnitType) : Volume(name, quantity, unit)

class CoffeeBeans(name: String, quantity: UInt, unit: UnitType) : Volume(name, quantity, unit)

class DisposableCups(name: String, quantity: UInt, unit: UnitType) : Volume(name, quantity, unit)

sealed class Money(
    var amount: BigDecimal,
    val currency: Currency,
    val rounding: RoundingMode = RoundingMode.HALF_EVEN,
    val locale: Locale = Locale.getDefault()
) {

    private val amountFormat = NumberFormat.getCurrencyInstance(locale)

    init {
        amount.setScale(currency.defaultFractionDigits, rounding)
        amountFormat.currency = currency
    }

    fun getFormattedString(): String = amountFormat.format(amount)

    fun getFormattedString(fractDigits: Int): String {
        amountFormat.maximumFractionDigits = fractDigits
        return amountFormat.format(amount)
    }

    operator fun plus(money: Money): Money {
        amount += money.amount
        return this
    }

    operator fun minus(money: Money): Money {
        amount -= money.amount
        return this
    }

    operator fun times(money: Money): Money {
        amount *= money.amount
        return this
    }

    operator fun div(money: Money): Money {
        amount /= money.amount
        return this
    }

    operator fun rem(money: Money): Money {
        amount %= money.amount
        return this
    }

    override fun toString(): String {
        return "Price(amount=$amount, currency=$currency, rounding=$rounding)"
    }
}

class Dollar(amount: BigDecimal) : Money(amount, Currency.getInstance("USD"))