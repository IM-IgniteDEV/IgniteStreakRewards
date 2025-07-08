package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.IgniteStreakRewards;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.event.LoginStreakEvent;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerJoinStreakListener implements Listener {

  @Autowired private static StreakRewardsConfiguration configuration;

  private final IgniteStreakRewards plugin;

  @EventHandler
  public void onJoin(LoginStreakEvent event) {
    StreakPlayer streakPlayer = event.getStreakPlayer();
    Player player = streakPlayer.getCachedPlayer();

    if (player == null) {
      return;
    }
    Bukkit.getScheduler()
        .runTaskLater(
            plugin,
            () -> {
              if (!streakPlayer.getRewardsToGrab().isEmpty()) {
                MessageUtility.send(
                    player, configuration.getPrefix() + configuration.getWaitingDailyReward());
              }
            },
            configuration.getWaitingProcessDelayOnPlayerJoin());
  }
}
