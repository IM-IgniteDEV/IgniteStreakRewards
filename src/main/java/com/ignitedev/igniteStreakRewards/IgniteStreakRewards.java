package com.ignitedev.igniteStreakRewards;

import com.ignitedev.aparecium.database.MongoDBConnection;
import com.ignitedev.aparecium.database.SimpleMongo;
import com.ignitedev.igniteStreakRewards.command.DailyCommand;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.listener.*;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.ignitedev.igniteStreakRewards.task.PendingRewardRemainderTask;
import com.ignitedev.igniteStreakRewards.task.RefreshTimeSpentTask;
import com.ignitedev.igniteStreakRewards.task.SavePlayersDataTask;
import com.mongodb.client.model.IndexOptions;
import com.twodevsstudio.simplejsonconfig.SimpleJSONConfig;
import com.twodevsstudio.simplejsonconfig.api.Config;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class IgniteStreakRewards extends JavaPlugin {

  private StreakPlayerRepository streakPlayerRepository;

  @Getter private SimpleMongo simpleMongo;

  @Override
  public void onEnable() {
    SimpleJSONConfig.INSTANCE.register(this);

    StreakRewardsConfiguration configuration = Config.getConfig(StreakRewardsConfiguration.class);

    initializeDatabase(configuration);
    loadListeners(Bukkit.getPluginManager());
    loadCommands();
    loadTasks();
  }

  @Override
  public void onDisable() {
    streakPlayerRepository.getPlayersCache().values().forEach(streakPlayerRepository::save);
  }

  private void loadTasks() {
    RefreshTimeSpentTask refreshTimeSpentTask =
        new RefreshTimeSpentTask(this.streakPlayerRepository);
    PendingRewardRemainderTask pendingRewardRemainderTask =
        new PendingRewardRemainderTask(this.streakPlayerRepository);
    SavePlayersDataTask savePlayersDataTask = new SavePlayersDataTask(this.streakPlayerRepository);

    refreshTimeSpentTask.runTaskTimer(this, 60 * 20, 60 * 20);
    pendingRewardRemainderTask.runTaskTimer(this, 60 * 20, 60 * 20);
    savePlayersDataTask.runTaskTimer(this, 30 * 60 * 20, 30 * 60 * 20);
  }

  private void loadCommands() {
    PluginCommand daily = getCommand("daily");

    if (daily != null) {
      daily.setExecutor(new DailyCommand(this.streakPlayerRepository));
    }
  }

  private void loadListeners(PluginManager pluginManager) {
    pluginManager.registerEvents(new PlayerJoinListener(this.streakPlayerRepository, this), this);
    pluginManager.registerEvents(new PlayerJoinStreakListener(this), this);
    pluginManager.registerEvents(new PlayerQuitListener(this.streakPlayerRepository), this);
    pluginManager.registerEvents(new PlayerTimeSpendListener(), this);
    pluginManager.registerEvents(new PlayerUniqueJoinListener(), this);
    pluginManager.registerEvents(
        new PlayerInventoryClickListener(this.streakPlayerRepository), this);
  }

  private void initializeDatabase(StreakRewardsConfiguration configuration) {
    MongoDBConnection mongoDBConnection = new MongoDBConnection(configuration.getDatabaseURL());

    this.simpleMongo =
        new SimpleMongo(
            mongoDBConnection, mongoDBConnection.getDatabase(configuration.getDatabaseName()));
    this.streakPlayerRepository = new StreakPlayerRepository(this.simpleMongo);
    this.simpleMongo
        .getDatabase()
        .getCollection("players")
        .createIndex(new Document("_uuid", 1), new IndexOptions().unique(true));
  }
}
