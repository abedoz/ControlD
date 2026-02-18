# ControlD Android App - Implementation Plan

## Overview
Native Android app (Kotlin + Jetpack Compose) for managing a personal ControlD subscription via their REST API.

## Tech Stack
- **UI**: Jetpack Compose + Material 3
- **Networking**: Retrofit + OkHttp + Moshi
- **Architecture**: MVVM + Repository pattern
- **DI**: Hilt
- **Navigation**: Compose Navigation
- **Token Storage**: EncryptedSharedPreferences
- **Min SDK**: 26 (Android 8.0) / Target SDK: 34

## API Details
- **Base URL**: `https://api.controld.com`
- **Auth**: `Authorization: Bearer <token>`
- **Account type**: Personal (no org/sub-org features)

## Implementation Steps

### Phase 1: Project Setup
1. Create Android project structure (Gradle Kotlin DSL)
2. Configure build.gradle with all dependencies
3. Set up Hilt application class
4. Set up AndroidManifest with INTERNET permission

### Phase 2: Data Layer
5. Define API response models (data classes for devices, profiles, services, rules, filters, analytics, user info)
6. Create Retrofit service interface (`ControlDApi`) with all endpoints
7. Create Hilt network module (OkHttp client with auth interceptor, Retrofit instance, Moshi)
8. Create repository classes (`DeviceRepository`, `ProfileRepository`, `AnalyticsRepository`, `UserRepository`)

### Phase 3: Token Management
9. Create `TokenManager` using EncryptedSharedPreferences
10. Create OkHttp Interceptor that injects Bearer token

### Phase 4: UI - Theme & Navigation
11. Set up Material 3 theme (colors, typography)
12. Create navigation graph with routes
13. Create bottom navigation bar (Dashboard, Devices, Profiles, Analytics)

### Phase 5: UI - Screens
14. **Login Screen** - API token input, validation via GET /users
15. **Dashboard Screen** - Account info, current IP, quick stats
16. **Devices Screen** - List all devices, create/modify/delete
17. **Device Detail/Edit Dialog** - Modify device settings, assign profile
18. **Profiles Screen** - List all profiles, create/modify
19. **Profile Detail Screen** - Tabs for Services, Custom Rules, Filters
20. **Services Tab** - List/modify service rules (block/bypass/spoof/redirect)
21. **Custom Rules Tab** - List/create/modify rules and folders
22. **Filters Tab** - View native and 3rd party filter states
23. **Analytics Screen** - Storage regions display

### Phase 6: Polish
24. Error handling (network errors, auth failures, API errors)
25. Pull-to-refresh on list screens
26. Loading states and empty states
27. Logout functionality (clear token)

## Project Structure
```
app/src/main/java/com/controld/app/
├── ControlDApplication.kt          # Hilt Application
├── MainActivity.kt                 # Single activity
├── data/
│   ├── api/
│   │   └── ControlDApi.kt          # Retrofit interface
│   ├── model/
│   │   ├── ApiResponse.kt          # Generic wrapper
│   │   ├── Device.kt
│   │   ├── Profile.kt
│   │   ├── Service.kt
│   │   ├── Rule.kt
│   │   ├── Filter.kt
│   │   ├── Analytics.kt
│   │   └── User.kt
│   └── repository/
│       ├── DeviceRepository.kt
│       ├── ProfileRepository.kt
│       ├── AnalyticsRepository.kt
│       └── UserRepository.kt
├── di/
│   └── NetworkModule.kt            # Hilt module
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── navigation/
│   │   ├── NavGraph.kt
│   │   └── Screen.kt
│   ├── login/
│   │   ├── LoginScreen.kt
│   │   └── LoginViewModel.kt
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── devices/
│   │   ├── DevicesScreen.kt
│   │   ├── DevicesViewModel.kt
│   │   └── DeviceEditDialog.kt
│   ├── profiles/
│   │   ├── ProfilesScreen.kt
│   │   ├── ProfilesViewModel.kt
│   │   ├── ProfileDetailScreen.kt
│   │   ├── ProfileDetailViewModel.kt
│   │   ├── ServicesTab.kt
│   │   ├── RulesTab.kt
│   │   └── FiltersTab.kt
│   ├── analytics/
│   │   ├── AnalyticsScreen.kt
│   │   └── AnalyticsViewModel.kt
│   └── components/
│       ├── LoadingIndicator.kt
│       └── ErrorMessage.kt
└── util/
    └── TokenManager.kt
```
