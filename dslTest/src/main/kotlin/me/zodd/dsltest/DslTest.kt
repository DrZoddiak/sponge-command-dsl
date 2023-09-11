package me.zodd.dsltest

import com.google.inject.Inject
import me.zodd.command.util.register
import org.apache.logging.log4j.Logger
import org.spongepowered.api.Server
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.api.event.lifecycle.StartingEngineEvent
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin

/**
 * The main class of your Sponge plugin.
 *
 *
 * All methods are optional -- some common event registrations are included as a jumping-off point.
 */
@Plugin("dsltest")
class DslTest @Inject constructor(private val container: PluginContainer, private val logger: Logger) {

    @Listener
    fun onConstructPlugin(event: ConstructPluginEvent?) {
        // Perform any one-time setup
        logger.info("Constructing dslTest")
    }

    @Listener
    fun onRegister(event: RegisterCommandEvent<Command.Parameterized>) {
        event.register(container, DslCommands().commands)
    }

    @Listener
    fun onServerStarting(event: StartingEngineEvent<Server?>?) {
        // Any setup per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
    }

    @Listener
    fun onServerStopping(event: StoppingEngineEvent<Server?>?) {
        // Any tear down per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
    }
}
