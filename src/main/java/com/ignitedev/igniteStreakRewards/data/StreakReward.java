package com.ignitedev.igniteStreakRewards.data;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.aparecium.util.collection.RandomSelector;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

@Data
public class StreakReward {

  private final Random random = new Random();

  private final List<ItemStack> items;
  private final List<String> commands;
  private final List<String> randomCommands;
  private final List<String> message;

  public void grantReward(Player player) {
    if (items != null) {
      items.forEach(itemStack -> player.getInventory().addItem(itemStack));
    }
    if (commands != null) {
      commands.forEach(
          command ->
              Bukkit.dispatchCommand(
                  Bukkit.getConsoleSender(), command.replace("{PLAYER}", player.getName())));
    }
    if (randomCommands != null) {
      RandomSelector<String> uniform = RandomSelector.uniform(randomCommands);
      Bukkit.dispatchCommand(
          Bukkit.getConsoleSender(), uniform.next(random).replace("{PLAYER}", player.getName()));
    }
    if (message != null) {
      message.forEach(
          message -> MessageUtility.send(player, message.replace("{PLAYER}", player.getName())));
    }
  }
}
