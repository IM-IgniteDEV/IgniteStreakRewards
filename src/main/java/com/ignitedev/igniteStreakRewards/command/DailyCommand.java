package com.ignitedev.igniteStreakRewards.command;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.gui.DailyInventory;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DailyCommand implements CommandExecutor {

  @Autowired private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }
    if (!sender.hasPermission("ignitestreakrewards.daily")) {
      MessageUtility.send(
          (Player) sender, configuration.getPrefix() + configuration.getNoPermission());
      return true;
    }
    new DailyInventory(repository).openInventory((Player) sender);
    return true;
  }
}
