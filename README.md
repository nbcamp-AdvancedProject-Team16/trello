## :notebook_with_decorative_cover: Trello Project
Spring Boot를 활용해 칸반 보드 방식으로 작업을 관리할 수 있는 협업 툴 구현

## :alarm_clock: 개발기간
2024/10/14 ~ 2024/10/18

## :wrench: 개발 환경
- **Framework**: Spring Boot, JPA

- **Database**: MySQL

## :floppy_disk: 기술 스택
![인텔리제이](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

Development<p>
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![스프링](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![스프링부트](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
![mysql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white) <p>
![AWS](https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) 
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">
<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
<img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white">
<p>
  
Communication<p>
![슬랙](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)


## :busts_in_silhouette: 멤버구성 및 역할분담
- 팀장 배주희: 회원가입/로그인, 멤버 및 역할 관리, 사용자 인증

- 팀원 김기혜: 보드, 리스트

- 팀원 윤지현: 첨부파일, 알림

- 팀원 안동환: 워크스페이스, 검색

- 팀원 김태준: 카드, 댓글


## :pushpin: 와이어 프레임
![수정본 3 와이어프레임](https://github.com/user-attachments/assets/640563e6-4a9a-4d0b-b24c-1824391962ef)



## :mag_right: ERD



## :green_book: API 명세함



## :page_facing_up: 기능 설명
### 1. 회원가입/로그인

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 회원 가입    | 이메일 형식의 유저 아이디, 비밀번호를 입력받아 USER 또는 ADMIN 권한으로 회원 정보 저장 |
| 로그인      | 저장된 이메일과 비밀번호를 입력해 JWT                                         |
| 회원 탈퇴    | 비밀번호 확인 후, 회원 상태를 탈퇴 상태로 변경하여 다시 사용할 수 없게 함             |


### 2. 멤버 및 역할 관리

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 유저 권한    | 일반 USER와 ADMIN 권한을 가질 수 있음<p> ADMIN은 워크스페이스 생성 및 관리 권한이 있음|
| 멤버 역할    | [워크스페이스어드민] 관리자에 의해 설정되며, 워크스페이스 생성 외 모든 기능 사용 가능 <p> [보드] 워크스페이스 관련 기능을 제외한 다른 모든 기능 사용 가능<p> [읽기 전용] 조회만 가능, 생성/수정/삭제 불가 |


### 3. 워크스페이스

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 워크스페이스 생성   | 권한이 Admin 인 User 만 워크스페이스를 생성할 수 있고, 워크스페이스 이름과 설명을 등록 가능   |
| 워크스페이스 조회   | 유저가 멤버로 가입되어 있는 워크스페이스를 조회 가능                                    |
| 워크스페이스 수정   | 워크스페이스의 이름과 설명을 수정가능하도록 설정                                       |
| 워크스페이스 삭제   | 워크스페이스가 삭제가 가능하며, 삭제를 하면, 멤버들은 더이상 워크스페이스 멤버가 아니게 됨       |


### 4. 보드

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 보드 생성 | 멤버는 워크스페이스 내에서 제목과 배경색, 이미지를 설정하여 보드 생성 가능 (읽기 전용 멤버는 생성 불가)                |
| 보드 조회 | 자신이 속한 워크스페이스의 보드 조회 가능 (읽기 전용 멤버는 수정 불가, 단건 조회 시 해당 보드의 리스트와 카드도 함께 조회) |
| 보드 삭제 | 삭제 시 보드 내에 있는 리스트의 데이터도 함께 삭제 (읽기 전용 멤버는 삭제 불가)                                  |


### 5. 리스트

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 리스트 생성   | 멤버는 보드 내에서 리스트 생성 가능 (읽기 전용 멤버는 생성 불가)                  |
| 리스트 수정   | 리스트의 순서 수정 가능 (읽기 전용 멤버는 수정 불가)                           |
| 리스트 삭제   | 리스트 삭제 시 해당 리스트의 모든 카드 데이터도 함께 삭제 (읽기 전용 멤버는 삭제 불가) |
|     |         |
|     |      |

### 6. 카드

| 기능       | 내용                                         |
|----------|--------------------------------------------|
|    |  |
|  |               |
|     |        |
|     |         |
|     |      |

### 7. 댓글

| 기능       | 내용                                         |
|----------|--------------------------------------------|
|    |  |
|  |               |
|     |        |
|     |         |
|     |      |


### 8. 첨부파일

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 첨부파일 생성 | READ_ONLY 권한을 가진 멤버를 제외한 사용자가 파일을 생성, S3에 파일 저장          |
| 첨부파일 조회 | 해당 워크스페이스를 생성한 사용자 또는 그 멤버들만 특정 카드에 있는 첨부파일 목록을 조회 |
| 첨부파일 삭제 | READ_ONLY 권한을 가진 멤버를 제외한 사용자가 파일을 삭제, S3에 저장된 파일 삭제     |


### 9. 알림

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 알림 생성     | 해당 유저의 ID, 메시지, 워크스페이스 ID, type을 입력받아 알림을 생성 및 슬랙에 알림 전달 |
| 알림 조회     | 테이블에 저장된 알림들을 조회                                                 |
| 알림 삭제     | 테이블에 저장된 특정 알림 삭제                                                |
| 알림 설정 조회 | 알림의 활성화 상태를 조회                                                    |
| 알림 설정 수정 | type, enabled를 입력받아 알림 활성화/비활성화 상태로 수정                         |

### 10. 검색

| 기능       | 내용                                         |
|----------|--------------------------------------------|
| 카드 검색    | 제목, 설명, 마감일, 담당자를 통해 카드를 검색 가능 |


### 11. 최적화(indexing)

| 기능       | 내용                                         |
|----------|--------------------------------------------|
|     | |
|  |              |
|     |         |
|    |         |
|    |     |

    
## :pill: 트러블 슈팅
