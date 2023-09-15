package me.zodd.command

import me.zodd.annotation.SpongeDsl

@SpongeDsl
class CommandManager : DslArgument, DslContext {

    companion object {
        val builder
            get() = CommandManager()
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
