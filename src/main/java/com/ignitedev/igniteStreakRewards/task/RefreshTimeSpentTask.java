package com.ignitedev.igniteStreakRewards.task;

import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.event.TimeSpentEvent;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class RefreshTimeSpentTask extends BukkitRunnable {

  @Autowired
  private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @Override
  public void run() {
    for (StreakPlayer streakPlayer : repository.getPlayersCache().values()) {
      Player player = streakPlayer.getCachedPlayer();

      if (player == null || !player.isOnline()) {
        continue;
      }
      streakPlayer.setTotalTimeSpend(streakPlayer.getTotalTimeSpend() + 1);
      int rewardTime = 0;

      for (Integer integer : configuration.getMilestoneRewards().keySet()) {
        if (streakPlayer.getTotalTimeSpend() == integer) {
          rewardTime = integer;
          break;
        }
      }
      if (rewardTime == 0) {
        continue;
      }
      Bukkit.getPluginManager().callEvent(new TimeSpentEvent(streakPlayer, streakPlayer.getTotalTimeSpend()));
    }
  }
}
