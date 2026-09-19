package ru.pumx.tab;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PUMXTab extends JavaPlugin implements Listener {

    private static final Key PUMX_FONT = Key.key("pumx", "default");

    private static final String ICON_OWNER = "\uE000";
    private static final String ICON_ADMIN = "\uE001";
    private static final String ICON_MODERATOR = "\uE002";
    private static final String ICON_PLAYER = "\uE004";

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

        updateTab();
    }

    @Override
    public void onDisable() {
        getLogger().info("PUM-X TAB disabled!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(
                this,
                this::updateTab,
                2L
        );
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

        Component header = createHeader(online, max);
        Component footer = createFooter();

        for (Player viewer : players) {
            viewer.sendPlayerListHeaderAndFooter(header, footer);
        }

        updatePlayerNames(players);
    }

    private Component createHeader(int online, int max) {

        Component title = Component.text("PUM-X")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD);

        Component subtitle = Component.text(
                        "ИГРАЙ • ОБЩАЙСЯ • РАЗВИВАЙСЯ"
                )
                .color(NamedTextColor.GRAY);

        Component onlineText = Component.text("ONLINE  ")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD)
                .append(
                        Component.text(String.valueOf(online))
                                .color(NamedTextColor.GREEN)
                )
                .append(
                        Component.text(" / " + max)
                                .color(NamedTextColor.GRAY)
                );

        return Component.text("\n")
                .append(Component.text("✦ ")
                        .color(NamedTextColor.GOLD))
                .append(title)
                .append(Component.text(" ✦\n")
                        .color(NamedTextColor.GOLD))
                .append(subtitle)
                .append(Component.text("\n\n"))
                .append(Component.text("╭────────────────────────╮\n")
                        .color(NamedTextColor.DARK_GRAY))
                .append(onlineText)
                .append(Component.text("\n╰────────────────────────╯\n\n")
                        .color(NamedTextColor.DARK_GRAY));
    }

    private Component createFooter() {

        return Component.text("\n")
                .append(
                        Component.text("✦ ")
                                .color(NamedTextColor.GOLD)
                )
                .append(
                        Component.text("PUM-X")
                                .color(NamedTextColor.WHITE)
                                .decorate(TextDecoration.BOLD)
                )
                .append(
                        Component.text(" ✦\n")
                                .color(NamedTextColor.GOLD)
                )
                .append(
                        Component.text("ВМЕСТЕ ДЕЛАЕМ ЭТОТ МИР ЛУЧШЕ!\n")
                                .color(NamedTextColor.GRAY)
                )
                .append(
                        Component.text("Minecraft 1.21.5")
                                .color(NamedTextColor.DARK_GRAY)
                );
    }

    private void updatePlayerNames(List<Player> players) {

        for (Player player : players) {

            Component icon = Component.text(getRankIcon(player))
                    .font(PUMX_FONT);

            Component prefix = Component.text(getRankName(player))
                    .color(getRankColor(player))
                    .decorate(TextDecoration.BOLD);

            Component name = Component.text(player.getName())
                    .color(NamedTextColor.WHITE);

            player.playerListName(
                    icon
                            .append(Component.text(" "))
                            .append(prefix)
                            .append(Component.text(" "))
                            .append(name)
            );
        }
    }

    private String getRankIcon(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return ICON_OWNER;
        }

        if (player.hasPermission("pumx.admin")) {
            return ICON_ADMIN;
        }

        if (player.hasPermission("pumx.moderator")) {
            return ICON_MODERATOR;
        }

        return ICON_PLAYER;
    }

    private String getRankName(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return "[ВЛАДЕЛЕЦ]";
        }

        if (player.hasPermission("pumx.admin")) {
            return "[АДМИН]";
        }

        if (player.hasPermission("pumx.moderator")) {
            return "[МОДЕРАТОР]";
        }

        if (player.hasPermission("pumx.captain")) {
            return "[КАПИТАН]";
        }

        if (player.hasPermission("pumx.lieutenant")) {
            return "[ЛЕЙТЕНАНТ]";
        }

        return "[ИГРОК]";
    }

    private NamedTextColor getRankColor(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return NamedTextColor.GOLD;
        }

        if (player.hasPermission("pumx.admin")) {
            return NamedTextColor.RED;
        }

        if (player.hasPermission("pumx.moderator")) {
            return NamedTextColor.BLUE;
        }

        if (player.hasPermission("pumx.captain")) {
            return NamedTextColor.DARK_AQUA;
        }

        if (player.hasPermission("pumx.lieutenant")) {
            return NamedTextColor.AQUA;
        }

        return NamedTextColor.GRAY;
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
