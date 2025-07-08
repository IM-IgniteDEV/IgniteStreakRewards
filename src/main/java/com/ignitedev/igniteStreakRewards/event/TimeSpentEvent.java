package com.ignitedev.igniteStreakRewards.event;

import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/*
 * This event is called every time when player spends any time specified in the configuration
 */
@RequiredArgsConstructor
@Getter
public class TimeSpentEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final StreakPlayer streakPlayer;
  private final int time;

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
