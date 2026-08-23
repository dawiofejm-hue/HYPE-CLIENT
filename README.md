# 🚀 HYPE CLIENT - Advanced Minecraft Launcher

**HYPE CLIENT** is a feature-rich Minecraft launcher with cosmetics, Microsoft OAuth, offline mode, and Discord Rich Presence integration.

## ✨ Features

- 🔐 **Microsoft OAuth Authentication** - Login with your Microsoft account
- 👤 **Offline Mode** - Play without an account
- 🎨 **Cosmetics System**
  - 🪶 Wings (5 types: Angel, Demon, Dragon, Fairy, Phoenix)
  - 👑 Hats (5 types: Crown, Wizard Hat, Horns, Halo, Cat Ears)
  - 🐉 Pets (5 types: Wolf, Dragon, Phoenix, Guardian, Knight Horse)
- 📊 **Discord Rich Presence** - Show your status on Discord
- 🎮 **Minecraft Launcher** - Supports 1.20.1 and configurable versions
- 🌐 **Web Dashboard** - Manage cosmetics via web panel
- ⚙️ **REST API** - Full API for cosmetic management

## 🚀 Quick Start

### 1️⃣ Build
```bash
git clone https://github.com/dawiofejm-hue/HYPE-CLIENT.git
cd HYPE-CLIENT
mvn clean package
```

### 2️⃣ Run
```bash
java -jar target/hype-client-1.0.0.jar
```

### 3️⃣ Launch
- Open HYPE CLIENT GUI
- Login with Microsoft or Offline
- Equip cosmetics
- Click **▶️ LAUNCH MINECRAFT**

## 🔧 Installation

### Requirements
- Java 11 or higher
- Maven 3.6+
- Discord (running for RPC features)
- Browser (for web dashboard - optional)

## 📖 Usage

### Login Options

1. **Microsoft Account** 🔐
   - Click "Login with Microsoft Account"
   - Complete the OAuth flow in your browser
   - Enter the authorization code

2. **Offline Mode** 👤
   - Enter a username
   - Click "Play Offline"

### Customize with Cosmetics

1. Login with either method
2. Go to **Cosmetics** tab
3. Select from:
   - 🪶 **Wings** - 5 unique wing styles
   - 👑 **Hats** - 5 unique hat styles
   - 🐉 **Pets** - 5 unique pet companions

### Launch Minecraft

1. Equip cosmetics (optional)
2. Click **▶️ LAUNCH MINECRAFT**
3. Your status will appear on Discord: "Playing HYPE CLIENT"

## 🌐 Web Dashboard

Access the web panel at: **http://localhost:3000**

Features:
- 👥 Manage players
- 🎨 Equip/unequip cosmetics
- 📊 View active cosmetics
- 🔍 Search and filter

## 📊 Discord Integration

When you launch Minecraft through HYPE CLIENT:
- Status shows: "HYPE CLIENT"
- Details include: Username, Server, and equipped cosmetics
- Example: `Playing as Player123 | 🪶 Angel Wings | 👑 Golden Crown | 🐉 Dragon`

## 🔌 REST API

### Base URL
```
http://localhost:8888
```

### Endpoints

**Get all cosmetics:**
```bash
GET /api/cosmetics
```

**Get player cosmetics:**
```bash
GET /api/player/{username}
```

**Equip cosmetic:**
```bash
POST /api/player/{username}/equip
Content-Type: application/json

{
  "type": "wings|hat|pet",
  "id": "cosmetic_id"
}
```

**Server status:**
```bash
GET /api/status
```

## 📁 Project Structure

```
HYPE-CLIENT/
├── pom.xml                                # Maven configuration
├── src/main/java/com/hypeclient/
│   ├── HypeClientLauncher.java           # Main entry point
│   ├── api/
│   │   └── CosmeticsAPIServer.java       # REST API Server (Port 8888)
│   ├── auth/
│   │   ├── MicrosoftAuth.java            # Microsoft OAuth
│   │   └── OfflineAuth.java              # Offline mode
│   ├── cosmetics/
│   │   ├── CosmeticManager.java          # Cosmetic manager
│   │   ├── Wings.java                    # Wings cosmetic
│   │   ├── Hats.java                     # Hats cosmetic
│   │   └── Pets.java                     # Pets cosmetic
│   ├── discord/
│   │   └── DiscordRPC.java               # Discord Rich Presence
│   ├── launcher/
│   │   └── MinecraftLauncher.java        # Minecraft launcher
│   └── ui/
│       └── LauncherUI.java               # GUI (Swing)
├── src/main/resources/
│   └── logback.xml                       # Logging configuration
└── README.md
```

## 🛠️ Configuration

### Discord RPC Setup

Edit `DiscordRPC.java` and update the CLIENT_ID:

```java
private static final long CLIENT_ID = 1234567890L; // Your Discord app ID
```

[Create Discord Application](https://discord.com/developers/applications)

### Minecraft Version

Change in `MinecraftLauncher.java`:

```java
private String gameVersion = "1.20.1"; // Change this
```

### API Server Port

Change in `CosmeticsAPIServer.java`:

```java
private static final int PORT = 8888; // Change this
```

## 🔐 Authentication Details

### Microsoft OAuth Flow

1. Opens browser for login
2. Gets authorization code
3. Exchanges for access token
4. Retrieves Minecraft profile
5. Stores UUID and username

### Offline Mode

- Generates deterministic UUID from username
- No internet required
- Works on local servers only

## 📝 Dependencies

- **gson** - JSON processing
- **httpclient5** - HTTP requests
- **JDA** - Discord integration
- **flatlaf** - Modern UI look
- **slf4j + logback** - Logging

## 🤝 Contributing

Contributions welcome! Submit PRs with:
- New cosmetics
- Bug fixes
- UI improvements
- API enhancements

## 📄 License

MIT License - Feel free to use for personal projects

## ⚠️ Disclaimer

This is a fan project. Use at your own risk. Not affiliated with Microsoft or Minecraft.

## 🐛 Troubleshooting

### Discord RPC not connecting?
- Ensure Discord is running
- Check Discord RPC port (6463)

### Minecraft won't launch?
- Ensure Java 11+ is installed
- Check `~/.minecraft` directory exists
- Verify game files are present

### Login fails?
- Clear browser cache
- Try offline mode
- Check internet connection

### API Server not responding?
- Check if port 8888 is available
- Ensure HypeClientLauncher started CosmeticsAPIServer
- Check logs for errors

## 📊 Build Status

![Build Status](https://github.com/dawiofejm-hue/HYPE-CLIENT/actions/workflows/maven-publish.yml/badge.svg)

---

**Made with ❤️ for Minecraft enthusiasts**

**Version:** 1.0.0 | **Java:** 11+ | **Build:** Maven
