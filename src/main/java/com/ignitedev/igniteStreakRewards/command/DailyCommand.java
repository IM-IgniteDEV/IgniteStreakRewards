package com.ignitedev.igniteStreakRewards.command;

import com.ignitedev.igniteStreakRewards.gui.DailyInventory;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DailyCommand implements CommandExecutor {

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
    new DailyInventory(repository).openInventory((Player) sender);
    return true;
  }
}
