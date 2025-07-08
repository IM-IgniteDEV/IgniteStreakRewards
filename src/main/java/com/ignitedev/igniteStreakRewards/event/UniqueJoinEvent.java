package com.ignitedev.igniteStreakRewards.event;

import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/*
 * This event is executed every join (any protection like delay can be implemented through that event)
 */
@RequiredArgsConstructor
@Getter
public class UniqueJoinEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final StreakPlayer streakPlayer;
  private final int uniqueLogin;

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {

    return HANDLER_LIST;
  }
}
