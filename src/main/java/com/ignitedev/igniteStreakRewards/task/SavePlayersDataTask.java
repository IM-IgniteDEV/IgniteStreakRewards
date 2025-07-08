package com.ignitedev.igniteStreakRewards.task;

import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class SavePlayersDataTask extends BukkitRunnable {

  @Autowired
  private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @Override
  public void run() {
    repository.getPlayersCache().values().forEach(repository::save);
    Bukkit.broadcastMessage(
        TextUtility.colorize(configuration.getPrefix() + configuration.getAutoSaveMessage()));
  }
}
