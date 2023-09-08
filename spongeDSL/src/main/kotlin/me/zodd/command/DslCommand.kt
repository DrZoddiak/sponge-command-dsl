package me.zodd.command

import org.spongepowered.api.command.Command

data class DslCommand(
    private val aliases: MutableList<String>,
    val command: Command.Parameterized
) {
    val baseCmd = aliases[0]
    val remainingAliases = aliases.filterNot { it == baseCmd }.toTypedArray()
}
