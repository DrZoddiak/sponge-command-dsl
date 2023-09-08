package me.zodd.dsltest

import me.zodd.command.DslArgument
import org.spongepowered.api.command.parameter.Parameter

object DslTestArguments : DslArgument {
    val nameParam = "name" typedWith Parameter.string()
}