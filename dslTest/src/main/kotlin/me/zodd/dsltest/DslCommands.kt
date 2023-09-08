package me.zodd.dsltest

import me.zodd.command.CommandBuilder
import me.zodd.dsltest.DslTestArguments.nameParam
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.LinearComponents
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.spongepowered.api.command.parameter.Parameter

class DslCommands {
    private val builder = CommandBuilder()
    val commands = builder {
        "greet" aliasedWith arrayOf(
            "wave",
            "sing"
        ) describedBy "greets users" permissibleWith "dslTest.command.greet" withArgs {
            +nameParam
            execute {
                val name = it requireOne nameParam
                it.sendMessage(
                    Identity.nil(), LinearComponents.linear(
                        NamedTextColor.AQUA,
                        Component.text("Hello "),
                        Component.text(name, Style.style(TextDecoration.BOLD)),
                        Component.text("!")
                    )
                )

                it.success()
            }
        }

        val reason = "reason" typedWith Parameter.string()

        "ban" aliasedWith "bban" describedBy "bans users" permissibleWith "dslTest.command.ban" withArgs {
            +nameParam
            +reason
            execute {
                val name = it requireOne nameParam
                it.sendMessage(
                    Identity.nil(), LinearComponents.linear(
                        NamedTextColor.AQUA,
                        Component.text("Hello "),
                        Component.text(name, Style.style(TextDecoration.BOLD)),
                        Component.text(", ${it requireOne reason}")
                    )
                )
                it.success()
            }
        }

    }
}
