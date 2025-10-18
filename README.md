# 🎵 LoopMusic

<div align="center">

A modern and elegant cross-platform music player application built with Kotlin Multiplatform and Compose Multiplatform.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-brightgreen.svg?style=flat)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Features](#-features) • [Architecture](#-architecture) • [Technologies](#-technologies) • [Installation](#-installation) • [Development](#-development)

</div>

---

## 📱 About the Project

LoopMusic is a cross-platform music player that works on both **Android** and **iOS**, sharing most of the code between both platforms. The application offers a modern and fluid experience for managing and playing your local music library.

### Supported Platforms

-   🤖 **Android** (API 24+)
-   🍎 **iOS** (iOS 13+)

---

## ✨ Features

### 🎼 Music Management

-   **Multiple views**: Songs, Albums, Artists, Folders, and Playlists
-   **Advanced search**: Real-time search across your entire library
-   **Folder selection**: Custom scanning of music directories
-   **Playlist management**: Create, edit, and organize your playlists

### 🎨 User Interface

-   **Material 3 Design**: Modern and elegant UI
-   **Customizable themes**: Light, dark, and follow system modes
-   **Dynamic color palette**: Color extraction from album artwork
-   **Smooth animations**: Fluid transitions and polished user experience
-   **Edge-to-Edge**: Full screen utilization

### 🎵 Playback

-   **Full control**: Play, pause, next, previous
-   **Playback modes**: Normal, repeat, and shuffle
-   **Interactive progress bar**: Navigation within songs
-   **Now Playing**: Expanded view with detailed information
-   **Background service** (Android): Continuous playback with media notification

### 💾 Data Management

-   **Local database**: Persistent storage with Room
-   **Smart cache**: Caching system for artwork and metadata
-   **Automatic metadata**: Information extraction from audio files
-   **Synchronization**: Automatic library updates

---

## 🏗 Architecture

The project follows a **Clean Architecture** with clear separation of concerns:

```
composeApp/
├── commonMain/           # Shared code between platforms
│   ├── domain/          # Business logic
│   │   ├── model/       # Domain models
│   │   ├── usecase/     # Use cases
│   │   └── interfaces/  # Repository contracts
│   ├── data/            # Data implementation
│   │   └── mapper/      # Data transformation
│   ├── infrastructure/  # Infrastructure
│   │   └── database/    # Room database
│   ├── presentation/    # ViewModels
│   └── ui/              # User interface
│       ├── components/  # Reusable components
│       ├── features/    # Screens
│       ├── navigation/  # Navigation
│       └── theme/       # Themes and styles
├── androidMain/         # Android-specific code
└── iosMain/             # iOS-specific code
```

### Patterns and Principles

-   **MVVM**: Model-View-ViewModel for UI/Logic separation
-   **Repository Pattern**: Data source abstraction
-   **Dependency Injection**: Koin for dependency injection
-   **UseCase Pattern**: Encapsulated business logic
-   **Clean Architecture**: Layer separation (Domain, Data, Presentation)
-   **Reactive Programming**: StateFlow for state management

---

## 🛠 Technologies

### Core

-   **[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)** - Shared code between platforms
-   **[Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)** - Cross-platform declarative UI
-   **[Kotlin 2.2.10](https://kotlinlang.org/)** - Programming language

### Architecture and Dependencies

-   **[Koin](https://insert-koin.io/)** - Dependency injection (4.1.1)
-   **[Room](https://developer.android.com/training/data-storage/room)** - Local database (2.7.2)
-   **[SQLite](https://www.sqlite.org/)** - Database engine (2.5.2)

### Networking and Serialization

-   **[Ktor Client](https://ktor.io/docs/client.html)** - HTTP client (3.3.0)
-   **[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)** - JSON serialization

### UI and Media

-   **[Coil](https://coil-kt.github.io/coil/)** - Image loading (3.3.0)
-   **[Media3](https://developer.android.com/jetpack/androidx/releases/media3)** - Android multimedia playback (1.8.0)
-   **[KMPalette](https://github.com/jordond/kmpalette)** - Image color extraction (3.1.0)

### Navigation and Utils

-   **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** - Declarative navigation (2.9.0)
-   **[FileKit](https://github.com/vinceglb/FileKit)** - Cross-platform file picker (0.10.0)
-   **[Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)** - Cross-platform date handling

### Testing

-   **JUnit** - Testing framework
-   **Kotlin Test** - Cross-platform testing

### Build Tools

-   **[Gradle](https://gradle.org/)** with Kotlin DSL
-   **[KSP](https://github.com/google/ksp)** - Kotlin Symbol Processing

---

## 📦 Installation

### Prerequisites

-   **JDK 11** or higher
-   **Android Studio** Ladybug or higher
-   **Xcode 14+** (for iOS development on macOS)
-   **Kotlin 2.2.10**

### Clone the Repository

```bash
git clone https://github.com/yourusername/loopmusic.git
cd loopmusic
```

### Android Setup

1. Open the project in Android Studio
2. Sync Gradle
3. Run on emulator or physical device

```bash
./gradlew :composeApp:assembleDebug
```

### iOS Setup

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select your device or simulator
3. Run the project (⌘ + R)

---

## 🚀 Development

### Module Structure

```
loopmusic/
├── composeApp/          # Main application module
├── gradle/              # Gradle configuration
├── iosApp/              # Native iOS application
└── build.gradle.kts     # Root Gradle configuration
```

### Useful Commands

```bash
# Build the entire project
./gradlew build

# Clean build
./gradlew clean

# Run tests
./gradlew test

# Generate release APK
./gradlew :composeApp:assembleRelease

# Check dependencies
./gradlew dependencies
```

### Configuration Variables

Main configuration in `gradle/libs.versions.toml`:

```toml
[versions]
android-minSdk = "24"
android-targetSdk = "35"
android-compileSdk = "36"
```

### API Keys / Secrets

The app uses Spotify API to retrieve the artists artworks. The repository does not include local secret files (for example, any API keys or credentials) for security reasons. To run the app you must add your own API keys file with your Spotify credentials.

Create the following Kotlin file in the project (example path):

`composeApp/src/commonMain/kotlin/com/example/jcarlosvelasco/loopmusic/secrets/ApiKeys.kt`

Example minimal contents (replace the placeholder values with your actual keys):

```kotlin
package com.example.jcarlosvelasco.loopmusic.secrets

object ApiKeys {
    const val SPOTIFY_CLIENT_ID = "your_spotify_client_id"
    const val SPOTIFY_CLIENT_SECRET = "your_spotify_client_secret"
}
```

Make sure this file is not committed to the public repository. The project intentionally omits sensitive files from version control; add the file locally before building or running the app.

---

## 🎨 Theme Configuration

The application supports three theme modes:

```kotlin
enum class Theme {
    LIGHT,   // Light mode
    DARK,    // Dark mode
    SYSTEM   // Follow system settings
}
```

Themes can be changed from the application settings and are persisted locally.

---

## 📝 Key Technical Features

### 1. **Dependency Injection with Koin**

```kotlin
// Modules organized by functionality
├── DatabaseFactory
├── FilesFactory
├── MediaPlayerFactory
├── MetadataParserFactory
├── PlaybackServiceFactory
└── SharedPreferencesFactory
```

### 2. **Cross-platform Room Database**

-   Entities: Songs, Albums, Artists, Playlists, Folders
-   DAOs with optimized queries
-   Automatic migrations
-   Custom converters

### 3. **Type-Safe Navigation**

```kotlin
// Serializable routes with Navigation Compose
@Serializable object HomeRoute
@Serializable object PlayingRoute
@Serializable data class AlbumDetailRoute(val albumId: String)
```

### 4. **Reactive State Management**

```kotlin
// StateFlow for UI states
val songs: StateFlow<List<Song>>
val currentPlayingSong: StateFlow<Song?>
val mediaState: StateFlow<MediaState>
```

---

## 🔐 Permissions

### Android

```xml
<!-- Storage access -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Notifications (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### iOS

```xml
<!-- Info.plist -->
<key>NSAppleMusicUsageDescription</key>
<string>To access your music library</string>
```

---

## 🤝 Contributing

Contributions are welcome. Please:

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

## 👤 Author

**Juan Carlos Velasco**

-   GitHub: [@jcarlosvelasco](https://github.com/jcarlosvelasco)
-   Email: jcarlosvelasco14@gmail.com
-   Web: [jcarlosvelasco.com](https://jcarlosvelasco.com)
-   LinkedIn: [Juan Carlos Velasco](https://www.linkedin.com/in/jcarlosvelasco/)

---

## 🙏 Acknowledgments

-   [JetBrains](https://www.jetbrains.com/) for Kotlin and Compose Multiplatform
-   [Square](https://square.github.io/) for the excellent Android libraries
-   The Kotlin Multiplatform community

---

## 📚 Additional Resources

-   [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
-   [Compose Multiplatform Guide](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)
-   [Koin Documentation](https://insert-koin.io/docs/reference/koin-mp/kmp)
-   [Room Documentation](https://developer.android.com/training/data-storage/room)

---

<div align="center">

**[⬆ Back to top](#-loopmusic)**

Made with ❤️ and Kotlin Multiplatform

</div>
