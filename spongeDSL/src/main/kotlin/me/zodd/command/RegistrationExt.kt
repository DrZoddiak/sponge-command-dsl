package me.zodd.command

import org.spongepowered.api.command.Command
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.plugin.PluginContainer

fun RegisterCommandEvent<Command.Parameterized>.register(
    container: PluginContainer,
    dslCommand: DslCommand
) {
    this.register(container, dslCommand.command, dslCommand.baseCmd, *dslCommand.remainingAliases)
}

fun RegisterCommandEvent<Command.Parameterized>.register(
    container: PluginContainer,
    dslCommands: MutableList<DslCommand>
) {
    dslCommands.forEach { this.register(container, it) }
}
