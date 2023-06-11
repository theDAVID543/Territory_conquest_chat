package thedavid.territoryconquestchat;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordConsoleCommandPreProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DiscordSRVListener {
    private static Map<Message, Boolean> deleted = new HashMap<>();

    private final Plugin plugin;
    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @Subscribe(priority = ListenerPriority.NORMAL)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent e) {
        Map<String, UUID> link = DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts();
        if(!e.getAuthor().isBot()) {
            if (e.getChannel().getId().equals("1114819065589026827")) {
                if (!Objects.equals(Territory_Conquest_Chat.tradeChannelPlayersTime.get(link.get(e.getAuthor().getId())), null) && Territory_Conquest_Chat.tradeChannelPlayersTime.get(link.get(e.getAuthor().getId())) >= 0) {
                    DiscordUtil.deleteMessage(e.getMessage());
                    DiscordUtil.privateMessage(e.getAuthor(),
                            "需等待 " + Territory_Conquest_Chat.tradeChannelPlayersTime.get(link.get(e.getAuthor().getId())) + " 秒後才可再次在此頻道發送訊息"
                    );
                    return;
                } else {
                    Territory_Conquest_Chat.tradeChannelPlayersTime.put(link.get(e.getAuthor().getId()), 300);
                }
            }
            if (e.getChannel().getId().equals("1114819096719147008")) {
                if (!Objects.equals(Territory_Conquest_Chat.adChannelPlayersTime.get(link.get(e.getAuthor().getId())), null) && Territory_Conquest_Chat.adChannelPlayersTime.get(link.get(e.getAuthor().getId())) >= 0) {
                    DiscordUtil.deleteMessage(e.getMessage());
                    DiscordUtil.privateMessage(e.getAuthor(),
                            "需等待 " + Territory_Conquest_Chat.adChannelPlayersTime.get(link.get(e.getAuthor().getId())) + " 秒後才可再次在此頻道發送訊息"
                    );
                    return;
                } else {
                    Territory_Conquest_Chat.adChannelPlayersTime.put(link.get(e.getAuthor().getId()), 300);
                }
            }
            if (e.getChannel().getId().equals("1114819138481836064")) {
                if (!Objects.equals(Territory_Conquest_Chat.qaChannelPlayersTime.get(link.get(e.getAuthor().getId())), null) && Territory_Conquest_Chat.qaChannelPlayersTime.get(link.get(e.getAuthor().getId())) >= 0) {
                    DiscordUtil.deleteMessage(e.getMessage());
                    DiscordUtil.privateMessage(e.getAuthor(),
                            "需等待 " + Territory_Conquest_Chat.qaChannelPlayersTime.get(link.get(e.getAuthor().getId())) + " 秒後才可再次在此頻道發送訊息"
                    );
                    return;
                } else {
                    Territory_Conquest_Chat.qaChannelPlayersTime.put(link.get(e.getAuthor().getId()), 3);
                }
            }
            if (e.getChannel().getId().equals("1111272413670428683")) {
                if (!Objects.equals(Territory_Conquest_Chat.globalChannelPlayersTime.get(link.get(e.getAuthor().getId())), null) && Territory_Conquest_Chat.globalChannelPlayersTime.get(link.get(e.getAuthor().getId())) >= 0) {
                    DiscordUtil.deleteMessage(e.getMessage());
                    DiscordUtil.privateMessage(e.getAuthor(),
                            "需等待 " + Territory_Conquest_Chat.globalChannelPlayersTime.get(link.get(e.getAuthor().getId())) + " 秒後才可再次在此頻道發送訊息"
                    );
                    deleted.put(e.getMessage(),true);
                    return;
                } else {
                    Territory_Conquest_Chat.globalChannelPlayersTime.put(link.get(e.getAuthor().getId()), 3);
                }
            }
        }

        String message = e.getMessage().getContentDisplay();
        TextChannel global = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
        message = message.replace("\\_","_").replace("\\*","*").replace("\\~","~").replace("\\|","|").replace("\\>",">");
        if(e.getChannel().getId().equals("1117070276144943104")) {
            String[] chat = message.split("\\|",4);
            Bukkit.getLogger().info(message);
            if(Objects.equals(chat[0], "trade")){
                Territory_Conquest_Chat.tradeChannelPlayersTime.put(Bukkit.getOfflinePlayer(chat[2]).getUniqueId(),300);
            }else if(Objects.equals(chat[0], "ad")){
                Territory_Conquest_Chat.adChannelPlayersTime.put(Bukkit.getOfflinePlayer(chat[2]).getUniqueId(),300);
            }else if(Objects.equals(chat[0], "qa")){
                Territory_Conquest_Chat.qaChannelPlayersTime.put(Bukkit.getOfflinePlayer(chat[2]).getUniqueId(),3);
            }else if(Objects.equals(chat[0], "global")){
                Territory_Conquest_Chat.globalChannelPlayersTime.put(Bukkit.getOfflinePlayer(chat[2]).getUniqueId(),3);
            }
            Territory_Conquest_Chat.sendChannel(chat[0],chat[2],chat[3],chat[1]);
        }else if(e.getChannel().getId().equals("1114819065589026827") && !e.getAuthor().isBot()){
            Territory_Conquest_Chat.sendChannel("trade", "§9[DC]§r" + e.getAuthor().getName(),message,"");
        }else if(e.getChannel().getId().equals("1114819096719147008") && !e.getAuthor().isBot()){
            Territory_Conquest_Chat.sendChannel("ad", "§9[DC]§r" + e.getAuthor().getName(),message,"");
        }else if(e.getChannel().getId().equals("1114819138481836064") && !e.getAuthor().isBot()){
            Territory_Conquest_Chat.sendChannel("qa", "§9[DC]§r" + e.getAuthor().getName(),message,"");
        }else if(e.getChannel().getId().equals("1111272413670428683") && !e.getAuthor().isBot()){
//            Territory_Conquest_Chat.sendChannel("global", "§9[DC]§r" + e.getAuthor().getName(),message,"");
        }
    }
    @Subscribe
    public void discordMessageProcessed(GameChatMessagePostProcessEvent event) {
        String message = String.valueOf(event.getProcessedMessage());
        if(message.contains("» ") && Objects.equals(event.getChannel(), "global")) {
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
    @Subscribe(priority = ListenerPriority.HIGHEST)
    public void discordMessageProcessed(DiscordGuildMessagePostProcessEvent e) {
        // Example of modifying a Discord -> Minecraft message
        if(e.getAuthor().isBot() || Objects.equals(deleted.get(e.getMessage()), true)){
            e.setCancelled(true);
        }
    }
}