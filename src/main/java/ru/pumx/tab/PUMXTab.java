package ru.pumx.tab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class PUMXTab extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PUM-X Tab enabled!");

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

    private void updateTab() {

        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        for (Player player : Bukkit.getOnlinePlayers()) {

            String header =
                    "\n" +
                    ChatColor.GOLD + "✦ " +
                    ChatColor.WHITE + "" + ChatColor.BOLD + "PUM-X" +
                    ChatColor.GOLD + " ✦\n" +
                    ChatColor.GRAY + "ИГРАЙ • ОБЩАЙСЯ • РАЗВИВАЙСЯ\n" +
                    ChatColor.DARK_GRAY + "Онлайн: " +
                    ChatColor.GREEN + online +
                    ChatColor.GRAY + "/" +
                    ChatColor.GREEN + max +
                    "\n";

            String footer =
                    "\n" +
                    ChatColor.GOLD + "✦ " +
                    ChatColor.WHITE + "ВМЕСТЕ ДЕЛАЕМ ЭТОТ МИР ЛУЧШЕ!" +
                    ChatColor.GOLD + " ✦\n" +
                    ChatColor.GRAY + "Minecraft 1.21.5\n";

            player.setPlayerListHeaderFooter(header, footer);
        }
    }

    private void setupPlayer(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        String teamName = getTeamName(player);

        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setPrefix(getRankPrefix(player));
        team.addEntry(player.getName());
    }

    private String getRankPrefix(Player player) {

        if (player.hasPermission("pumx.owner")) {
            return ChatColor.GOLD + "[Владелец] " + ChatColor.RESET;
        }

        if (player.hasPermission("pumx.admin")) {
            return ChatColor.RED + "[Админ] " + ChatColor.RESET;
        }

        if (player.hasPermission("pumx.moderator")) {
            return ChatColor.BLUE + "[Модератор] " + ChatColor.RESET;
        }

        if (player.hasPermission("pumx.captain")) {
            return ChatColor.DARK_AQUA + "[Капитан] " + ChatColor.RESET;
        }

        if (player.hasPermission("pumx.lieutenant")) {
            return ChatColor.AQUA + "[Лейтенант] " + ChatColor.RESET;
        }

        return ChatColor.GRAY + "[Игрок] " + ChatColor.RESET;
    }

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
