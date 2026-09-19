package ru.pumx.tab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class PUMXTab extends JavaPlugin implements Listener {

    // Иконки из Resource Pack
    private static final String ICON_CROWN = "\uE000";
    private static final String ICON_SHIELD = "\uE001";
    private static final String ICON_SWORD = "\uE002";
    private static final String ICON_STAR = "\uE003";
    private static final String ICON_PLAYER = "\uE004";
    private static final String ICON_PING = "\uE005";

    @Override
    public void onEnable() {

        getLogger().info("PUM-X Tab enabled!");

        Bukkit.getPluginManager().registerEvents(this, this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            setupPlayer(player);
        }

        Bukkit.getScheduler().runTaskTimer(
                this,
                this::updateTab,
                0L,
                20L
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("PUM-X Tab disabled.");
    }

    // =========================
    // ИГРОК ЗАШЁЛ
    // =========================

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setupPlayer(event.getPlayer());
    }

    // =========================
    // ИГРОК ВЫШЕЛ
    // =========================

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    // =========================
    // ОБНОВЛЕНИЕ TAB
    // =========================

    private void updateTab() {

        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        for (Player player : Bukkit.getOnlinePlayers()) {

            String header =
                    "\n" +
                    ChatColor.GOLD + "✦ " +
                    ChatColor.WHITE + ChatColor.BOLD + "PUM-X" +
                    ChatColor.GOLD + " ✦\n" +

                    ChatColor.GRAY +
                    "ИГРАЙ • ОБЩАЙСЯ • РАЗВИВАЙСЯ\n\n" +

                    ChatColor.DARK_GRAY +
                    "━━━━━━━━━━━━━━━━━━━━\n" +

                    ChatColor.WHITE +
                    ICON_PLAYER +
                    "  ОНЛАЙН  " +

                    ChatColor.GREEN +
                    ChatColor.BOLD +
                    online +

                    ChatColor.DARK_GRAY +
                    " / " +

                    ChatColor.GRAY +
                    max +

                    "\n" +

                    ChatColor.DARK_GRAY +
                    "━━━━━━━━━━━━━━━━━━━━\n";

            String footer =
                    "\n" +
                    ChatColor.GOLD +
                    "✦ " +

                    ChatColor.WHITE +
                    ChatColor.BOLD +
                    "ВМЕСТЕ ДЕЛАЕМ ЭТОТ МИР ЛУЧШЕ!" +

                    ChatColor.GOLD +
                    " ✦\n" +

                    ChatColor.GRAY +
                    "PUM-X • Minecraft 1.21.5";

            player.setPlayerListHeaderFooter(header, footer);

            updatePlayerDisplay(player);
        }
    }

    // =========================
    // ОТОБРАЖЕНИЕ ИГРОКА
    // =========================

    private void updatePlayerDisplay(Player player) {

        String prefix = getRankPrefix(player);

        player.setPlayerListName(
                prefix +
                ChatColor.WHITE +
                player.getName()
        );

        setupTeam(player);
    }

    // =========================
    // TEAM
    // =========================

    private void setupPlayer(Player player) {

        setupTeam(player);
        updatePlayerDisplay(player);
    }

    private void setupTeam(Player player) {

        Scoreboard scoreboard =
                Bukkit.getScoreboardManager().getMainScoreboard();

        String teamName = getTeamName(player);

        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setPrefix(getRankPrefix(player));

        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }
    }

    // =========================
    // УДАЛЕНИЕ
    // =========================

    private void removePlayer(Player player) {

        Scoreboard scoreboard =
                Bukkit.getScoreboardManager().getMainScoreboard();

        for (Team team : scoreboard.getTeams()) {

            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    // =========================
    // РАНГ + ИКОНКА
    // =========================

    private String getRankPrefix(Player player) {

        if (player.hasPermission("pumx.owner")) {

            return ChatColor.GOLD +
                    ICON_CROWN +
                    " " +
                    ChatColor.BOLD +
                    "[ВЛАДЕЛЕЦ] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.admin")) {

            return ChatColor.RED +
                    ICON_SHIELD +
                    " " +
                    ChatColor.BOLD +
                    "[АДМИН] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.moderator")) {

            return ChatColor.BLUE +
                    ICON_SWORD +
                    " " +
                    ChatColor.BOLD +
                    "[МОДЕРАТОР] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.captain")) {

            return ChatColor.DARK_AQUA +
                    ICON_STAR +
                    " " +
                    ChatColor.BOLD +
                    "[КАПИТАН] " +
                    ChatColor.RESET;
        }

        if (player.hasPermission("pumx.lieutenant")) {

            return ChatColor.AQUA +
                    ICON_STAR +
                    " " +
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

    // =========================
    // ПОРЯДОК РАНГОВ
    // =========================

    private String getTeamName(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return "01_owner";
        }

        if (player.hasPermission("pumx.admin")) {
            return "02_admin";
        }

        if (player.hasPermission("pumx.moderator")) {
            return "03_moderator";
        }

        if (player.hasPermission("pumx.captain")) {
            return "04_captain";
        }

        if (player.hasPermission("pumx.lieutenant")) {
            return "05_lieutenant";
        }

        return "99_player";
    }
}
