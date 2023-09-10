package me.zodd.command

import org.spongepowered.api.command.Command

data class DslCommand(
    private val aliases: List<String>,
    val command: Command.Parameterized
) {
    val baseAlias = aliases[0]
    val remainingAliases = aliases.filterNot { it.contentEquals(baseAlias) }.toTypedArray()
}
