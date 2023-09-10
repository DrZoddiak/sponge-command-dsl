package me.zodd.command

import me.zodd.annotation.SpongeDsl
import net.kyori.adventure.text.Component
import org.spongepowered.api.command.Command
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext

@SpongeDsl
class CommandBuilder : AbstractCommandBuilder<CommandBuilder>() {

    companion object {
        val builder = CommandBuilder()
    }

    override fun getInstance(): CommandBuilder {
        return this
    }
}

@SpongeDsl
class SubCommandBuilder : AbstractCommandBuilder<SubCommandBuilder>() {
    override fun getInstance(): SubCommandBuilder {
        return this
    }
}

abstract class AbstractCommandBuilder<T : AbstractCommandBuilder<T>> : DslContext, DslArgument {

    internal var builtCommands = mutableListOf<DslCommand>()

    private lateinit var aliases: List<String>
    private lateinit var description: String
    private lateinit var permission: String
    internal lateinit var commandExecutor: CommandContext.() -> CommandResult

    init {
        applyDefaults()
    }

    private fun applyDefaults() {
        aliases = listOf()
        commandExecutor = { error(Component.text("Command executor not registered")) }
        permission = ""
        description = ""
    }

    operator fun invoke(initializer: T.() -> Unit): List<DslCommand> {
        initializer(getInstance())
        return builtCommands
    }

    infix fun String.withAlias(aliases: String): T {
        getInstance().aliases = listOf(this, *aliases.split(",").toTypedArray())
        return getInstance()
    }

    infix fun String.withAlias(aliases: Array<String>): T {
        return this.withAlias(aliases.joinToString(","))
    }

    infix fun T.withDescription(description: String): T {
        this.description = description
        return this
    }

    infix fun T.withPermission(permission: String): T {
        this.permission = permission
        return this
    }

    infix fun T.withArgs(param: ArgumentBuilder.() -> Unit): DslCommand {
        val commandBuilder = Command.builder()
        val argBuilder = ArgumentBuilder(getInstance())
        argBuilder.param()

        commandBuilder.addParameters(argBuilder.parameters)
        commandBuilder.addFlags(argBuilder.flags)
        commandBuilder.executor(commandExecutor)
        commandBuilder.shortDescription(Component.text(description))
        commandBuilder.permission(permission)

        val command = DslCommand(aliases, commandBuilder.build())
        builtCommands.add(command)
        applyDefaults()
        argBuilder.clear()

        return command
    }

    abstract fun getInstance(): T
}

