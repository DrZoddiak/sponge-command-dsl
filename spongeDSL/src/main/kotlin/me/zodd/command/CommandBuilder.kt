package me.zodd.command

import me.zodd.annotation.SpongeDsl
import net.kyori.adventure.text.Component
import org.spongepowered.api.command.Command
import org.spongepowered.api.command.CommandCause
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.command.parameter.managed.Flag
import java.util.function.Predicate

@SpongeDsl
class CommandManager : DslArgument, DslContext {

    companion object {
        val builder = CommandManager()
    }

    operator fun invoke(initializer: CommandManager.() -> Unit): List<DslCommand> {
        this.initializer()
        return CommandBuilder.builtCommands
    }

    fun command(name: String, initializer: CommandBuilder.(name: String) -> Unit): DslCommand {
        val builder = CommandBuilder()
        builder.aliases += name
        builder.initializer(name)
        return builder.buildCommand()
    }
}

@SpongeDsl
class CommandBuilder : DslArgument, DslContext {

    companion object {
        internal val builtCommands = mutableListOf<DslCommand>()
    }

    var aliases: MutableList<String> = mutableListOf()
    var description: String = ""
    var permission: String = ""
    var terminal: Boolean = false
    var executionRequirement: Predicate<CommandCause> = Predicate { true }

    private var commandExecutor: CommandContext.() -> CommandResult =
        { error(Component.text("Command executor not registered")) }

    private var parameters = mutableListOf<Parameter>()
    private var subcommands = mutableListOf<DslCommand>()
    private var flags = mutableListOf<Flag>()

    operator fun <T : Any> Parameter.Value<T>.unaryPlus() {
        parameters += this
    }

    operator fun Flag.unaryPlus() {
        flags += this
    }

    fun subcommand(name: String, initializer: CommandBuilder.(cmd: String) -> Unit): DslCommand {
        val subBuilder = CommandBuilder()
        subBuilder.aliases += name
        subBuilder.initializer(name)
        val command = subBuilder.buildCommand()
        subcommands += command
        return command
    }

    fun executes(exec: CommandContext.() -> CommandResult) {
        commandExecutor = exec
    }

    internal fun buildCommand(): DslCommand {
        val spongeCommandBuilder = Command.builder()

        spongeCommandBuilder.apply {
            subcommands.forEach {
                addChild(it.command, it.aliases)
            }
            addParameters(parameters)
            addFlags(flags)
            executionRequirements(executionRequirement)
            terminal(terminal)
            executor(commandExecutor)
            shortDescription(Component.text(description))
            permission(permission)
        }
        val command = DslCommand(aliases, spongeCommandBuilder.build())
        builtCommands.add(command)

        return command
    }
}