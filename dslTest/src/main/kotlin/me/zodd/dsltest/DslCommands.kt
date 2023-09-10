package me.zodd.dsltest

import me.zodd.command.CommandBuilder
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
    private fun commandTest(ctx : CommandContext) {
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

    val commands = CommandBuilder.builder {
        val reason = "reason" withType Parameter.string()

        "greet" withAlias arrayOf(
            "wave",
            "sing") withDescription "greets users" withPermission "dslTest.command.greet" withArgs {
            +nameParam

            execute {
                commandTest(this)

                success()
            }
        }

        "ban" withAlias "bban" withDescription "bans users" withPermission "dslTest.command.ban" withArgs {
            +nameParam
            +reason
            execute {
                commandTest(this)

                success()
            }
        }

        "foo" withAlias "buzz" withDescription "test sub" withPermission "dslTest.command.foo" withArgs {

            +subcommand {
                "cheese" withAlias "breeze" withDescription "weezy" withPermission "dslTest.command.cheese" withArgs {
                    +subcommand {
                        "butter" withAlias "blasted" withDescription "unrated" withPermission "dslTest.command.butter" withArgs {
                            +nameParam
                            execute {
                                commandTest(this)

                                success()
                            }
                        }
                    }

                    execute {
                        commandTest(this)
                        success()
                    }
                }
            }

            execute {
                commandTest(this)
                success()
            }
        }


    }
}
