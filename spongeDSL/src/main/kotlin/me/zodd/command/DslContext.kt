package me.zodd.command

import net.kyori.adventure.text.Component
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.command.parameter.managed.Flag

internal sealed interface DslContext {
    infix fun <T> CommandContext.requireOne(param: Parameter.Value<T>): T = this.requireOne(param)

    fun CommandContext.success(): CommandResult = CommandResult.success()

    infix fun CommandContext.error(errorMessage: Component): CommandResult = CommandResult.error(errorMessage)

    infix fun CommandContext.hasFlag(flag: Flag) = this.hasFlag(flag)
}
