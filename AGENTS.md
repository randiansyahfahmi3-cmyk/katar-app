# KATAR (Karang Taruna) Application - Technical Specification

**Package:** `com.katar.app`

## 1. Technology Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **UI Design:** Material Design 3
- **Navigation:** Compose Navigation with Bottom Navigation Bar
- **Build System:** Gradle

## 2. Architecture

### Pattern: MVVM + Clean Architecture

```
UI Layer          → View → Compose UI → StateFlow → ViewModel
                    ↓
ViewModel Layer    → ViewModel → StateFlow → UI
                    ↓
Repository Layer   → Repository → Data Source (Local/Remote)
                    ↓
Data Layer         → Room Database → Entity/DAO
                    ↓
Remote Layer       → Retrofit + OkHttp → REST API
```

### Dependency Rules:
- UI depends only on ViewModel (no direct Repository dependency)
- ViewModel depends on Repository
- Repository depends on Data Source implementations
- Data Sources depend on models/entities only
- No circular dependencies allowed

## 3. Local Storage & Cloud Integration

### Room Database (Offline-First)
- **Database:** `@Database(entities = [...], version = 1)`
- **Migration:** Strategy defined for version updates
- **DAO Methods:** CRUD operations for all entities

### REST API Structure (Retrofit + OkHttp)
- **Base URL:** To be configured per environment
- **Timeouts:** Connect timeout 30s, Read timeout 60s
- **Interceptors:** Logging, Authorization (Bearer token)
- **Endpoints:** RESTful JSON API corresponding to data models

### Data Source Interfaces:
```kotlin
interface LocalDataSource { /* Room operations */ }
interface RemoteDataSource { /* Retrofit operations */ }
interface Repository { /* Unified data operations */ }
```

## 4. Main Features

### (a) Manajemen Anggota & Pengurus
- Add/edit/view members and executives
- Role-based permissions (Anggota vs Pengurus)
- Search and filter functionality

### (b) Kas & Laporan Keuangan
- Income and expense tracking
- Category classification (Mandatory, Operational, etc.)
- Financial reports visualization (charts)
- Balance tracking

### (c) Agenda Kegiatan & Absensi Rapat
- Create/manage event agendas
- Attendance tracking per event
- Rapat (meeting) status updates
- Absensi recording with timestamp

### (d) Pengumuman/Info Warga
- Announcement posting and viewing
- Announcement categories (Info, Urgent, Event)
- Read/unread status tracking
- Push notification ready structure

## 5. Verification & Build Commands

### Required Testing
All changes must be verified with:
```bash
./gradlew testDebugUnitTest
```

### Required Build
All changes must compile with:
```bash
./gradlew assembleDebug
```

### CI/CD Gate
- Unit tests must pass (testDebugUnitTest)
- Debug APK must build successfully (assembleDebug)
- No warnings allowed in `warningsAsErrors` mode

## 6. Code Conventions

- **Package Structure:** `com.katar.app.{feature}.{data,domain,presentation}`
- **Naming:** camelCase for functions/variables, PascalCase for Compose components
- **Imports:** Strict ordering (Android, Architecture, App, Network, Local, etc.)
- **Null Safety:** All nullable types must be explicitly handled
- **Compose:** Preview annotations for all UI components

## 7. Project Initialization Checklist

- [ ] Create Kotlin project with `com.katar.app` package
- [ ] Add Jetpack Compose Material 3 dependency
- [ ] Configure Compose Navigation with Bottom Navigation
- [ ] Set up Room Database with initial entities
- [ ] Configure Retrofit + OkHttp for REST API
- [ ] Implement MVVM pattern for all 4 main features
- [ ] Write initial unit tests
- [ ] Run `./gradlew testDebugUnitTest` - must pass
- [ ] Run `./gradlew assembleDebug` - must succeed