package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

  private final StreakPlayerRepository repository;

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    repository.save(repository.findOrCreateByUUID(event.getPlayer().getUniqueId()));
  }
}
