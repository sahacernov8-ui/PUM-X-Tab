package ru.pumx.tab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PUMXTab extends JavaPlugin implements Listener {

    private static final String ICON_OWNER = "\uE000";
    private static final String ICON_ADMIN = "\uE001";
    private static final String ICON_MODERATOR = "\uE002";
    private static final String ICON_PLAYER = "\uE004";
    private static final String ICON_PING = "\uE005";

    @Override
    public void onEnable() {

        getLogger().info("PUM-X TAB enabled!");

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTaskTimer(
                this,
                this::updateTab,
                0L,
                20L
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("PUM-X TAB disabled!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateTab();
    }

    private void updateTab() {

        List<Player> players =
                new ArrayList<>(Bukkit.getOnlinePlayers());

        players.sort(
                Comparator
                        .comparingInt(this::getRankPriority)
                        .thenComparing(Player::getName)
        );

        int online = players.size();
        int max = Bukkit.getMaxPlayers();

        for (Player viewer : players) {

            StringBuilder header = new StringBuilder();

            header.append("\n");

            header.append(ChatColor.GOLD)
                    .append("✦ ");

            header.append(ChatColor.WHITE)
                    .append(ChatColor.BOLD)
                    .append("PUM-X");

            header.append(ChatColor.GOLD)
                    .append(" ✦\n");

            header.append(ChatColor.GRAY)
                    .append("ИГРАЙ • ОБЩАЙСЯ • РАЗВИВАЙСЯ")
                    .append("\n\n");

            header.append(ChatColor.DARK_GRAY)
                    .append("╭────────────────────────╮")
                    .append("\n");

            header.append(ChatColor.WHITE)
                    .append(ChatColor.BOLD)
                    .append("     ONLINE  ");

            header.append(ChatColor.GREEN)
                    .append(online);

            header.append(ChatColor.GRAY)
                    .append(" / ")
                    .append(max)
                    .append("\n");

            header.append(ChatColor.DARK_GRAY)
                    .append("╰────────────────────────╯")
                    .append("\n\n");

            viewer.setPlayerListHeader(header.toString());

            viewer.setPlayerListFooter(
                    "\n" +
                    ChatColor.DARK_GRAY +
                    "────────────────────────" +
                    "\n" +
                    ChatColor.GOLD +
                    "✦ " +
                    ChatColor.WHITE +
                    ChatColor.BOLD +
                    "PUM-X" +
                    ChatColor.GOLD +
                    " ✦" +
                    "\n" +
                    ChatColor.GRAY +
                    "Minecraft 1.21.5"
            );

            updatePlayers(viewer, players);
        }
    }

    private void updatePlayers(
            Player viewer,
            List<Player> players
    ) {

        for (Player target : players) {

            String prefix = getPrefix(target);

            String ping =
                    ChatColor.DARK_GRAY +
                    ICON_PING +
                    " " +
                    ChatColor.GRAY +
                    target.getPing() +
                    "ms";

            viewer.getPlayerListHeader();

            target.setPlayerListName(
                    prefix +
                    ChatColor.WHITE +
                    target.getName() +
                    " " +
                    ping
            );
        }
    }

    private String getPrefix(Player player) {

        if (player.hasPermission("pumx.owner")) {

            return ChatColor.GOLD +
                    ICON_OWNER +
                    " " +
                    ChatColor.BOLD +
                    "[ВЛАДЕЛЕЦ] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.admin")) {

            return ChatColor.RED +
                    ICON_ADMIN +
                    " " +
                    ChatColor.BOLD +
                    "[АДМИН] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.moderator")) {

            return ChatColor.BLUE +
                    ICON_MODERATOR +
                    " " +
                    ChatColor.BOLD +
                    "[МОДЕРАТОР] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.captain")) {

            return ChatColor.DARK_AQUA +
                    "★ " +
                    ChatColor.BOLD +
                    "[КАПИТАН] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.lieutenant")) {

            return ChatColor.AQUA +
                    "★ " +
                    ChatColor.BOLD +
                    "[ЛЕЙТЕНАНТ] " +
                    ChatColor.RESET;
        }

        return ChatColor.GRAY +
                ICON_PLAYER +
                " " +
                "[ИГРОК] " +
                ChatColor.RESET;
    }

    private int getRankPriority(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return 1;
        }

        if (player.hasPermission("pumx.admin")) {
            return 2;
        }

        if (player.hasPermission("pumx.moderator")) {
            return 3;
        }

        if (player.hasPermission("pumx.captain")) {
            return 4;
        }

        if (player.hasPermission("pumx.lieutenant")) {
            return 5;
        }

        return 99;
    }
}
