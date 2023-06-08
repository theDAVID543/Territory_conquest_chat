package thedavid.territoryconquestchat;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
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
    @Subscribe(priority = ListenerPriority.MONITOR)
    public void aMessageWasSentInADiscordGuildByTheBot(DiscordGuildMessageSentEvent event) {
//        plugin.getLogger().info("Received a chat message on Discord: " + event.getMessage());
//        String message = String.valueOf(event.getMessage());
//        TextChannel global = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
//        if(message.contains("» ")) {
//            String[] chat = message.split("» ");
//            Bukkit.getLogger().info(chat[0] + "\n" + chat[1]);
//            if(chat[1].charAt(0) == '$' && event.getChannel() == global){
//                DiscordUtil.deleteMessage(event.getMessage());
//            }
//            if(chat[1].charAt(0) == '!' && event.getChannel() == global){
//                DiscordUtil.deleteMessage(event.getMessage());
//            }
//            if(chat[1].charAt(0) == '?' && event.getChannel() == global){
//                DiscordUtil.deleteMessage(event.getMessage());
//            }
//        }
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