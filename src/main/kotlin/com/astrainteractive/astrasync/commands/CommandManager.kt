import com.astrainteractive.astrasync.commands.reload
import com.astrainteractive.astrasync.commands.syncServer
import com.astrainteractive.astrasync.commands.tabCompleter


/**
 * Command handler for your plugin
 * It's better to create different executors for different commands
 * @see Reload
 */
class CommandManager {
    /**
     * Here you should declare commands for your plugin
     *
     * Commands stored in plugin.yml
     *
     * etemp has TabCompleter
     */
    init {
        tabCompleter()
        reload()
        syncServer()
    }


}