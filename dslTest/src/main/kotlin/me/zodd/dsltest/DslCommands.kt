package me.zodd.dsltest

import me.zodd.command.CommandManager
import me.zodd.dsltest.DslTestArguments.nameParam
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.LinearComponents
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.spongepowered.api.command.parameter.CommonParameters
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import java.util.function.Predicate
import kotlin.jvm.optionals.getOrNull

class DslCommands {
    val commands = CommandManager.builder {
        val reason = "reason" withType Parameter.string()

        command("greet") { baseCmd ->
            aliases += "wave"
            description = "greets users"
            permission = "dslTest.command.$baseCmd"
            terminal = false

            executionRequirement = Predicate {
                it.cause().root() is ServerPlayer
            }

            +nameParam
            executes {
                val name = requireOne(nameParam)
                sendMessage(
                    Identity.nil(), LinearComponents.linear(
                        NamedTextColor.AQUA,
                        Component.text("Hello "),
                        Component.text(name, Style.style(TextDecoration.BOLD)),
                        Component.text("!")
                    )
                )
                success()
            }
        }

        command("ban") {
            aliases += "punish"
            description = "bans users"
            permission = "dslTest.command.$it"
            +nameParam
            +reason
            executes {
                val name = requireOne(nameParam)
                sendMessage(
                    Identity.nil(), LinearComponents.linear(
                        NamedTextColor.DARK_RED,
                        Component.text("Banned "),
                        Component.text(name, Style.style(TextDecoration.BOLD)),
                        Component.text(" for $reason!")
                    )
                )
                success()
            }
        }

        command("foo") { baseCmd ->
            aliases += "buzz"
            description = "Foo the buzz on your bar"
            permission = "dslTest.command.$baseCmd"

            +nameParam
            subcommand("sub") {
                aliases += "bus"
                description = "desc"
                permission = "dslTest.command.sub"

                val noteParam = "note" withType Parameter.string()
                +noteParam
                subcommand("crank") {
                    +CommonParameters.PLAYER_OPTIONAL
                    +noteParam
                    permission = "dslTest.command.$it"
                    executes {
                        val playerOptional = one(CommonParameters.PLAYER_OPTIONAL)
                        val note = requireOne(noteParam)

                        sendMessage(
                            Identity.nil(), LinearComponents.linear(
                                NamedTextColor.LIGHT_PURPLE,
                                Component.text("player: ${playerOptional.getOrNull()?.name() ?: "No player provided"}"),
                                Component.text(note, Style.style(TextDecoration.BOLD)),
                                Component.text("!")
                            )
                        )
                        success()
                    }
                }
                executes {
                    val note = requireOne(noteParam)
                    sendMessage(
                        Identity.nil(), LinearComponents.linear(
                            NamedTextColor.DARK_AQUA,
                            Component.text("notes: "),
                            Component.text(note, Style.style(TextDecoration.BOLD)),
                            Component.text("!")
                        )
                    )
                    success()
                }
            }
            subcommand("bool") {
                permission = "dslTest.command.$it"

                +CommonParameters.BOOLEAN
                executes {
                    val bool = requireOne(CommonParameters.BOOLEAN)
                    sendMessage(
                        Identity.nil(), LinearComponents.linear(
                            NamedTextColor.BLUE,
                            Component.text("boolean: "),
                            Component.text(bool, Style.style(TextDecoration.BOLD)),
                            Component.text("!")
                        )
                    )
                    success()
                }
            }

            executes {
                val name = requireOne(nameParam)
                sendMessage(
                    Identity.nil(), LinearComponents.linear(
                        NamedTextColor.GOLD,
                        Component.text("Executed "),
                        Component.text(name, Style.style(TextDecoration.BOLD)),
                        Component.text("!")
                    )
                )
                success()
            }
        }
    }
}
