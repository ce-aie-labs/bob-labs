---
title: JSF UI를 React로 현대화하기
lang: ko
category: Migration
difficulty: 가이드
duration: 60분
stack: Java, JSF
work_replaced: JSF UI를 REST + React 앱으로 수동 재작성
expected_saving: 2주 → 한 시간
---

## 문제

JSF 프런트엔드, 즉 서버 렌더링 `.xhtml` 와 관리 빈에 API 계층도 없는 구조는 현대적 UI로 가는 막다른 길입니다. 이걸 손으로 REST 백엔드와 React 프런트엔드로 옮기면, 모든 화면의 동작을 다시 유도하면서 비즈니스 규칙을 보존했기를 바라야 합니다. careful하고 실수하기 쉬운 몇 주짜리 일입니다.

Bob의 프리미엄 UI Modernization 워크플로는 앱을 Spring Boot REST 백엔드와 React + Carbon 프런트엔드로 화면 단위로 쪼개고, 비즈니스 로직은 그대로 보존한다고 말합니다. 이 랩은 그걸 실제 JSF 앱에 돌리고, 양쪽이 다 빌드되는지, 로직이 정말 살아남았는지 확인합니다. 참고: 이건 **유료 프리미엄 패키지**라, 활성화된 계정에서만 실행됩니다.

## 프롬프트

### Step 1 - Java 21, Maven, Node 설치

Java 21 + Maven 은 다른 랩과 동일. 생성될 프런트엔드를 위해 **Node 18 이상**(`node -v`)도 필요합니다. <https://nodejs.org> 또는 `brew install node`.

### Step 2 - 애플리케이션 받기

**[bob-lab-bookstore-ui.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore-ui.zip)** 을 받아 압축을 풉니다. `START-HERE.md` 가 있는 폴더에서 터미널을 여세요.

**JSF 화면 4개**(`index`, 책 목록, 책 상세, 주문 확인)를 가진 **Legacy Bookstore** 입니다. 이건 주석이 붙은 빌드입니다. JSF 빈에 이미 `@ManagedBean` / `@SessionScoped` 가 있고, 산출물이 들어갈 빈 `app/back/` · `app/front/` 폴더가 준비돼 있습니다. 둘 다 필요합니다(팁 참고).

### Step 3 - 시작 상태 확인

```
mvn clean package
```

JSF 앱에서 `BUILD SUCCESS` 가 출발선입니다. 지금 UI는 `app/src/main/webapp` 아래 `.xhtml` 4개 화면입니다.

### Step 4 - UI Modernization 워크플로 실행

Bob에서 `app` 폴더를 엽니다. 워크플로를 시작해 **UI Modernization** 을 고릅니다.

```
이 프로젝트에 UI Modernization 워크플로를 돌려주세요.

백엔드: back/ 에 Spring Boot REST 엔드포인트
프런트엔드: front/ 에 React + TypeScript + Carbon Design System
모든 비즈니스 규칙을 보존해 주세요. 서비스·리포지터리 로직은 바꾸지 마세요.
화면 단위로 옮겨주세요.
```

### Step 5 - 양쪽 확인

```
cd back  && mvn -q clean package     # Spring Boot REST 백엔드 빌드
cd ../front && npm install && npm run build   # React + Vite 프런트엔드 빌드
```

그다음 비즈니스 로직이 보존됐는지 확인합니다. 서비스·리포지터리 클래스를 원본과 diff 하세요(기대 결과 참고).

## 기대 결과

- [ ] JSF 화면 4개가 **React 프런트엔드**가 됨 (실제 기록에서는 Carbon Design System 위 **React 8화면**) + **Spring Boot REST 백엔드**. `back/` 과 `front/` 둘 다 빌드됨 (기록의 `front/` 는 Vite로 **931ms** 빌드)
- [ ] **비즈니스 규칙 보존, 검증 가능하게** - 제품이 내건 건 "모든 원본 비즈니스 규칙 보존" 이고, 실제 기록에서 **서비스·리포지터리 계층이 원본과 byte-identical** 이었음. diff 해보세요. 로직은 안 움직이고 UI와 그 주변 API 계층만 바뀌어야 함
- [ ] 껍데기만 바꾼 게 아니라 진짜 분리: `.xhtml` + 관리 빈이 REST 엔드포인트 + React 컴포넌트로, 화면 단위로. 기록에서는 **67파일에 약 7,090줄**
- [ ] 양쪽이 각자 툴체인에서 빌드 초록(`back/` 은 Maven, `front/` 은 Vite)

이 애플리케이션에 프리미엄 워크플로를 실제로 돌린 기록(JSF 4화면 → React 8화면 on Carbon, 서비스/리포 byte-identical, 67파일 7,090줄, 프런트 빌드 931ms, IBM client-engineering Bob-for-Java 평가)에서 시드했습니다.

<!-- Bob-verify: 이 repo에서 아직 Bob으로 안 돌렸고 여기선 실행 불가 - UI Modernization은 우리가 접근 없는 유료 Java Modernization 프리미엄 패키지의 일부. 위 수치는 실제 기록(VALUE.md: JSF 4→React 8화면 on Carbon, back/+front/ 둘 다 빌드, 서비스/리포 byte-identical, 7090줄/67파일)에서 시드. 참가자 사용 전 실제 패키지로 한국어 프롬프트를 돌려 @ManagedBean 게이팅과 비즈니스 로직 diff가 깨끗한지 확인할 것. -->

## 팁

- 이건 프리미엄 패키지라 UI Modernization은 Java Modernization이 활성화된 계정에서 실행됩니다. 데모 전에 접근 권한을 확인하세요.
- **이 픽스처가 주석 붙은 빌드인 데는 이유가 있습니다.** JSF 관리 빈에 `@ManagedBean` 이 없으면 워크플로가 **잠깁니다.** 그래서 `bob-lab-bookstore-ui.zip` 은 주석을 미리 붙이고 빈 `back/` · `front/` 폴더까지 넣어 배포합니다. 일반 JSF 앱이라면 그걸 먼저 해야 합니다.
- **UI Modernization을 Java Upgrade보다 먼저 하세요.** 순서가 중요합니다. UI Modernization → Java Upgrade는 되지만, Java Upgrade → UI Modernization은 막힙니다. Jakarta Faces 4.0이 `@ManagedBean` 을 없애는데, 그게 바로 UI 워크플로가 필요로 하는 주석이기 때문입니다.
- **단독으로 돌리고, 업그레이드는 그다음에 계획하세요.** 단독 실행에서 워크플로는 백엔드를 Java 8에 그대로 두고 EOL Spring Boot 2.7.18을 들여왔습니다(스캔에서 취약점 수십 건). REST 분리와 버전 업그레이드는 두 단계로 다루세요. UI를 현대화하고, 그다음 업그레이드하고 스캔하세요.
- 컴파일만이 아니라 비즈니스 로직을 검증하세요. 약속은 "같은 규칙, 새 UI" 입니다. 서비스·리포지터리 계층의 diff가 그걸 확인하는 자리입니다.

## 응용

1. **업그레이드와 짝지어**: UI를 먼저 현대화하고, 새 백엔드에 Java Upgrade 랩을 돌리고, 그다음 스캔하세요. 위 함정들이 강제하는 순서입니다.
2. **Carbon 타깃**: 워크플로는 React + Carbon을 목표로 합니다. 각 JSF 화면을 어떤 Carbon 컴포넌트에 매핑하는지 물어보고 매핑을 확인하세요.
3. **우리 JSF/Struts 앱으로**: 우리가 가진 레거시 UI에 돌립니다. 관리 빈에 주석을 먼저 붙이고, 끝나면 비즈니스 계층을 diff해서 아무것도 안 움직였는지 확인하세요.
