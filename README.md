# CareerSignal

> 성균관대학교 삼성리더스클럽 재학생과 졸업생 멘토를 연결하는 멘토 탐색·투표 서비스

CareerSignal은 관심 분야, 진로 경로, 전공 연관성, 근무 지역 등의 조건으로 멘토를 탐색하고, 마음에 드는 멘토를 저장하거나 최대 3명에게 투표할 수 있는 웹 서비스입니다. 관리자 화면/API를 통해 멘토와 사용자 정보를 개별 또는 CSV로 관리할 수 있습니다.

[**서비스 바로가기 → careersignal.duckdns.org**](https://careersignal.duckdns.org)

![CareerSignal](src/main/resources/static/assets/career-signal-share.png)

## 주요 기능

- **멘토 탐색** — 전공, 분야, 진로 경로, 유학 여부, 전공 연관성 기준 필터링
- **투표 및 관심 멘토** — 로그인한 사용자는 최대 3명의 멘토에게 투표하고, 관심 멘토를 별도로 저장
- **사용자 인증** — 학번과 패스코드를 사용하는 HTTP 세션 기반 로그인/로그아웃
- **관리 기능** — 사용자·멘토 등록, 수정, 삭제와 CSV 일괄 등록
- **파일 관리** — MinIO 기반 프로필·카드 이미지 업로드 및 인라인 조회
- **API 문서** — Spring REST Docs로 테스트 기반 API 문서를 자동 생성하여 `/docs/index.html`에서 제공

## 기술 스택

| 구분         | 기술                                      |
| ------------ | ----------------------------------------- |
| Backend      | Java 17, Spring Boot 4.1, Spring MVC      |
| Data         | Spring Data JPA, MySQL                    |
| Storage      | MinIO                                     |
| Build & Test | Gradle Wrapper, JUnit 5, Spring REST Docs |
| Frontend     | HTML, CSS, JavaScript (정적 리소스)       |

## 시작하기

### 사전 요구 사항

- JDK 17 이상
- MySQL 8 이상
- MinIO 서버

### 1. 데이터베이스 준비

MySQL에서 아래 데이터베이스를 생성합니다.

```sql
CREATE DATABASE SLC_CAREERSIGNAL
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

기본 설정은 `root` / `root` 계정을 사용합니다. 실제 환경에서는 `src/main/resources/application.yaml`의 데이터베이스와 MinIO 접속 정보를 환경에 맞게 변경하세요.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/SLC_CAREERSIGNAL
    username: <MYSQL_USERNAME>
    password: <MYSQL_PASSWORD>

minio:
  url: http://localhost:9000
  access-key: <MINIO_ACCESS_KEY>
  secret-key: <MINIO_SECRET_KEY>
  bucket:
    name: test-bucket
```

MinIO에 설정한 버킷(기본값: `test-bucket`)도 생성해 주세요.

### 2. 애플리케이션 실행

Windows:

```powershell
.\gradlew.bat bootRun
```

macOS / Linux:

```bash
./gradlew bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

| 주소                                    | 설명                 |
| --------------------------------------- | -------------------- |
| [https://careersignal.duckdns.org](https://careersignal.duckdns.org) | 운영 중인 CareerSignal 서비스 |
| `http://localhost:8080/`                | CareerSignal 웹 화면 |
| [https://careersignal.duckdns.org/docs/index.html](https://careersignal.duckdns.org/docs/index.html) | 운영 API 문서 |
| `http://localhost:8080/docs/index.html` | 로컬 API 문서        |

## 테스트 및 문서 생성

```bash
./gradlew test
```

테스트가 끝나면 Spring REST Docs 문서가 생성되어 `src/main/resources/static/docs/`에 복사됩니다. 문서 생성까지 포함한 실행 파일은 다음 명령으로 만들 수 있습니다.

```bash
./gradlew bootJar
```

## API 요약

| 분류   | 메서드        | 엔드포인트               | 설명                 |
| ------ | ------------- | ------------------------ | -------------------- |
| 사용자 | `POST`        | `/login`                 | 로그인 및 세션 생성  |
| 사용자 | `POST`        | `/logout`                | 로그아웃             |
| 멘토   | `GET`         | `/mentor`                | 멘토 전체 조회       |
| 멘토   | `GET`         | `/mentorSearch`          | 조건별 멘토 검색     |
| 투표   | `GET`, `POST` | `/vote`                  | 내 투표 조회·등록    |
| 관심   | `GET`, `POST` | `/favorite`              | 관심 멘토 조회·등록  |
| 파일   | `POST`        | `/files/upload`          | 파일 업로드          |
| 파일   | `GET`         | `/files/view/{fileName}` | 파일 인라인 조회     |
| 관리자 | `POST`        | `/admin/user/batch`      | 사용자 CSV 일괄 등록 |
| 관리자 | `POST`        | `/admin/mentor/batch`    | 멘토 CSV 일괄 등록   |

전체 요청·응답 형식은 [운영 API 문서](https://careersignal.duckdns.org/docs/index.html)에서 확인할 수 있습니다. 로컬 실행 환경에서는 `http://localhost:8080/docs/index.html`을 이용하세요.

## 프로젝트 구조

```text
src
├── main
│   ├── java/com/slc/mentoring
│   │   ├── controller/    # REST API 엔드포인트
│   │   ├── service/       # 비즈니스 로직
│   │   ├── repository/    # JPA Repository
│   │   ├── entity/        # 도메인 엔티티 및 열거형
│   │   └── dto/           # 요청·응답 DTO
│   └── resources
│       ├── static/        # 웹 화면 및 생성 API 문서
│       └── application.yaml
└── test/                  # API 테스트 및 REST Docs 스니펫
```

## 주의 사항

- 현재 설정 파일에는 로컬 개발용 접속 정보가 포함되어 있습니다. 공개 저장소 또는 운영 환경에서는 민감 정보가 커밋되지 않도록 환경 변수 또는 별도 프로필로 관리하세요.
- 관리자 API는 `/admin/**` 경로를 사용합니다. 외부 공개 환경에서는 인증·인가 정책을 반드시 강화해야 합니다.
