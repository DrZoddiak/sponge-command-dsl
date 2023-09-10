package me.zodd.command

import me.zodd.annotation.SpongeDsl
import net.kyori.adventure.text.Component
import org.spongepowered.api.command.Command
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.command.parameter.managed.Flag

@SpongeDsl
class CommandBuilder : DslArgument, DslContext {

    companion object {
        val builder = CommandBuilder()
    }

    init {
        applyDefaults()
    }

    lateinit var aliases: MutableList<String>
    lateinit var description: String
    lateinit var permission: String

    private lateinit var commandExecutor: CommandContext.() -> CommandResult

    private var parameters = mutableListOf<Parameter>()
    private var subcommands = mutableListOf<DslCommand>()
    private var flags = mutableListOf<Flag>()

    private fun applyDefaults() {
        aliases = mutableListOf()
        description = ""
        permission = ""

        commandExecutor = { error(Component.text("Command executor not registered")) }

        parameters = mutableListOf()
        subcommands = mutableListOf()
        flags = mutableListOf()
    }

    private var builtCommands = mutableListOf<DslCommand>()

    operator fun invoke(initializer: CommandBuilder.() -> Unit): List<DslCommand> {
        this.initializer()
        return builtCommands
    }

    operator fun <T : Any> Parameter.Value<T>.unaryPlus() {
        parameters += this
    }

    operator fun Flag.unaryPlus() {
        flags += this
    }

    fun command(name: String, initializer: CommandBuilder.(name: String) -> Unit): DslCommand {
        aliases += name
        this.initializer(name)
        return buildCommand()
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

    private fun buildCommand(): DslCommand {
        val spongeCommandBuilder = Command.builder()

        spongeCommandBuilder.apply {
            subcommands.forEach {
                addChild(it.command, it.aliases)
            }
            addParameters(parameters)
            addFlags(flags)
            executor(commandExecutor)
            shortDescription(Component.text(description))
            permission(permission)
        }
        val command = DslCommand(aliases, spongeCommandBuilder.build())
        builtCommands.add(command)
        applyDefaults()

        return command
    }
}