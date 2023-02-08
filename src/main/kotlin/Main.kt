import java.math.BigDecimal

fun main() {
    val coffeeMachine = CoffeeMachine(
        StoreVolumes(
            Water("water", 400u, UnitType.ML),
            CoffeeBeans("coffee beans", 120u, UnitType.GM),
            Milk("milk", 540u, UnitType.ML),
            DisposableCups("disposable cups", 9u, UnitType.UNIT)
        ),
        Dollar(BigDecimal(550))
    )

    val coffeeMachineMenu = CoffeeMachineMenu(coffeeMachine)

    while (true) {
        println("Write action (buy, fill, take, remaining, exit): ")
        coffeeMachineMenu.executeCommand(readln())
    }
}