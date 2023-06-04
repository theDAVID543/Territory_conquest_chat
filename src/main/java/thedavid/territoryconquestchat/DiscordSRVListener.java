package thedavid.territoryconquestchat;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DiscordSRVListener {
    private final Plugin plugin;

    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @Subscribe(priority = ListenerPriority.MONITOR)
    public void aMessageWasSentInADiscordGuildByTheBot(DiscordGuildMessageSentEvent e) {
        String chatText = e.getMessage().getContentDisplay();
        Bukkit.getLogger().info(chatText);
    }
}
