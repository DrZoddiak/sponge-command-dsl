package me.zodd.command

import me.zodd.annotation.SpongeDsl
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.command.parameter.managed.Flag

@SpongeDsl
class ArgumentBuilder(private val builder: AbstractCommandBuilder<*>) : DslContext {
    var parameters = mutableListOf<Parameter>()
    var flags = mutableListOf<Flag>()

    operator fun <T : Any> Parameter.Value<T>.unaryPlus() {
        parameters.add(this)
    }

    operator fun Flag.unaryPlus() {
        flags.add(this)
    }

    operator fun List<DslCommand>.unaryPlus() {
        map { Parameter.subcommand(it.command, it.baseAlias, *it.remainingAliases) }.forEach { parameters.add(it) }
    }

    val subcommand = SubCommandBuilder()

    infix fun execute(exec: CommandContext.() -> CommandResult): AbstractCommandBuilder<*> {
        builder.commandExecutor = exec
        return builder
    }

    internal fun clear() {
        parameters.clear()
        flags.clear()
    }
}



