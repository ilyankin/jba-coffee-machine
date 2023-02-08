import java.math.BigDecimal

enum class CupType {
    COFFEE,
    MILK_COFFEE
}

sealed interface Cup

sealed class WaterCup(
    open val type: CupType,
    open val price: Money,
    open val water: Volume,
    open val disposableCups: DisposableCups = DisposableCups("disposable cups", 1u, UnitType.UNIT)
) : Cup

sealed class CoffeeCup(
    override val type: CupType,
    override val price: Money,
    override val water: Water,
    open val coffeeBeans: CoffeeBeans
) : WaterCup(type, price, water)

sealed class MilkCoffeeCup(
    override val type: CupType,
    override val price: Money,
    override val water: Water,
    override val coffeeBeans: CoffeeBeans,
    open val milk: Milk
) : CoffeeCup(type, price, water, coffeeBeans)

class Espresso : CoffeeCup(
    CupType.COFFEE,
    Dollar(BigDecimal(4)),
    Water("water", 250U, UnitType.ML),
    CoffeeBeans("coffee beans", 16U, UnitType.GM),
)

class Latter : MilkCoffeeCup(
    CupType.MILK_COFFEE,
    Dollar(BigDecimal(7)),
    Water("water", 350U, UnitType.ML),
    CoffeeBeans("coffee beans", 20U, UnitType.GM),
    Milk("milk", 75U, UnitType.ML)
)

class Cappuccino : MilkCoffeeCup(
    CupType.MILK_COFFEE,
    Dollar(BigDecimal(6)),
    Water("water", 200U, UnitType.ML),
    CoffeeBeans("coffee beans", 12U, UnitType.GM),
    Milk("milk", 100U, UnitType.ML)
)