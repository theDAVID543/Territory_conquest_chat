package thedavid.territoryconquestchat;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DiscordSRVListener {

    private final Plugin plugin;
    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @Subscribe(priority = ListenerPriority.HIGHEST)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent e) {
        plugin.getLogger().info("Discord: " + e.getMessage());
        String message = e.getMessage().getContentDisplay();
        TextChannel global = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
        message = message.replace("\\_","_").replace("\\*","*").replace("\\~","~").replace("\\|","|").replace("\\>",">");
        if(e.getChannel().getId().equals("1117070276144943104")) {
            String[] chat = message.split("\\|",4);
            Bukkit.getLogger().info(message);
            Territory_Conquest_Chat.tradeChannelPlayersTime.put(Bukkit.getOfflinePlayer(chat[2]).getUniqueId(),300);
            Bukkit.getLogger().info(String.valueOf(Bukkit.getOfflinePlayer(chat[2]).getUniqueId()));
            Territory_Conquest_Chat.sendChannel(chat[0],chat[2],chat[3],chat[1]);
        }else {
            if(e.getChannel().getId().equals("1114819065589026827") && !e.getAuthor().isBot()){
                Territory_Conquest_Chat.sendChannel("trade", "[DC]" + e.getAuthor().getName(),message,"");
            }
        }
    }
    @Subscribe
    public void discordMessageProcessed(GameChatMessagePostProcessEvent event) {
        String message = String.valueOf(event.getProcessedMessage());
        TextChannel global = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
        if(message.contains("» ")) {
            String[] chat = message.split("» ");
            if(chat[1].charAt(0) == '$'){
                event.setCancelled(true);
            }
            if(chat[1].charAt(0) == '!'){
                event.setCancelled(true);
            }
            if(chat[1].charAt(0) == '?'){
                event.setCancelled(true);
            }
        }
    }
}