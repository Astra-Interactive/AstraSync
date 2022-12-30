import com.astrainteractive.astrasync.commands.reload
import com.astrainteractive.astrasync.commands.syncServer
import com.astrainteractive.astrasync.commands.tabCompleter
import com.astrainteractive.astrasync.gui.PlayerSavesGUI
import com.astrainteractive.astrasync.utils.AstraPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.commands.DSLCommand


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
        DSLCommand.invoke("history") {
            if (!AstraPermission.Damage.hasPermission(sender)) {
                sender.sendMessage("No permission")
                return@invoke
            }
            val playerSender = this.sender as? Player ?: run {
                sender.sendMessage("Player only command")
                return@invoke
            }

            argument(0, parser = { it?.let(Bukkit::getPlayer) }, onError = {
                sender.sendMessage("${ChatColor.RED}Wrong usage! /history <player>")
            }) {
                PluginScope.launch(Dispatchers.IO) {
                    PlayerSavesGUI(playerSender, it.value).open()
                }
            }
        }
    }


}