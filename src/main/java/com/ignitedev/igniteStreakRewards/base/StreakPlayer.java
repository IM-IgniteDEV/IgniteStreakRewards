package com.ignitedev.igniteStreakRewards.base;

import com.ignitedev.igniteStreakRewards.IgniteStreakRewards;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.event.LoginStreakEvent;
import com.ignitedev.igniteStreakRewards.event.UniqueJoinEvent;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Data
public class StreakPlayer {

  @Autowired
  private static StreakRewardsConfiguration configuration;

  private final UUID uuid;
  private final List<Integer> rewardsToGrab;

  private int loginStreak; // 1-30
  private int totalTimeSpend; // in minutes
  private long lastActivityTime = 0;

  private transient Player cachedPlayer;

  // should be executed when login
  public void checkLoginStreak(IgniteStreakRewards plugin) {
    // if you don't have any data about last activity then your streak is equal to 1
    if (lastActivityTime == 0) {
      loginStreak = 1;
      Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new LoginStreakEvent(this, loginStreak)));

      if (!rewardsToGrab.contains(loginStreak)) {
        rewardsToGrab.add(loginStreak);
      }
      return;
    }
    LocalDateTime localDateTimeNow = LocalDateTime.now();
    LocalDateTime localDateTimeLastActivity = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastActivityTime), ZoneId.systemDefault());

    // if days are different
    if (localDateTimeLastActivity.getDayOfYear() != localDateTimeNow.getDayOfYear()) {
      // if last activity is not the same day as current
      int lastActivity = localDateTimeLastActivity.plusDays(1).getDayOfYear();
      int now = localDateTimeNow.getDayOfYear();

      if (lastActivity == now) {
        // if saved activity (+ one day) is the same day
        // we will display streak even bigger than 30 but rewards will be only duo to 30
        loginStreak = loginStreak == 30 ? 30 : loginStreak + 1;

        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new LoginStreakEvent(this, loginStreak)));

        if (!rewardsToGrab.contains(loginStreak)) {
          rewardsToGrab.add(loginStreak);
        }
      } else {
        loginStreak = 1;
        rewardsToGrab.add(loginStreak);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new LoginStreakEvent(this, loginStreak)));
      }
    }
  }

  // should be executed when login
  public void addUniqueLoginStreak(IgniteStreakRewards plugin) {
    // cannot call event from another thread
    StreakPlayer StreakPlayer = this;
    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new UniqueJoinEvent(StreakPlayer, Bukkit.getOfflinePlayers().length)));
  }

  public Player getCachedPlayer() {
    if (cachedPlayer == null) {
      setCachedPlayer();
    }
    if (cachedPlayer == null || !cachedPlayer.isOnline()) {
      return null;
    }
    return cachedPlayer;
  }

  public void setCachedPlayer() {
    this.cachedPlayer = Bukkit.getPlayer(this.uuid);
  }

  // should be executed when login
  public void setActivity() {
    this.lastActivityTime = System.currentTimeMillis();
  }

  public void grabReward(int reward) {
    Player player = getCachedPlayer();

    if (player == null) {
      return;
    }
    StreakReward dailyReward = configuration.getDailyRewards().get(Math.min(reward, configuration.getDailyRewards().size())); // If streak is bigger than reward size then anyway you
    // will receive max prize
    dailyReward.grantReward(player);
    player.closeInventory();
  }
}
