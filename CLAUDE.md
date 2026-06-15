# WeatherAppKMP — Project Document

## Overview

Kotlin Multiplatform (KMP) weather app targeting **Android** and **iOS**. All business logic and UI live in the `shared` module using Compose Multiplatform. The `androidApp` module is a thin shell (Activity + Koin init).

---

## Tech Stack

| Layer | Library | Version |
|-------|---------|---------|
| UI | Compose Multiplatform (JetBrains) | 1.11.1 |
| HTTP | Ktor Client | 3.1.3 |
| JSON | kotlinx.serialization | 1.8.1 |
| DI | Koin | 4.0.2 |
| ViewModel | AndroidX Lifecycle (KMP) | 2.11.0-beta01 |
| Date/Time | kotlinx-datetime | 0.6.2 |
| Kotlin | 2.4.0 | — |
| Gradle | 9.1.0 | — |
| compileSdk / minSdk | 36 / 24 | — |

---

## Project Structure

```
WeatherAppKMP/
├── androidApp/                         # Android shell
│   └── src/main/
│       ├── MainActivity.kt             # setContent { App() }
│       └── WeatherApplication.kt       # initKoin(apiKey, isDebug)
│
└── shared/                             # KMP shared module
    └── src/
        ├── commonMain/kotlin/.../
        │   ├── App.kt                  # Root composable, navigation state
        │   ├── di/AppModule.kt         # Koin module definitions
        │   ├── network/
        │   │   └── WeatherApiClient.kt # Ktor HTTP client + all API calls
        │   ├── model/
        │   │   ├── forecast/ForecastModels.kt   # All API response DTOs
        │   │   ├── basenetwork/Resource.kt       # Loading/Success/Error sealed class
        │   │   └── WeatherNoteType.kt            # NoteDetailWeather enum
        │   ├── data/
        │   │   └── ForecastWeatherRepository.kt  # Cache + delegates to ApiClient
        │   ├── domain/
        │   │   └── ForecastWeatherUseCase.kt
        │   └── ui/
        │       ├── components/         # Reusable composables
        │       │   ├── AnimatedShimmer.kt
        │       │   ├── WeatherColors.kt          # weatherBackgroundColors(hour)
        │       │   ├── WeatherErrorScreen.kt
        │       │   └── WeatherInfoBottomSheet.kt
        │       ├── home/
        │       │   ├── WeatherHomeScreen.kt
        │       │   └── WeatherHomeViewModel.kt
        │       ├── detail/
        │       │   ├── WeatherDetailScreen.kt
        │       │   └── WeatherDetailViewModel.kt
        │       └── preview/PreviewData.kt        # Shared @Preview sample data
        │
        ├── androidMain/kotlin/.../
        │   └── di/KoinAndroid.kt       # startKoin { androidContext(...) }
        └── iosMain/kotlin/.../
            └── MainViewController.kt
```

---

## Navigation

Defined in `App.kt` using a simple `sealed class Screen` + `remember { mutableStateOf }`:

```
Screen.Home  →  WeatherHomeRoute
Screen.Detail  →  WeatherDetailRoute
Screen.Search  →  SearchRoute          (TODO: add)
```

To add a new screen:
1. Add object to `sealed class Screen`
2. Add `when` branch in `App.kt`
3. Pass navigation lambdas into Route composable

---

## API — WeatherAPI via RapidAPI

**Base URL:** `https://weatherapi-com.p.rapidapi.com`

**Headers (all requests):**
```
x-rapidapi-key: <from local.properties WEATHER_API_KEY>
x-rapidapi-host: weatherapi-com.p.rapidapi.com
```

**API key:** stored in `local.properties` as `WEATHER_API_KEY`, injected into `BuildConfig` via `androidApp/build.gradle.kts`, passed to Koin at app start.

### Endpoints

| Endpoint | Params | Used for |
|----------|--------|----------|
| `GET /forecast.json` | `q`, `days` (1–10), `lang`, `dt` | Home + Detail screens |
| `GET /search.json` | `q` (city name) | Search screen (TODO) |

### Search response model
```json
[
  {
    "id": 2717933,
    "name": "Ha Noi",
    "region": "",
    "country": "Vietnam",
    "lat": 21.03,
    "lon": 105.85,
    "url": "ha-noi-vietnam"
  }
]
```

### Forecast response key types (already verified against live API)

All `*_mb` pressure fields → `Double` (API returns `1013.0`)
All `vis_miles` fields → `Double` (API returns `6.0`)
`totalsnow_cm` → `Double`
`uv` → `Double` everywhere

---

## Data Flow

```
UI (Screen) → ViewModel → UseCase → Repository → ApiClient (Ktor)
                ↑                        ↓
           StateFlow               in-memory cache
        Resource<T>
```

`Resource<T>` states: `Loading`, `Success(data)`, `Error(message)`

---

## DI (Koin)

All wiring in `shared/src/commonMain/.../di/AppModule.kt`:

```kotlin
fun appModule(apiKey: String, isDebug: Boolean) = module {
    single { createHttpClient(isDebug) }
    single { WeatherApiClient(get(), apiKey) }
    single<ForecastWeatherRepository> { ForecastWeatherRepositoryImpl(get()) }
    single { ForecastWeatherUseCase(get()) }
    viewModelOf(::WeatherHomeViewModel)
    viewModelOf(::WeatherDetailViewModel)
    // Add SearchViewModel here when implementing search
}
```

---

## UI Conventions

- All screens in `commonMain` — works on both Android and iOS
- Background gradient via `weatherBackgroundColors(hour: Int)` in `WeatherColors.kt`:
  - 05:00–16:00 → day blue gradient
  - 16:00–17:00 → sunset
  - 18:00–19:00 → dusk purple
  - else → night dark blue
- `@Preview` annotation: `androidx.compose.ui.tooling.preview.Preview` (from `org.jetbrains.compose.ui:ui-tooling-preview`)
- Every composable file has a matching `*Preview.kt` in the same package
- Shared preview sample data: `ui/preview/PreviewData.kt`

---

## Adding a New Feature Checklist

1. **Model** — add data class in `model/` with `@Serializable` + correct JSON types
2. **API** — add suspend fun in `WeatherApiClient`
3. **Repository** — add interface + impl in `data/`
4. **UseCase** — add in `domain/`
5. **ViewModel** — add in `ui/<feature>/`, expose `StateFlow<Resource<T>>`
6. **Screen** — add `*Route` composable + sub-composables in `ui/<feature>/`
7. **Preview** — add `*Preview.kt` with `@Preview` for each sub-composable
8. **DI** — register in `AppModule.kt`
9. **Navigation** — add `Screen` object + `when` branch in `App.kt`

---

## Known Fixes Applied

| File | Issue | Fix |
|------|-------|-----|
| `DateUtil.kt` | `kotlin.time.Clock` returns wrong `Instant` type | Changed to `kotlinx.datetime.Clock` |
| `ForecastModels.kt` | `pressure_mb: Long/Int` but API returns `1013.0` | Changed to `Double` |
| `ForecastModels.kt` | `vis_miles: Long/Int` but API returns `6.0` | Changed to `Double` |
| `ForecastModels.kt` | `totalsnow_cm: Int` but API returns `0.0` | Changed to `Double` |
| `ForecastModels.kt` | `uv: Float` inconsistent across models | Unified to `Double` |

---

## Local Setup

1. Clone repo
2. Add `local.properties` at root:
   ```
   sdk.dir=/path/to/Android/sdk
   WEATHER_API_KEY=your_rapidapi_key_here
   ```
3. Open in Android Studio → sync Gradle
4. Run `androidApp` on emulator/device

## Build Commands

```bash
# Build Android debug APK
./gradlew :androidApp:assembleDebug

# Compile shared module only
./gradlew :shared:compileAndroidMain

# Run all tests
./gradlew test
```
