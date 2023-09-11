package me.zodd.command.util

import me.zodd.command.DslCommand
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.plugin.PluginContainer

fun RegisterCommandEvent<Command.Parameterized>.register(
    container: PluginContainer,
    dslCommand: DslCommand
) {
    this.register(container, dslCommand.command, dslCommand.baseAlias, *dslCommand.remainingAliases)
}

fun RegisterCommandEvent<Command.Parameterized>.register(
    container: PluginContainer,
    dslCommands: List<DslCommand>
) {
    dslCommands.forEach { this.register(container, it) }
}
