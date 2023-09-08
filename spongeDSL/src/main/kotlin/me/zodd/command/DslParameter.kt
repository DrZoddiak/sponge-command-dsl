package me.zodd.command

import org.spongepowered.api.command.parameter.Parameter


interface DslParameter {
    infix fun <T> String.typedWith(type: Parameter.Value.Builder<T>): Parameter.Value<T> {
        return type.key(this).build()
    }

    infix fun <T> Parameter.Value.Builder<T>.keyedWith(key: String): Parameter.Value<T> {
        return this.key(key).build()
    }
}