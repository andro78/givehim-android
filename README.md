# 기부힘 Android

기부힘의 첫 기부자용 네이티브 앱입니다. 현재 운영 API에 맞춰 공개 사연 목록, 비공개 사연 접수, 신뢰 원칙을 제공합니다. 결제는 서버·PG·법률 게이트 전까지 포함하지 않습니다.

## 기술 기준

- Kotlin 2.3.21, Jetpack Compose BOM 2026.08.00
- AGP 9.2.0, Gradle 9.4.1, JDK 17
- compile/target SDK 36, min SDK 26
- 운영 API: `https://www.give-him.org`

## 빌드

공식 Android SDK가 동작하는 x86_64 Linux/macOS/Windows 환경에서 실행합니다.

```bash
./gradlew :app:assembleDebug
```

Orange Pi 서버는 ARM64이므로 홈페이지와 AI PM 운영에 사용하고 Android APK/AAB는 CI 또는 x86_64 개발 머신에서 빌드합니다.
