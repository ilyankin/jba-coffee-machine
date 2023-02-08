import kotlin.system.exitProcess

interface Command {
    fun execute()
}

sealed class CoffeeMachineCommand(open val coffeeMachine: CoffeeMachine) : Command // CM - machine.CoffeeMachine

class BuyCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        coffeeMachine.buy()
    }
}

class FillCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        coffeeMachine.fill()
    }
}

class TakeCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        coffeeMachine.take()
    }
}

class RemainingCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        println(coffeeMachine.contentToString(0))
    }
}

class ExitCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        exitProcess(0)
    }
}

class UndefinedCMCommand(override val coffeeMachine: CoffeeMachine) : CoffeeMachineCommand(coffeeMachine) {
    override fun execute() {
        println("Undefined command")
    }
}