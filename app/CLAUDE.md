# KakaoBank Android 과제 - 프로젝트 컨벤션

## 작업 방식
- 한 번에 하나의 커밋 단위로만 작업한다
- 작업 전 반드시 범위를 먼저 설명하고 확인을 받는다
- 다음 작업으로 넘어가기 전에 커밋 여부를 물어본다
- 명시적으로 범위를 지정하지 않으면 임의로 다음 작업을 진행하지 않는다

## 프로젝트 개요
카카오 검색 API를 이용해 이미지/동영상을 검색하고 보관함에 저장하는 Android 앱.

## 아키텍처

Clean Architecture + MVVM을 기반으로 한다.
레이어 의존성 방향: Presentation → Domain ← Data

### 레이어 구조

```
presentation/   UI, ViewModel (Android 프레임워크 의존 허용)
domain/         UseCase, Repository 인터페이스, 모델 (순수 Kotlin, android.* import 금지)
data/           Repository 구현체, Remote/Local DataSource
di/             Hilt 모듈
```

### 패키지명
`com.example.kakaobank`

---

## 기술 스택

| 역할 | 라이브러리 |
|------|-----------|
| DI | Hilt |
| 비동기 | Coroutines + StateFlow |
| 네트워크 | Retrofit2 + OkHttp3 |
| JSON | Gson |
| 이미지 로딩 | Glide |
| 로컬 저장 | SharedPreferences (DB 라이브러리 사용 금지) |

---

## 파일 구조

```
app/src/main/java/com/example/kakaobank/
├── presentation/
│   ├── MainActivity.kt
│   ├── search/
│   │   ├── SearchFragment.kt
│   │   ├── SearchViewModel.kt
│   │   └── SearchAdapter.kt
│   └── bookmark/
│       ├── BookmarkFragment.kt
│       ├── BookmarkViewModel.kt
│       └── BookmarkAdapter.kt
├── domain/
│   ├── model/
│   │   └── MediaItem.kt
│   ├── repository/
│   │   ├── ISearchRepository.kt
│   │   └── IBookmarkRepository.kt
│   └── usecase/
│       ├── SearchMediaUseCase.kt
│       ├── ToggleBookmarkUseCase.kt
│       └── GetBookmarksUseCase.kt
├── data/
│   ├── remote/
│   │   ├── KakaoSearchApi.kt
│   │   └── dto/
│   │       ├── ImageSearchResponse.kt
│   │       └── VideoSearchResponse.kt
│   ├── local/
│   │   └── BookmarkLocalDataSource.kt
│   └── repository/
│       ├── SearchRepositoryImpl.kt
│       └── BookmarkRepositoryImpl.kt
└── di/
    ├── NetworkModule.kt
    └── RepositoryModule.kt
```

---

## 레이어별 규칙

### Presentation
- Fragment는 `@AndroidEntryPoint`, ViewModel은 `@HiltViewModel` 어노테이션 필수
- ViewModel은 UseCase만 주입받는다. Repository 직접 참조 금지
- UI 상태는 `StateFlow<UiState>`로 관리하고 Fragment에서 `lifecycleScope.launch + repeatOnLifecycle(STARTED)`로 collect
- `UiState`는 sealed class로 `Loading`, `Success`, `Error` 정의

### Domain
- `android.*` import 절대 금지 — JVM 단독 테스트 가능해야 함
- Repository 인터페이스는 이 레이어에 위치 (의존성 역전 원칙)
- UseCase는 `operator fun invoke()`로 호출
- `MediaItem`은 data class, `type: MediaType` (enum: IMAGE, VIDEO) 포함

### Data
- Repository 구현체는 Domain의 인터페이스를 구현
- DTO → Domain 모델 변환은 Repository 구현체 안에서 처리 (DTO가 Domain으로 노출되지 않도록)
- `BookmarkLocalDataSource`는 `MediaItem`을 JSON 직렬화(Gson)하여 SharedPreferences에 저장
- SharedPreferences key: `"bookmarks"`, 저장 형식: JSON 배열 문자열

### DI
- `NetworkModule`: Retrofit, OkHttp, KakaoSearchApi 제공 (`@Singleton`)
- `RepositoryModule`: `@Binds`로 인터페이스 → 구현체 연결

---

## 핵심 비즈니스 로직

### 검색 (SearchMediaUseCase)
```
1. ISearchRepository.searchImages()와 searchVideos()를 async { } + awaitAll()로 병렬 호출
2. 두 결과를 합쳐 MediaItem 리스트로 변환
3. datetime 필드 기준 내림차순(최신순) 정렬 후 반환
```

### 보관함 토글 (ToggleBookmarkUseCase)
```
1. IBookmarkRepository.getBookmarks()로 현재 보관 목록 조회
2. 해당 아이템이 이미 있으면 → remove
3. 없으면 → add (savedAt = System.currentTimeMillis() 기록)
```

### 보관함 표시 상태 동기화
```
- SearchViewModel이 검색 결과를 가져올 때 IBookmarkRepository에서 현재 보관 목록도 함께 조회
- MediaItem.isBookmarked는 두 목록을 비교해 SearchViewModel에서 계산하여 UI에 전달
```

---

## API 정보

### 카카오 이미지 검색
- Endpoint: `GET https://dapi.kakao.com/v2/search/image`
- 쿼리 파라미터: `query`, `sort`, `page`, `size`
- 응답에서 사용하는 필드: `thumbnail_url`, `datetime`

### 카카오 동영상 검색
- Endpoint: `GET https://dapi.kakao.com/v2/search/vclip`
- 쿼리 파라미터: `query`, `sort`, `page`, `size`
- 응답에서 사용하는 필드: `thumbnail`, `datetime`

### 인증
- 헤더: `Authorization: KakaoAK {REST_API_KEY}`
- API 키는 `local.properties`에 `KAKAO_API_KEY=...`로 저장 후 BuildConfig로 참조 (하드코딩 금지)

---

## 커밋 컨벤션

형식: `<type>(<scope>): <subject>`

| type | 용도 |
|------|------|
| `feat` | 새 기능 |
| `fix` | 버그 수정 |
| `chore` | 빌드 설정, 의존성, 툴링 등 코드 변경 없는 작업 |
| `refactor` | 기능 변경 없는 코드 구조 개선 |
| `style` | 포맷팅, trailing comma 등 코드 의미 변경 없음 |
| `test` | 테스트 추가/수정 |
| `docs` | 문서 수정 |
| `perf` | 성능 개선 |

- subject는 소문자로 시작, 마침표 없음
- 현재형 동사로 작성 (`added` ❌ → `add` ✅)
- scope는 레이어나 기능 단위 (`search`, `bookmark`, `domain`, `di` 등)
- 한 커밋은 한 가지 변경만

---

## 코딩 컨벤션

- 언어: Kotlin
- 들여쓰기: 4 spaces
- 클래스명: PascalCase / 함수·변수명: camelCase / 상수: UPPER_SNAKE_CASE
- 함수 하나는 한 가지 일만
- 새 기능 추가 시 기존 레이어 규칙을 반드시 준수
- 레이어 경계를 넘는 의존성 추가 시 이 파일 확인 후 진행
- companion object는 맨 아래로 빼기
- 함수 파라미터, 컬렉션, enum 등 여러 줄로 늘어지는 목록에는 trailing comma 사용
  ```kotlin
  // Good
  data class MediaItem(
      val imageUrl: String,
      val datetime: String,
      val type: MediaType,
      val isBookmarked: Boolean,  // ← trailing comma
  )

  // Bad
  data class MediaItem(
      val imageUrl: String,
      val datetime: String,
      val type: MediaType,
      val isBookmarked: Boolean
  )
  ```