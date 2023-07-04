package thedavid.territoryconquestchat;

import com.loohp.interactivechat.InteractiveChat;
import com.loohp.interactivechat.api.InteractiveChatAPI;
import com.loohp.interactivechat.utils.ChatColorUtils;
import com.loohp.interactivechatdiscordsrvaddon.InteractiveChatDiscordSrvAddon;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Territory_Conquest_Chat extends JavaPlugin implements Listener, CommandExecutor {
    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);
    public static Set<Player> tradeChannelPlayers = new HashSet<>();
    public static Set<Player> adChannelPlayers = new HashSet<>();
    public static Set<Player> qaChannelPlayers = new HashSet<>();
    public static Map<UUID, Integer> globalChannelPlayersTime = new HashMap<>();
    public static Map<UUID, Integer> tradeChannelPlayersTime = new HashMap<>();
    public static Map<UUID, Integer> adChannelPlayersTime = new HashMap<>();
    public static Map<UUID, Integer> qaChannelPlayersTime = new HashMap<>();

    static JavaPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);
        DiscordSRV.api.subscribe(discordsrvListener);
        Objects.requireNonNull(Bukkit.getPluginCommand("channel")).setExecutor(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    if(!Objects.equals(tradeChannelPlayersTime.get(player.getUniqueId()),null) && tradeChannelPlayersTime.get(player.getUniqueId()) >= 0){
                        tradeChannelPlayersTime.put(player.getUniqueId(),tradeChannelPlayersTime.get(player.getUniqueId())-1);
                    }
                    if(!Objects.equals(adChannelPlayersTime.get(player.getUniqueId()),null) && adChannelPlayersTime.get(player.getUniqueId()) >= 0){
                        adChannelPlayersTime.put(player.getUniqueId(),adChannelPlayersTime.get(player.getUniqueId())-1);
                    }
                    if(!Objects.equals(qaChannelPlayersTime.get(player.getUniqueId()),null) && qaChannelPlayersTime.get(player.getUniqueId()) >= 0){
                        qaChannelPlayersTime.put(player.getUniqueId(),qaChannelPlayersTime.get(player.getUniqueId())-1);
                    }
                    if(!Objects.equals(globalChannelPlayersTime.get(player.getUniqueId()),null) && globalChannelPlayersTime.get(player.getUniqueId()) >= 0){
                        globalChannelPlayersTime.put(player.getUniqueId(),globalChannelPlayersTime.get(player.getUniqueId())-1);
                    }
                }
            }
        }.runTaskTimer(plugin, 0,20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }
    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
    public void onChat(AsyncChatEvent e){
        if(e.message().equals(Component.text())){
            e.setCancelled(true);
            return;
        }
        String chatText = PlainTextComponentSerializer.plainText().serialize(e.message());
        if(chatText.equals("")){
            e.setCancelled(true);
            return;
        }
        String gson;
        Component nC = e.message();
        if(!e.getPlayer().hasPermission("tcc.color")){
            nC = nC.color(NamedTextColor.WHITE);
        }
        if(!e.getPlayer().hasPermission("tcc.decoration")){
            nC = nC.decoration(TextDecoration.BOLD,false)
                    .decoration(TextDecoration.UNDERLINED,false)
                    .decoration(TextDecoration.STRIKETHROUGH,false)
                    .decoration(TextDecoration.ITALIC,false)
                    .decoration(TextDecoration.OBFUSCATED,false);
        }
//        if(e.getPlayer().hasPermission("tcc.mini")){
//            MiniMessage mm = MiniMessage.miniMessage();
//            nC = mm.deserialize(chatText);
//        }
        gson = GsonComponentSerializer.gson().serialize(
                nC
        );
        Bukkit.getLogger().info(e.getPlayer().getName() + " > " + chatText);
        String prefix = PlaceholderAPI.setPlaceholders(e.getPlayer(), "%vault_prefix%");
        String coloredPrefix = ChatColorUtils.translateAlternateColorCodes('&',prefix);
        String painPrefix = prefix
                .replace("&0","")
                .replace("&1","")
                .replace("&2","")
                .replace("&3","")
                .replace("&4","")
                .replace("&5","")
                .replace("&6","")
                .replace("&7","")
                .replace("&8","")
                .replace("&9","")
                .replace("&a","")
                .replace("&b","")
                .replace("&c","")
                .replace("&d","")
                .replace("&e","")
                .replace("&f","")
                .replace("&k","")
                .replace("&l","")
                .replace("&m","")
                .replace("&n","")
                .replace("&o","")
                .replace("&r","");
        if(chatText.charAt(0) == '$'){
            if(Objects.equals(tradeChannelPlayersTime.get(e.getPlayer().getUniqueId()),null) || tradeChannelPlayersTime.get(e.getPlayer().getUniqueId()) <= 0){
                String changed = gson.replaceFirst("\\$","");
                Component changedC = GsonComponentSerializer.gson().deserialize(changed);
                String changedP = PlainTextComponentSerializer.plainText().serialize(changedC);
                TextChannel dev = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("dev");
                InteractiveChatDiscordSrvAddon.discordsrv.processChatMessage(e.getPlayer(),changedP,"trade",false,e);
                DiscordUtil.queueMessage(dev,"trade" + "|" + coloredPrefix + "|" + e.getPlayer().getName() + "|" + changed);
                if(!tradeChannelPlayers.contains(e.getPlayer())){
                    tradeChannelPlayers.add(e.getPlayer());
                    e.getPlayer().sendMessage(
                            Component.text("觀看 ")
                                    .append(Component.text("交易頻道(trade)").color(NamedTextColor.GOLD))
                                    .append(Component.text("中"))
                    );
                }
                sendChannel("trade",e.getPlayer().getName(), changedC, coloredPrefix);
                tradeChannelPlayersTime.put(e.getPlayer().getUniqueId(), 300);
                e.setCancelled(true);
            }else{
                e.getPlayer().sendMessage(
                        Component.text("需等待 ").color(TextColor.color(NamedTextColor.RED))
                                .append(Component.text(tradeChannelPlayersTime.get(e.getPlayer().getUniqueId())))
                                .append(Component.text(" 秒後才可再次在此頻道發送訊息"))
                );
                e.setCancelled(true);
            }
        }else if(chatText.charAt(0) == '!'){
            if(Objects.equals(adChannelPlayersTime.get(e.getPlayer().getUniqueId()),null) || adChannelPlayersTime.get(e.getPlayer().getUniqueId()) <= 0){
                String changed = gson.replaceFirst("!","");
                Component changedC = GsonComponentSerializer.gson().deserialize(changed);
                String changedP = PlainTextComponentSerializer.plainText().serialize(changedC);
                TextChannel dev = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("dev");
                InteractiveChatDiscordSrvAddon.discordsrv.processChatMessage(e.getPlayer(),changedP,"ad",false,e);
                DiscordUtil.queueMessage(dev,"ad" + "|" + coloredPrefix + "|" + e.getPlayer().getName() + "|" + changed);
                if(!adChannelPlayers.contains(e.getPlayer())){
                    adChannelPlayers.add(e.getPlayer());
                    e.getPlayer().sendMessage(
                            Component.text("觀看 ")
                                    .append(Component.text("設施宣傳頻道(ad)").color(NamedTextColor.AQUA))
                                    .append(Component.text("中"))
                    );
                }
                sendChannel("ad",e.getPlayer().getName(), changedC, coloredPrefix);
                adChannelPlayersTime.put(e.getPlayer().getUniqueId(), 300);
                e.setCancelled(true);
            }else{
                e.getPlayer().sendMessage(
                        Component.text("需等待 ").color(TextColor.color(NamedTextColor.RED))
                                .append(Component.text(adChannelPlayersTime.get(e.getPlayer().getUniqueId())))
                                .append(Component.text(" 秒後才可再次在此頻道發送訊息"))
                );
                e.setCancelled(true);
            }
//        }else if(chatText.charAt(0) == '?'){
//            if(Objects.equals(qaChannelPlayersTime.get(e.getPlayer().getUniqueId()),null) || qaChannelPlayersTime.get(e.getPlayer().getUniqueId()) <= 0){
//                String changed = gson.replaceFirst("\\?","");
//                Component changedC = GsonComponentSerializer.gson().deserialize(changed);
//                String changedP = PlainTextComponentSerializer.plainText().serialize(changedC);
//                TextChannel dev = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("dev");
//                InteractiveChatDiscordSrvAddon.discordsrv.processChatMessage(e.getPlayer(),changedP,"qa",false,e);
//                DiscordUtil.queueMessage(dev,"qa" + "|" + coloredPrefix + "|" + e.getPlayer().getName() + "|" + changed);
//                if(!qaChannelPlayers.contains(e.getPlayer())){
//                    qaChannelPlayers.add(e.getPlayer());
//                    e.getPlayer().sendMessage(
//                            Component.text("觀看 ")
//                                    .append(Component.text("問答頻道(qa)").color(NamedTextColor.BLUE))
//                                    .append(Component.text("中"))
//                    );
//                }
//                sendChannel("qa",e.getPlayer().getName(), changedC, coloredPrefix);
//                qaChannelPlayersTime.put(e.getPlayer().getUniqueId(), 3);
//                e.setCancelled(true);
//            }else{
//                e.getPlayer().sendMessage(
//                        Component.text("需等待 ").color(TextColor.color(NamedTextColor.RED))
//                                .append(Component.text(qaChannelPlayersTime.get(e.getPlayer().getUniqueId())))
//                                .append(Component.text(" 秒後才可再次在此頻道發送訊息"))
//                );
//                e.setCancelled(true);
//            }
        }else{
            if(Objects.equals(globalChannelPlayersTime.get(e.getPlayer().getUniqueId()),null) || globalChannelPlayersTime.get(e.getPlayer().getUniqueId()) <= 0){
                TextChannel dev = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("dev");
//                InteractiveChatDiscordSrvAddon.discordsrv.processChatMessage(e.getPlayer(),chatText,"global",false,e);
                DiscordUtil.queueMessage(dev,"global" + "|" + coloredPrefix + "|" + e.getPlayer().getName() + "|" + gson);
                sendChannel("global",e.getPlayer().getName(), GsonComponentSerializer.gson().deserialize(gson), coloredPrefix);
                globalChannelPlayersTime.put(e.getPlayer().getUniqueId(), 3);
                ableToSend.put(e.getPlayer(),true);
                e.setCancelled(true);
            }else{
                e.getPlayer().sendMessage(
                        Component.text("需等待 ").color(TextColor.color(NamedTextColor.RED))
                                .append(Component.text(globalChannelPlayersTime.get(e.getPlayer().getUniqueId())))
                                .append(Component.text(" 秒後才可再次在此頻道發送訊息"))
                );
                ableToSend.put(e.getPlayer(),false);
                e.setCancelled(true);
            }
        }
    }
    public static Map<Player, Boolean> ableToSend = new HashMap<>();
    public static void sendChannel(String channel, String sendPlayer, Component message, String prefix){
        if(Objects.equals(channel, "trade")){
            for(Player player : tradeChannelPlayers){
                player.sendMessage(
                        Component.text("").color(NamedTextColor.WHITE)
                                .append(Component.text("$ ").color(NamedTextColor.GOLD))
                                .append(Component.text(prefix))
                                .append(Component.text(sendPlayer).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(message)
                );
            }
        }else if(Objects.equals(channel, "ad")){
            for(Player player : adChannelPlayers){
                player.sendMessage(
                        Component.text("").color(NamedTextColor.WHITE)
                                .append(Component.text("! ").color(NamedTextColor.AQUA))
                                .append(Component.text(prefix))
                                .append(Component.text(sendPlayer).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(message)
                );
            }
//        }else if(Objects.equals(channel, "qa")){
//            for(Player player : qaChannelPlayers){
//                player.sendMessage(
//                        Component.text("").color(NamedTextColor.WHITE)
//                                .append(Component.text("? ").color(NamedTextColor.BLUE))
//                                .append(Component.text(prefix))
//                                .append(Component.text(sendPlayer).color(NamedTextColor.WHITE))
//                                .append(Component.text("> ").color(NamedTextColor.GRAY))
//                                .append(message)
//                );
//            }
        }else if(Objects.equals(channel, "global")){
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(
                        Component.text("").color(NamedTextColor.WHITE)
                                .append(Component.text(prefix))
                                .append(Component.text(sendPlayer).color(NamedTextColor.WHITE))
                                .append(Component.text("> ").color(NamedTextColor.GRAY))
                                .append(message)
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
//        if(Objects.equals(args[0], "qa") || Objects.equals(args[0], "q")){
//            if(!qaChannelPlayers.contains(sender)){
//                qaChannelPlayers.add(((Player) sender).getPlayer());
//                sender.sendMessage(
//                        Component.text("觀看 ")
//                                .append(Component.text("問答頻道(qa)").color(NamedTextColor.BLUE))
//                                .append(Component.text(" 中"))
//                );
//            }else{
//                qaChannelPlayers.remove(((Player) sender).getPlayer());
//                sender.sendMessage(
//                        Component.text("已取消觀看 ")
//                                .append(Component.text("問答頻道(qa)").color(NamedTextColor.BLUE))
//                );
//            }
//        }
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
