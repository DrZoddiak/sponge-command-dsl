package me.zodd.command

import me.zodd.annotation.SpongeDsl
import net.kyori.adventure.text.Component
import org.spongepowered.api.command.Command
import org.spongepowered.api.command.Command.Parameterized
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.managed.Flag
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.plugin.PluginContainer

@SpongeDsl
class CommandBuilder : DslContext, DslArgument {

    private var builtCommands = mutableListOf<DslCommand>()

    private var aliases = mutableListOf<String>()
    private lateinit var description: String
    private lateinit var permission: String
    internal lateinit var commandExecutor: (CommandContext) -> CommandResult

    init {
        applyDefaults()
    }

    operator fun invoke(initializer: CommandBuilder.() -> Unit): MutableList<DslCommand> {
        initializer()
        return builtCommands
    }

    infix fun CommandBuilder.describedBy(description: String): CommandBuilder {
        this.description = description
        return this
    }

    infix fun String.describedBy(description: String): CommandBuilder {
        return describedBy(description)
    }

    infix fun String.aliasedWith(aliases: String): CommandBuilder {
        this@CommandBuilder.aliases.add(this)
        this@CommandBuilder.aliases.addAll(aliases.split(","))
        return this@CommandBuilder
    }

    infix fun String.aliasedWith(aliases: Array<String>): CommandBuilder {
        return this.aliasedWith(aliases.joinToString(","))
    }

    infix fun withArgs(param: ArgumentBuilder.() -> Unit): DslCommand {
        val commandBuilder = Command.builder()
        val argBuilder = ArgumentBuilder(this)
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

    infix fun permissibleWith(permission: String): CommandBuilder {
        this.permission = permission
        return this
    }

    private fun applyDefaults() {
        aliases.clear()
        commandExecutor = { it.error(Component.text("Command executor not registered")) }
        permission = ""
        description = ""
    }
}