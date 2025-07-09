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
  private static final int MAX_STREAK = 30;

  @Autowired private static StreakRewardsConfiguration configuration;

  private final UUID uuid;
  private final List<Integer> rewardsToGrab;
  private int loginStreak; // 1-30
  private int totalTimeSpend; // in minutes
  private long lastActivityTime;
  private transient Player cachedPlayer;

  public void checkLoginStreak(IgniteStreakRewards plugin) {
    if (isFirstLogin()) {
      handleFirstLogin(plugin);
      return;
    }
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime lastActivity =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(lastActivityTime), ZoneId.systemDefault());

    if (isNewDay(now, lastActivity)) {
      updateLoginStreak(plugin, now, lastActivity);
    }
  }

  private boolean isFirstLogin() {
    return lastActivityTime == 0;
  }

  private void handleFirstLogin(IgniteStreakRewards plugin) {
    loginStreak = 1;
    fireLoginStreakEvent(plugin, loginStreak);
    addRewardIfNotExists(loginStreak);
  }

  private boolean isNewDay(LocalDateTime now, LocalDateTime lastActivity) {
    return lastActivity.getDayOfYear() != now.getDayOfYear();
  }

  private void updateLoginStreak(
      IgniteStreakRewards plugin, LocalDateTime now, LocalDateTime lastActivity) {
    boolean isConsecutiveDay = lastActivity.plusDays(1).getDayOfYear() == now.getDayOfYear();

    if (isConsecutiveDay) {
      incrementStreak(plugin);
    } else {
      resetStreak(plugin);
    }
  }

  private void incrementStreak(IgniteStreakRewards plugin) {
    loginStreak = Math.min(loginStreak + 1, MAX_STREAK);
    fireLoginStreakEvent(plugin, loginStreak);
    addRewardIfNotExists(loginStreak);
  }

  private void resetStreak(IgniteStreakRewards plugin) {
    loginStreak = 1;
    addRewardIfNotExists(loginStreak);
    fireLoginStreakEvent(plugin, loginStreak);
  }

  private void addRewardIfNotExists(int streak) {
    if (!rewardsToGrab.contains(streak)) {
      rewardsToGrab.add(streak);
    }
  }

  private void fireLoginStreakEvent(IgniteStreakRewards plugin, int streak) {
    Bukkit.getScheduler()
        .runTask(
            plugin, () -> Bukkit.getPluginManager().callEvent(new LoginStreakEvent(this, streak)));
  }

  public void addUniqueLoginStreak(IgniteStreakRewards plugin) {
    Bukkit.getScheduler()
        .runTask(
            plugin,
            () ->
                Bukkit.getPluginManager()
                    .callEvent(new UniqueJoinEvent(this, Bukkit.getOfflinePlayers().length)));
  }

  public Player getCachedPlayer() {
    if (cachedPlayer == null) {
      cachedPlayer = Bukkit.getPlayer(uuid);
    }
    return (cachedPlayer != null && cachedPlayer.isOnline()) ? cachedPlayer : null;
  }

  public void setActivity() {
    this.lastActivityTime = System.currentTimeMillis();
  }

  public void grabReward(int reward) {
    Player player = getCachedPlayer();
    if (player == null) return;

    int rewardIndex = Math.min(reward, configuration.getDailyRewards().size() - 1);
    StreakReward dailyReward = configuration.getDailyRewards().get(rewardIndex);

    dailyReward.grantReward(player);
    player.closeInventory();
  }
}
