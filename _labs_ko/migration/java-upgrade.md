---
title: 자바 버전 업그레이드 자동화하기
lang: ko
category: Migration
difficulty: 가이드
duration: 30분
stack: Java
work_replaced: 수동 자바 버전 업그레이드
expected_saving: 2일 → 2시간
---

## 문제

Java 8이나 11로 된 자산을 최신 LTS(17, 21, 25)로 옮겨야 하는데, 손으로 하면 - 의존성 버전 올리기, `javax` → `jakarta`, deprecated API 교체, 그다음 깨지는 걸 전부 고치기 - 느리고 위험한 작업이다. Bob의 프리미엄 Java Modernization 패키지는 이 업그레이드 전체를 가이드형 에이전틱 워크플로로 돌린다. 참고: 이건 **유료 프리미엄 패키지**라, 해당 패키지가 활성화된 계정에서만 워크플로가 실행된다.

## 프롬프트

**Agent 모드**에서, 깨끗하게 빌드되는 자바 프로젝트를 열고 실행:

```
이 프로젝트에 Java Upgrade 워크플로를 실행해줘.

요구사항:
- 목표 자바 버전: 21
- 자바 배포판: Semeru
- 빌드 도구: Maven (pom.xml 감지)
- 프레임워크별 코드 패턴을 모두 감지해서 마이그레이션
- Java 21 마이그레이션용 OpenRewrite 레시피 적용
- 빌드가 통과할 때까지 에이전틱 수정 사이클 반복
- 워크플로 단계의 Mermaid 다이어그램 생성
- 피처 브랜치 생성, 변경 커밋, 풀 리퀘스트 열기
```

## 기대 결과

- [ ] *내* 프로젝트의 실제 프레임워크와 버전(예: Spring Boot 2.7, Hibernate 5.6)과 적용할 구체적인 OpenRewrite 레시피를 짚는 업그레이드 계획 - 일반적인 "21로 올림" 요약이 아니라
- [ ] 실제 코드 변환이 적용되고 diff로 표시됨: `javax` → `jakarta`, deprecated API 교체, `maven-compiler-plugin` source/target을 목표 버전으로 상향 - 설명만이 아니라
- [ ] Build → Test → Diagnose → Fix를 반복하는 에이전틱 수정 사이클이 빌드와 테스트가 실제로 통과할 때까지 돎 - 첫 컴파일 에러에서 멈춰 사용자가 손으로 고치게 두는 게 아니라
- [ ] 워크플로 자체 산출물: 단계별 Mermaid 다이어그램, 그리고 피처 브랜치·커밋·PR로 마이그레이션이 감사 가능함 - "목표 버전에서 컴파일, 레시피 적용, 빌드/테스트 통과, PR 생성"이라는 성공 기준에 부합

IBM Bob 프리미엄 [Java Modernization 패키지](https://bob.ibm.com/docs/ide/premium-packages/java-modernization/java-modernization-index)와 공개된 Java Upgrade 워크플로 기준입니다. OpenRewrite 레시피와 에이전틱 수정 사이클로 Java 8/11을 17, 21, 25로 업그레이드하며, Semeru / Temurin / Corretto와 Maven·Gradle 모두를 지원합니다.

<!-- Bob-verify: 아직 Bob에 안 돌려봤고, 이 환경에서는 실행 자체가 불가 - Java Modernization은 접근 권한이 없는 유료 프리미엄 패키지. 공개된 프리미엄 워크플로 워크스루(bob-lab-app .../labs/premium-java-modernization) 기반으로 작성. 참가자에게 쓰기 전에 실제 프리미엄 패키지로 검증 필요 - 특히 단일 프롬프트로 워크플로가 시작되는지(Start Workflow 버튼 / `/start-java-mod` 스킬 대비), 에이전틱 수정 사이클이 사람 개입 없이 통과 빌드까지 도는지 확인할 것. -->

## 팁

- 이건 프리미엄 패키지다. Java Modernization이 활성화된 계정에서만 워크플로가 돈다 - 데모 전에 접근 권한을 확인할 것, 아니면 프롬프트가 아무것도 안 한다. **Start Workflow** 버튼이나 `/start-java-mod` 스킬로도 시작할 수 있다.
- 큰 자산에 들이대기 전에 작은 모듈에서 먼저 워크플로 형태를 익힐 것 - 패키지 자체 권장 사항.
- 롤백할 수 있게 전용 마이그레이션 브랜치를 먼저 만들고, Bob의 서브태스크가 실패를 처리하게 둘 것 - 빌드 실패를 강제로 넘기지 말 것. 에이전틱 수정 사이클이 핵심이니까.
- 어디든 배포하기 전에 검증 리포트를 검토할 것. 워크플로는 컴파일되고 테스트되고 PR까지 올라간 업그레이드를 주지만, 동작에 대한 사람의 최종 승인을 대체하지는 않는다.

## 응용

1. **Liberty 리플랫폼**: "이 프로젝트에 Liberty Replatforming 워크플로를 실행해줘" + AMA 마이그레이션 번들 - WebSphere를 Open/WebSphere Liberty로 이전하고, `server.xml`을 생성·검증하고, `liberty-maven-plugin`으로 배포.
2. **UI 현대화**: "UI Modernization 워크플로를 실행해줘" - 레거시 JSP/Struts UI를 Spring Boot 3 REST 백엔드와 React + Carbon 프론트엔드로 컴포넌트 단위 이전, 비즈니스 규칙은 전부 보존.
3. **단위 테스트 생성**: "Unit Test Generation 워크플로를 실행해줘" - 전략 우선 접근에 generate-run-fix 루프로 JaCoCo 커버리지 임계치(예: 라인 80%)까지 반복.
