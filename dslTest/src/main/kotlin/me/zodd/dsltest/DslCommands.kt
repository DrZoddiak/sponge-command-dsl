package me.zodd.dsltest

import me.zodd.command.CommandManager
import me.zodd.dsltest.DslTestArguments.nameParam
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.LinearComponents
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.parameter.Parameter

class DslCommands {
    private fun commandTest(ctx: CommandContext) {
        val name = ctx.requireOne(nameParam)
        ctx.sendMessage(
            Identity.nil(), LinearComponents.linear(
                NamedTextColor.AQUA,
                Component.text("Hello "),
                Component.text(name, Style.style(TextDecoration.BOLD)),
                Component.text("!")
            )
        )
    }

    val commands = CommandManager.builder {
        val reason = "reason" withType Parameter.string()


        command("greet") {
            aliases += "wave"
            aliases += "sing"
            description = "greets users"
            permission = "dslTest.command.greet"
            +nameParam
            executes {
                commandTest(this)
                success()
            }
        }

        command("ban") {
            aliases += "bban"
            description = "bans users"
            permission = "dslTest.command.ban"
            +nameParam
            +reason
            executes {
                commandTest(this)
                success()
            }
        }

        command("foo") {
            aliases += "buzz"
            description = "test desc"
            permission = "dslTest.command.foo"

            +nameParam
            subcommand("sub") {
                aliases += "bus"
                description = "desc"
                permission = "dslTest.command.sub"

                subcommand("crank") {
                    permission = "dslTest.command.crank"
                    executes {
                        success()
                    }
                }
                executes {
                    success()
                }
            }
            subcommand("test") {
                permission = "dslTest.command.test"
                executes {
                    success()
                }
            }

            executes {
                commandTest(this)
                success()
            }
        }
    }
}
