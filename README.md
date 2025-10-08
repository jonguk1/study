# ToDo List Spring Boot 프로젝트

## 소개
- Java Spring Boot 기반의 간단한 ToDo 리스트 백엔드/프론트엔드 예제입니다.
- REST API, 입력값 검증, 예외 처리, DTO 분리, Swagger(OpenAPI) 문서화 등 실무에 필요한 구조를 포함합니다.

## 주요 기능
- 할 일 등록, 조회, 수정, 삭제, 완료/미완료 토글
- 입력값 검증 및 일관된 에러 메시지 처리
- Swagger UI를 통한 API 문서 자동화 및 테스트
- 프론트엔드(todo.html)에서 할 일 관리

## 추가 개발 아이디어
- 사용자 인증(로그인/회원가입) 및 사용자별 할 일 관리
- 할 일 마감일, 중요도, 카테고리 등 추가
- 할 일 목록 페이징 및 정렬
- 할 일 완료/미완료 통계 및 대시보드
- 파일 첨부(이미지, 문서 등)
- 알림(마감 임박, 완료 등)
- API 테스트/문서 자동화 고도화(Swagger 설명, 예시 등)
- 단위/통합 테스트 코드 작성
- Docker, CI/CD, 클라우드 배포 등 운영 자동화

## 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Data JPA, H2 Database
- Spring Validation
- Springdoc OpenAPI(Swagger)
- HTML/CSS/JavaScript (Bootstrap)

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
   - 프론트엔드: [http://localhost:8080/todo](http://localhost:8080/todo)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - H2 콘솔: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## API 문서
- Swagger UI에서 모든 API 명세와 테스트 가능

## 기여 방법
- 이슈 등록 또는 PR(Pull Request) 환영

## 라이선스
- MIT License
