package thedavid.territoryconquestchat;

import com.comphenix.protocol.PacketType;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Territory_Conquest_Chat extends JavaPlugin implements Listener, CommandExecutor {
    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);
    public static Set<Player> tradeChannelPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        DiscordSRV.api.subscribe(discordsrvListener);
        Objects.requireNonNull(Bukkit.getPluginCommand("channel")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }
    @EventHandler
    public void onChat(AsyncChatEvent e){
        String chatText = PlainTextComponentSerializer.plainText().serialize(e.message());
        if(chatText.charAt(0) == '$'){
            String changed = chatText.substring(1);
            TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("trade");
            DiscordUtil.queueMessage(channel, e.getPlayer().getName() + " » " + changed);
            sendChannel("trade",e.getPlayer(), changed);
            e.setCancelled(true);
        }
    }
    public static void sendChannel(String channel, Player sendPlayer, String message){
        for(Player player : tradeChannelPlayers){
            player.sendMessage(Component.text("$ ").color(NamedTextColor.GOLD)
                    .append(Component.text(sendPlayer.getName()))
                    .append(Component.text("> ").color(NamedTextColor.GRAY))
                    .append(Component.text(message))
            );

        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }
        if(Objects.equals(args[0], "trade") || Objects.equals(args[0], "t")){
            if(!tradeChannelPlayers.contains(sender)){
                tradeChannelPlayers.add(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("交易頻道(trade)").color(NamedTextColor.GREEN))
                                .append(Component.text("中"))
                );
            }else{
                tradeChannelPlayers.add(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("已取消觀看 ")
                                .append(Component.text("交易頻道(trade)").color(NamedTextColor.GREEN))
                );
            }
        }
        return true;
    }
}
