package thedavid.territoryconquestchat;

import com.loohp.interactivechat.utils.ChatColorUtils;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Territory_Conquest_Chat extends JavaPlugin implements Listener, CommandExecutor {
    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);
    public static Set<Player> tradeChannelPlayers = new HashSet<>();
    public static Set<Player> adChannelPlayers = new HashSet<>();
    public static Set<Player> qaChannelPlayers = new HashSet<>();

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
            if(!tradeChannelPlayers.contains(e.getPlayer())){
                tradeChannelPlayers.add(e.getPlayer());
                e.getPlayer().sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("交易頻道(trade)").color(NamedTextColor.GOLD))
                                .append(Component.text("中"))
                );
            }
            sendChannel("trade",e.getPlayer(), changed);
            e.setCancelled(true);
        }
        if(chatText.charAt(0) == '!'){
            String changed = chatText.substring(1);
            TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("ad");
            DiscordUtil.queueMessage(channel, e.getPlayer().getName() + " » " + changed);
            if(!adChannelPlayers.contains(e.getPlayer())){
                adChannelPlayers.add(e.getPlayer());
                e.getPlayer().sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("設施宣傳頻道(ad)").color(NamedTextColor.AQUA))
                                .append(Component.text("中"))
                );
            }
            sendChannel("ad",e.getPlayer(), changed);
            e.setCancelled(true);
        }
        if(chatText.charAt(0) == '?'){
            String changed = chatText.substring(1);
            TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("qa");
            DiscordUtil.queueMessage(channel, e.getPlayer().getName() + " » " + changed);
            if(!qaChannelPlayers.contains(e.getPlayer())){
                qaChannelPlayers.add(e.getPlayer());
                e.getPlayer().sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("問答頻道(ad)").color(NamedTextColor.BLUE))
                                .append(Component.text("中"))
                );
            }
            sendChannel("qa",e.getPlayer(), changed);
            e.setCancelled(true);
        }
    }
    public static void sendChannel(String channel, Player sendPlayer, String message){
        String prefix = PlaceholderAPI.setPlaceholders(sendPlayer, "%vault_prefix%");
        String coloredPrefix = ChatColorUtils.translateAlternateColorCodes('&',prefix);
        if(Objects.equals(channel, "trade")){
            for(Player player : tradeChannelPlayers){
                player.sendMessage(
                        Component.text("$ ").color(NamedTextColor.GOLD)
                                .append(Component.text(coloredPrefix))
                                .append(Component.text(sendPlayer.getName()).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(Component.text(message).color(NamedTextColor.WHITE))
                );
            }
        }
        if(Objects.equals(channel, "ad")){
            for(Player player : adChannelPlayers){
                player.sendMessage(
                        Component.text("! ").color(NamedTextColor.AQUA)
                                .append(Component.text(coloredPrefix))
                                .append(Component.text(sendPlayer.getName()).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(Component.text(message).color(NamedTextColor.WHITE))
                );
            }
        }
        if(Objects.equals(channel, "qa")){
            for(Player player : qaChannelPlayers){
                player.sendMessage(
                        Component.text("? ").color(NamedTextColor.BLUE)
                                .append(Component.text(coloredPrefix))
                                .append(Component.text(sendPlayer.getName()).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(Component.text(message).color(NamedTextColor.WHITE))
                );
            }
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
                                .append(Component.text("交易頻道(trade)").color(NamedTextColor.GOLD))
                                .append(Component.text(" 中"))
                );
            }else{
                tradeChannelPlayers.remove(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("已取消觀看 ")
                                .append(Component.text("交易頻道(trade)").color(NamedTextColor.GOLD))
                );
            }
        }
        if(Objects.equals(args[0], "ad") || Objects.equals(args[0], "a")){
            if(!adChannelPlayers.contains(sender)){
                adChannelPlayers.add(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("設施宣傳頻道(ad)").color(NamedTextColor.AQUA))
                                .append(Component.text(" 中"))
                );
            }else{
                adChannelPlayers.remove(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("已取消觀看 ")
                                .append(Component.text("設施宣傳頻道(ad)").color(NamedTextColor.AQUA))
                );
            }
        }
        if(Objects.equals(args[0], "qa") || Objects.equals(args[0], "q")){
            if(!qaChannelPlayers.contains(sender)){
                qaChannelPlayers.add(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("觀看 ")
                                .append(Component.text("問答頻道(qa)").color(NamedTextColor.BLUE))
                                .append(Component.text(" 中"))
                );
            }else{
                qaChannelPlayers.remove(((Player) sender).getPlayer());
                sender.sendMessage(
                        Component.text("已取消觀看 ")
                                .append(Component.text("問答頻道(qa)").color(NamedTextColor.BLUE))
                );
            }
        }
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if (!(sender instanceof Player)) {
            return null;
        }
        if (args.length >= 2) {
            return null;
        }
        List<String> list = new ArrayList<>();
        list.add("trade");
        list.add("t");
        list.add("ad");
        list.add("a");
        list.add("qa");
        list.add("q");
        return list;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        tradeChannelPlayers.add(e.getPlayer());
        adChannelPlayers.add(e.getPlayer());
        qaChannelPlayers.add(e.getPlayer());
    }

}
