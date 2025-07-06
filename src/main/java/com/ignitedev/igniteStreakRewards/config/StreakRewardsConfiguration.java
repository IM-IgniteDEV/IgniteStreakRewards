package com.ignitedev.igniteStreakRewards.config;

import com.ignitedev.igniteStreakRewards.data.StreakReward;
import com.twodevsstudio.simplejsonconfig.api.Config;
import com.twodevsstudio.simplejsonconfig.interfaces.Comment;
import com.twodevsstudio.simplejsonconfig.interfaces.Configuration;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Configuration("StreakRewardsConfiguration.json")
@Getter
public class StreakRewardsConfiguration extends Config {

  private String mongoDbConnectionAddress = "mongodb://localhost:27017";
  private String mongoDbDatabaseName = "streakRewards";

  private String waitingDailyReward =
      "&e☆ &6DailyRewards&e ☆&c Daily reward is waiting! Use /daily";
  private String rewardNotAvailable = "&e☆ &6DailyRewards&e ☆&c This reward is not available!";
  private String rewardAlreadyClaimed = "&e☆ &6DailyRewards&e ☆&c This reward is already claimed!";
  private String rewardCorrupted =
      "&e☆ &6DailyRewards&e ☆&c Reward corrupted, Please contact administrator!";

  private Map<Integer, ItemStack> dailyInventory = dailyInventoryExample();

  @Comment(
      "do not replace {number} with anything, tag need this! Tag is to recognize item day, it can be put in lore or name")
  private String dailyGuiTag = "#{number}";

  private String dailyGuiName = "&e☆ &6DailyRewards&e ☆";

  @Comment("If reward is available, it will get this enchant")
  private String dailyGuiRewardsAvailableEnchant = "DURABILITY:1";

  private int dailyGuiSize = 27;

  @Comment("key is period ( how many logins to acquire ), value is reward")
  private Map<Integer, StreakReward> uniqueLoginRewards = uniqueLoginRewardsExample();

  @Comment("key is minutes ( how many minutes to acquire ), value is reward")
  private Map<Integer, StreakReward> milestoneRewards = milestoneRewardsExample();

  @Comment("key is streak day, value is reward")
  private Map<Integer, StreakReward> dailyRewards = dailyRewardsExample();

  @Comment("In Ticks, 20 = 1 sec")
  private int waitingProcessDelayOnPlayerJoin = 20;

  private Map<Integer, StreakReward> uniqueLoginRewardsExample() {
    Map<Integer, StreakReward> rewards = new HashMap<>();

    rewards.put(
        100,
        new StreakReward(
            List.of(new ItemStack(Material.DIAMOND_PICKAXE, 1)),
            List.of("broadcast Congratulations {PLAYER} you have 100 logins!"),
            List.of("One of that {PLAYER}", "commands", "will be", "selected", "randomly"),
            List.of("You are 100th player joining server!")));

    return rewards;
  }

  private Map<Integer, StreakReward> milestoneRewardsExample() {
    Map<Integer, StreakReward> rewards = new HashMap<>();

    rewards.put(
        1440,
        new StreakReward(
            List.of("say You got one day in streak (1440 minutes is one day)"),
            List.of(new ItemStack(Material.DIAMOND_PICKAXE, 1)),
            List.of("Reward message"),
            List.of("One of that", "commands", "will be", "selected", "randomly")));

    return rewards;
  }

  private Map<Integer, StreakReward> dailyRewardsExample() {
    Map<Integer, StreakReward> rewards = new HashMap<>();

    rewards.put(
        1,
        new StreakReward(
            List.of("say reward acquired"),
            List.of(new ItemStack(Material.DIAMOND_PICKAXE, 1)),
            List.of("Reward message"),
            List.of("One of that", "commands", "will be", "selected", "randomly")));
    rewards.put(
        2,
        new StreakReward(
            List.of("say reward acquired nr 2"), null, List.of("Reward 2 message"), null));
    rewards.put(3, new StreakReward(List.of("say reward acquired nr 3"), null, null, null));

    return rewards;
  }

  private Map<Integer, ItemStack> dailyInventoryExample() {
    Map<Integer, ItemStack> inventory = new HashMap<>();

    inventory.put(
        0,
        ItemUtility.getItemStackWithName(
            new ItemStack(Material.DIAMOND_SWORD, 1), "&e☆ &6Day #1&e ☆"));
    inventory.put(
        1,
        ItemUtility.getItemStackWithName(
            new ItemStack(Material.DIAMOND_PICKAXE, 1), "&e☆ &6Day #2&e ☆"));
    inventory.put(
        2,
        ItemUtility.getItemStackWithName(
            new ItemStack(Material.DIAMOND_AXE, 1), "&e☆ &6Day #3&e ☆"));

    return inventory;
  }
}
