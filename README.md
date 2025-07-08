# IgniteStreakRewards
Daily Streak Rewards allows you to create rewards which players can grab every day and collect streak for better rewards, it also allows you to create rewards for played time on server as well as reward for specific amount of accounts created.

---

## Features:
* Daily Rewards (Collecting daily rewards in streak)
* Play Time Rewards (Rewards for spent time on server)
* Registered account amount reward (Reward for being, for example, 100th account registered) 
* Rewards as: Commands, Messages, Items, Random Commands
* Configurable Messages, Inventory, Rewards, Rewards conditions. 
* Developer Friendly
* MongoDB support for persistent data storage
* Highly configurable via `StreakRewardsConfiguration.json`

---

## Requirements

- Java 21+
- MongoDB
- PlaceholderAPI (for placeholders)
- Spigot 1.8.8 - 1.21.4

---


## Commands:

- `/daily` — Open daily menu 
  **Permission:** `ignitestreakrewards.daily`


## Preview:
![image](https://user-images.githubusercontent.com/31209154/142352946-6d713607-07a9-4a4e-bfb2-90f1993b94a9.png)
![image](https://user-images.githubusercontent.com/31209154/142353085-850c70f5-64bb-46ed-aeea-8dccb0399c03.png)
![image](https://user-images.githubusercontent.com/31209154/142353153-a421adcc-3394-4cbe-8dd6-fe0f5895e8d4.png)


## Configuration

```jsonc
{
  "databaseURL": "",
  "databaseName": "streaks",
  "prefix": "&e☆ &6DailyRewards&e ☆&c",
  "waitingDailyReward": "Daily reward is waiting! Use /daily",
  "rewardNotAvailable": "This reward is not available!",
  "rewardAlreadyClaimed": "This reward is already claimed!",
  "rewardCorrupted": "Reward corrupted, Please contact administrator!",
  "noPermission": "You do not have permission to do this!",
  "autoSaveMessage": "&aAuto save completed successfully",
  "dailyInventory": {
    "0": {
      "v": 2730,
      "type": "DIAMOND_SWORD",
      "meta": {
        "meta-type": "UNSPECIFIC",
        "display-name": "&e☆ &6Day #1&e ☆"
      }
    },
    "1": {
      "v": 2730,
      "type": "DIAMOND_PICKAXE",
      "meta": {
        "meta-type": "UNSPECIFIC",
        "display-name": "&e☆ &6Day #2&e ☆"
      }
    },
    "2": {
      "v": 2730,
      "type": "DIAMOND_AXE",
      "meta": {
        "meta-type": "UNSPECIFIC",
        "display-name": "&e☆ &6Day #3&e ☆"
      }
    }
  },
  "dailyGuiTag": "#{number}",
  "dailyGuiName": "&e☆ &6DailyRewards&e ☆",
  "dailyGuiRewardsAvailableEnchant": "DURABILITY:1",
  "dailyGuiSize": 27,
  "uniqueLoginRewards": {
    "100": {
      "itemStack": [
        {
          "v": 2730,
          "type": "DIAMOND_PICKAXE"
        }
      ],
      "commands": [
        "broadcast Congratulations {PLAYER} you have 100 logins!"
      ],
      "randomCommands": [
        "One of that {PLAYER}",
        "commands",
        "will be",
        "selected",
        "randomly"
      ],
      "message": [
        "You are 100th player joining server!"
      ]
    }
  },
  "milestoneRewards": {
    "1440": {
      "itemStack": [
        {
          "v": 2730,
          "type": "DIAMOND_PICKAXE"
        }
      ],
      "commands": [
        "say You got one day in streak (1440 minutes is one day)"
      ],
      "randomCommands": [
        "One of that",
        "commands",
        "will be",
        "selected",
        "randomly"
      ],
      "message": [
        "Reward message"
      ]
    }
  },
  "dailyRewards": {
    "1": {
      "itemStack": [
        {
          "v": 2730,
          "type": "DIAMOND_PICKAXE"
        }
      ],
      "commands": [
        "say reward acquired"
      ],
      "randomCommands": [
        "One of that",
        "commands",
        "will be",
        "selected",
        "randomly"
      ],
      "message": [
        "Reward message"
      ]
    },
    "2": {
      "itemStack": null,
      "commands": [
        "say reward acquired nr 2"
      ],
      "randomCommands": null,
      "message": [
        "Reward 2 message"
      ]
    },
    "3": {
      "itemStack": null,
      "commands": [
        "say reward acquired nr 3"
      ],
      "randomCommands": null,
      "message": null
    }
  },
  "waitingProcessDelayOnPlayerJoin": 20
}
```

---

## API Usage

// todo

---

## Available API Methods

// todo

---

## Events

IgniteLeveling exposes several events you can listen to in your plugin:

- `LoginStreakEvent` — fired when a player login and their streak is updated
- `TimeSpentEvent` — fired when a player spends time on the server
- `UniqueJoinEvent` — fired when a player joins the server for the first time

---


## TODO

// todo

