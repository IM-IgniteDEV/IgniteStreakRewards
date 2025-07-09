package com.ignitedev.igniteStreakRewards.repository;

import com.ignitedev.aparecium.database.SimpleMongo;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.mongodb.client.MongoCollection;
import com.twodevsstudio.simplejsonconfig.def.Serializer;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class StreakPlayerRepository {

  @Getter private final Map<UUID, StreakPlayer> playersCache = new ConcurrentHashMap<>();

  private final SimpleMongo simpleMongo;

  @NotNull
  public StreakPlayer findOrCreateByUUID(UUID uuid) {
    StreakPlayer byUUID = findByUUID(uuid);

    if (byUUID == null) {
      StreakPlayer StreakPlayer = new StreakPlayer(uuid, new ArrayList<>());
      cache(StreakPlayer);
      save(StreakPlayer);
      return StreakPlayer;
    } else {
      return byUUID;
    }
  }

  @Nullable
  private StreakPlayer findByUUID(UUID uuid) {
    StreakPlayer fromCache = playersCache.get(uuid);
    if (fromCache == null) {
      StreakPlayer first =
          simpleMongo
              .get()
              .getObjectSync(
                  simpleMongo.getDatabase().getCollection("players"),
                  new Document("_uuid", uuid.toString()))
              .map(
                  document ->
                      Serializer.getInst()
                          .getGson()
                          .fromJson(((String) document.get("data")), StreakPlayer.class))
              .first();
      if (first != null) {
        cache(first);
      }
      return first;
    }
    return fromCache;
  }

  public void cache(StreakPlayer player) {
    playersCache.put(player.getUuid(), player);
  }

  public void save(StreakPlayer player) {
    MongoCollection<Document> players = simpleMongo.getDatabase().getCollection("players");
    Document identifier = new Document("_uuid", player.getUuid().toString());

    simpleMongo
        .save()
        .saveObjectAsync(
            players,
            identifier,
            new Document("data", Serializer.getInst().getGson().toJson(player)));
  }

  public boolean exists(@NotNull UUID uuid) {
    return simpleMongo
            .get()
            .getObjectSync(
                simpleMongo.getDatabase().getCollection("players"),
                new Document("_uuid", uuid.toString()))
            .first()
        != null;
  }
}
