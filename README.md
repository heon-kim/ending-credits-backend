# Ending-Credits-Backend

### ✅ Commit Convention

| 태그 이름      | 설명 |
|------------| --- |
| CHORE    | 코드 수정, 내부 파일 수정 |
| FEAT     | 새로운 기능 구현 |
| ADD      | FEAT 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 |
| HOTFIX   | issue나 QA에서 급한 버그 수정에 사용 |
| FIX      | 버그, 오류 해결 |
| DEL      | 쓸모 없는 코드 삭제 |
| DOCS     | README나 WIKI 등의 문서 개정 |
| CORRECT  | 주로 문법의 오류나 타입의 변경, 이름 변경에 사용 |
| MOVE     | 프로젝트 내 파일이나 코드의 이동 |
| RENAME   | 파일 이름 변경이 있을 때 사용 |
| IMPROVE  | 향상이 있을 때 사용 |
| REFACTOR | 전면 수정이 있을 때 사용 |
| TEST     | 테스트 코드 추가 시 사용 |


- 커밋 규칙 -> FEAT. 해당 기능 설명
- `ex) FEAT. 로그인 기능 추가`


### 🔸Git Working Process
1. issue를 등록한다.
2. 깃 컨벤션에 맞게 Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
3. 작업이 완료되었다면, Remote Repository(Github)에 해당 브랜치를 올린다.
4. Github에서 PR을 생성한다.
5. CI 테스트를 잘 통과했다면, 해당 PR에 관한 리뷰를 요청한다.
6. 리뷰에서 Approve를 받지 못했다면, 수정 사항을 처리해서 다시 올린다. 
7. Approve를 받았다면, Merge를 진행한다.
8. merge된 Branch는 삭제한다.
9. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.

🌴 브랜치
---
#### 📌 브랜치 단위
- 브랜치 단위 = 이슈 단위 = PR단위

#### 📌 브랜치명
- 브랜치는 뷰 단위로 생성합니다.
- 브랜치 규칙 → feature/기능상세-이슈번호
- `ex) feature/login-1`


### 🌟 코드 리뷰 규칙
### Pull Request(PR)
1. 기능 개발이 완료되면 `feature/기능상세-이슈번호` 브랜치에서 PR을 생성
2. PR 제목은 해당 기능을 설명할 수 있도록 작성
3. 리뷰어를 지정하고, 관련 이슈를 연결
4. PR은 최소 한 명 이상의 리뷰어가 승인해야 병합 가능

### 리뷰어 역할
- 코드 스타일 가이드 준수 여부 확인
- 기능 요구사항 충족 여부 확인
- 성능 및 보안 개선 사항 피드백 제공


### 기타 규칙
- 커밋 전에 반드시 모든 테스트를 실행하고 통과하기

