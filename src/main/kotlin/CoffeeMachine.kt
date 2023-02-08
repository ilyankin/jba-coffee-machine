import java.math.BigDecimal

class CoffeeMachineMenu(var coffeeMachine: CoffeeMachine) {
    companion object {
        private const val BUY_COMMAND_TEXT = "buy"
        private const val FILL_COMMAND_TEXT = "fill"
        private const val TAKE_COMMAND_TEXT = "take"
        private const val EXIT_COMMAND_TEXT = "exit"
        private const val REMAINING_COMMAND_TEXT = "remaining"
    }

    fun executeCommand(input: String) {
        executeCommand(
            when (input) {
                BUY_COMMAND_TEXT -> BuyCMCommand(coffeeMachine)
                FILL_COMMAND_TEXT -> FillCMCommand(coffeeMachine)
                TAKE_COMMAND_TEXT -> TakeCMCommand(coffeeMachine)
                REMAINING_COMMAND_TEXT -> RemainingCMCommand(coffeeMachine)
                EXIT_COMMAND_TEXT -> ExitCMCommand(coffeeMachine)
                else -> UndefinedCMCommand(coffeeMachine)
            }
        )
    }

    private fun executeCommand(command: Command) {
        command.execute()
    }
}

class CoffeeMachine(var storeVolumes: StoreVolumes, var income: Money = Dollar(BigDecimal.ZERO)) {
    fun contentToString(): String {
        return """The coffee machine has:
            |${storeVolumes.contentToString()}
            |${income.getFormattedString()} of money
            """.trimMargin()
    }

    fun contentToString(fractDigits: Int): String {
        return """The coffee machine has:
            |${storeVolumes.contentToString()}
            |${income.getFormattedString(fractDigits)} of money
            """.trimMargin()
    }

    internal fun buy() {
        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        val input = readln()
        if (input == "back") return
        when (input.toUInt()) {
            1u -> makeDrink(Espresso())
            2u -> makeDrink(Latter())
            3u -> makeDrink(Cappuccino())
            else -> println("The coffee machine cannot recognize this command")
        }
    }

    private fun makeDrink(cup: WaterCup) {
        val missingVolumes = getMissingVolumes(cup)

        if (missingVolumes.isEmpty()) {
            println("I have enough resources, making you a coffee!")
            income += storeVolumes.deductQuantity(cup)
        } else {
            missingVolumes.forEach {
                println("Sorry, not enough ${it.key}!")
            }
        }
    }

    private fun getMissingVolumes(cup: Cup): Map<String, UInt> {
        val availableCupsByVolume = when (cup) {
            is Espresso -> getAvailableCupsByVolume(cup.water, cup.coffeeBeans)
            is Cappuccino -> getAvailableCupsByVolume(cup.water, cup.coffeeBeans, cup.milk)
            is Latter -> getAvailableCupsByVolume(cup.water, cup.coffeeBeans, cup.milk)
        }
        return availableCupsByVolume.filter { it.value == 0u }
    }

    private fun getAvailableCupsByVolume(vararg volumes: Volume): Map<String, UInt> {
        val availableCupsByVolume = volumes.associate { it.name to 0u }.toMutableMap()

        volumes.forEach {
            when (it) {
                is Water -> availableCupsByVolume[it.name] = storeVolumes.water.quantity / it.quantity
                is CoffeeBeans -> availableCupsByVolume[it.name] = storeVolumes.coffeeBeans.quantity / it.quantity
                is Milk -> availableCupsByVolume[it.name] = storeVolumes.milk.quantity / it.quantity
                is DisposableCups -> availableCupsByVolume[it.name] = storeVolumes.cups.quantity / it.quantity
            }
        }

        return availableCupsByVolume
    }

    internal fun fill() {
        storeVolumes.volumes.forEach { volume ->
            val value = volume.unit.value
            if (volume.unit == UnitType.GM) {
                println("Write how many ${value}rams of ${volume.name} the coffee machine has")
            } else if (value.isNotBlank()) {
                println("Write how many $value of ${volume.name} the coffee machine has")
            } else {
                println("Write how many ${volume.name} the coffee machine has")
            }
            volume.quantity += readln().toUInt()
        }
    }

    internal fun take() {
        val amount = income.amount
        println("I gave you $amount")
        income.amount -= amount
    }
}

class StoreVolumes(
    val water: Water = Water("water", 0U, UnitType.ML),
    val coffeeBeans: CoffeeBeans = CoffeeBeans("coffee beans", 0U, UnitType.GM),
    val milk: Milk = Milk("milk", 0U, UnitType.GM),
    val cups: DisposableCups = DisposableCups("disposable cups", 0U, UnitType.UNIT)
) {

    val volumes: Set<Volume> = linkedSetOf(water, milk, coffeeBeans, cups)

    fun deductQuantity(cup: WaterCup): Money {
        when (cup) {
            is Espresso -> deductQuantity(cup.water, cup.coffeeBeans)
            is Cappuccino -> deductQuantity(cup.water, cup.coffeeBeans, cup.milk)
            is Latter -> deductQuantity(cup.water, cup.coffeeBeans, cup.milk)
        }
        cups.quantity--
        return cup.price
    }

    private fun deductQuantity(vararg volumes: Volume) {
        volumes.forEach { volume ->
            when (volume) {
                is Water -> water.quantity -= volume.quantity
                is CoffeeBeans -> coffeeBeans.quantity -= volume.quantity
                is Milk -> milk.quantity -= volume.quantity
                is DisposableCups -> {}
            }
        }
    }

    fun contentToString(): String {
        var result = ""
        volumes.forEach { volume ->
            val value = volume.unit.value
            result +=
                if (value.isNotBlank()) "${volume.quantity} $value of ${volume.name}\n"
                else "${volume.quantity} ${volume.name}"
        }
        return result
    }
}