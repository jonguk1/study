# ToDo List Spring Boot 프로젝트

## 소개
- Java Spring Boot 기반의 실전형 ToDo 리스트 백엔드/프론트엔드 예제입니다.
- JWT 인증, 사용자별 할 일 관리, REST API, 입력값 검증, 예외 처리, DTO 분리, Swagger(OpenAPI) 문서화, 단위 테스트 등 실무에 필요한 구조를 포함합니다.

## 주요 기능
- 회원가입, 로그인, 로그아웃, JWT(Access/RefreshToken) 인증
- 사용자별 할 일 등록, 조회, 수정, 삭제, 완료/미완료 토글
- 본인 할 일만 접근/수정/삭제 가능 (권한 체크)
- 입력값 검증 및 일관된 에러 메시지 처리
- Swagger UI를 통한 API 문서 자동화 및 테스트
- 프론트엔드(todo.html)에서 할 일 관리 (로그인 후 사용)
- JUnit + Mockito 기반 서비스 단위 테스트

## 실행 방법
1. 저장소 클론  
   ```
   git clone <레포지토리 주소>
   ```
2. 프로젝트 폴더 이동  
   ```
   cd example
   ```
3. 빌드 및 실행  
   ```
   ./gradlew bootRun
   ```
4. 브라우저에서 접속  
   - 로그인/회원가입: [http://localhost:8080/](http://localhost:8080/)
   - 할 일 관리: [http://localhost:8080/todo](http://localhost:8080/todo)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - H2 콘솔: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## 테스트 실행 방법
- 터미널에서  
  ```
  ./gradlew test
  ```
- 또는 IDE에서 각 테스트 파일 우클릭 → "테스트 실행"

## API 문서
- Swagger UI에서 모든 API 명세, 파라미터, 응답, 예시, 에러 응답까지 확인 및 테스트 가능

## 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Data JPA, H2 Database
- Spring Security, JWT
- Spring Validation
- Springdoc OpenAPI(Swagger)
- JUnit5, Mockito
- HTML/CSS/JavaScript (Bootstrap)

## 추가 개발 아이디어
- 관리자/권한(Role) 기능
- 할 일 마감일, 중요도, 카테고리 등 추가
- 할 일 목록 페이징 및 정렬
- 파일 첨부, 알림, 통계 등 부가 기능
- Docker, CI/CD, 클라우드 배포 등 운영 자동화

## 리포지토리 구조/운영 제안

- 현재 리포지토리 이름이 `study`라면,  
  이 프로젝트만을 위한 별도 리포지토리(`todo`)로 분리하는 것이 좋습니다.
- 여러 프로젝트(예: todo, blog, shop 등)를 모으는 상위 리포지토리를 `study`로 사용하세요.

## 리포지토리 이름 변경 및 구조 예시

1. **현재 리포지토리 이름 변경**
   - GitHub에서 `study` → `todo`로 리포지토리 이름 변경

2. **상위 리포지토리 생성**
   - GitHub에서 새 리포지토리 `study` 생성
   - 여러 프로젝트의 소스코드를 각각의 하위 폴더(또는 submodule)로 관리
     ```
     study/
       ├── todo/
       ├── blog/
       └── shop/
     ```

3. **각 프로젝트별 독립 개발/배포**
   - 각 프로젝트(todo 등)는 독립적으로 clone, 개발, 배포 가능

---

## 변경 방법 요약

- GitHub에서 `study` 리포지토리 → `todo`로 이름 변경
- 새로 `study` 리포지토리 생성 후 여러 프로젝트를 폴더별로 관리
- 기존 로컬 저장소는 아래 명령어로 원격 주소 변경
  ```
  git remote set-url origin https://github.com/yourname/todo.git
  ```

---

**정리:**  
- 단일 프로젝트는 `todo` 등 구체적 이름으로,  
- 여러 프로젝트 모음은 `study` 등 상위 리포지토리로 관리하는 것이 좋습니다.
