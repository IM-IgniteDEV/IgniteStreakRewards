package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.igniteStreakRewards.IgniteStreakRewards;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

  private final StreakPlayerRepository repository;
  private final IgniteStreakRewards plugin;

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    StreakPlayer streakPlayer = repository.findOrCreateByUUID(player.getUniqueId());

    streakPlayer.checkLoginStreak(plugin);
    streakPlayer.setActivity();

    if (!repository.exists(player.getUniqueId())) {
      streakPlayer.addUniqueLoginStreak(plugin);
    }
  }
}
