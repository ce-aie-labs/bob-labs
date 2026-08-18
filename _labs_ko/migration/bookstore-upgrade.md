---
title: 레거시 JSF 앱을 Java 21로 업그레이드하기
lang: ko
category: Migration
difficulty: 가이드
duration: 45분
stack: Java, JSF
work_replaced: JSF/서블릿 앱의 수동 Java 8 → 21 업그레이드
expected_saving: 일주일 → 한 시간
---

## 문제

Java 8 웹 애플리케이션이 하나 있습니다. Spring MVC, JSF, `javax.servlet`, WAR 패키징. 이걸 Java 21과 Jakarta EE로 올려야 합니다. 손으로 하면 의존성을 훑고, 모든 파일에서 `javax` → `jakarta` 로 바꾸고, 다 같이 옮기기 전엔 컴파일도 안 되는 프레임워크들을 한꺼번에 올리고, 그다음 깨진 걸 며칠이고 고치는 일입니다.

Bob의 프리미엄 Java Modernization 패키지는 이 업그레이드 전체를 가이드형 에이전트 워크플로로 돌립니다. 이 랩은 그 워크플로를 실제 레거시 앱에 물리고, 워크플로가 안 하는 방식으로 결과를 확인합니다. 바로 "업그레이드된 앱이 실제로 뜨는가" 입니다. 참고: 이건 **유료 프리미엄 패키지**라, 해당 패키지가 활성화된 계정에서만 워크플로가 실행됩니다.

## 프롬프트

### Step 1 - Java 21 과 Maven 설치

`java -version` 이 이미 21 이상이고 `mvn -v` 가 되면 건너뛰세요.

**Windows** - <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/> 에서 **Version 21 · Windows · x64** `.msi` 를 받아 실행하고 **Set JAVA_HOME** 과 **Add to PATH** 를 켠 뒤 터미널을 새로 여세요. Maven 은 <https://maven.apache.org/download.cgi>.

**macOS** - `brew install --cask semeru-jdk-open@21 && brew install maven`.

### Step 2 - 애플리케이션 받기

**[bob-lab-bookstore.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore.zip)** 을 받아 압축을 풉니다. `START-HERE.md` 가 있는 폴더에서 터미널을 여세요.

안에 든 건 **Legacy Bookstore** 입니다. Java 8, Spring MVC 4.3, JSF 2.2, Hibernate 5.4, H2 1.4.200, 자바 16파일, WAR 패키징.

### Step 3 - 시작 상태 확인

```
mvn clean package
```

`BUILD SUCCESS` 가 나오면 출발선에 선 겁니다. Java 8 타깃으로 컴파일되고, 아직 `javax` · Spring 4 · JSF 2.2 그대로입니다. 워크플로가 옮길 대상이 바로 이겁니다.

### Step 4 - Java Upgrade 워크플로 실행

Bob에서 `app` 폴더를 엽니다. **Start Workflow** 버튼이나 `/start-java-mod` 스킬, 또는 Agent 모드로 워크플로를 시작하고, **Flow Selection** 화면에서 **Java Upgrade** 를 고릅니다. 설정 화면에 이렇게 답합니다.

```
Java Distribution: Temurin (Eclipse)
Java Version: 21
Jakarta EE Version: Jakarta EE 10
Build tool: Maven

동작을 약화시키지 마세요. 컴파일을 위한 javax → jakarta 변환과 API 교체는 괜찮지만,
로직을 지우지는 마세요. 빌드가 통과할 때까지 에이전트 수정 루프를 돌려주세요.
```

이름 붙은 단계들과 수정 루프가 도는 걸 그대로 두세요. 빌드가 실패해도 억지로 넘기지 마세요. 그 수정 루프가 이 패키지의 핵심입니다.

### Step 5 - "끝났다"의 의미를 확인

Bob이 성공했다고 해도 멈추지 마세요. 직접 다시 빌드합니다.

```
mvn clean package
```

빌드가 초록이라고 앱이 뜨는 게 **아닙니다.** 이 앱은 서블릿 컨테이너에서 떠야 하는 WAR이고, 그건 컴파일과는 별개의 사실입니다. 기대 결과를 보세요.

## 기대 결과

- [ ] 이 앱의 실제 스택을 짚고 한 세트로 옮김: **Java 8 → 21 (Temurin)**, **Spring 4.3.30 → 6.1.x**, **Hibernate 5.4 → 6.4**, **JSF 2.2 → 4.0.22 (+ Weld CDI)**, **H2 1.4.200 → 2.2.220**, **log4j 1.2.17 → SLF4J + Logback**. "21로 올림" 같은 일반론이 아니라
- [ ] **`javax` → `jakarta`** 변환을 코드 전반(servlet · faces · persistence)에 OpenRewrite 레시피로 한 번에 적용하고 diff로 보여줌. 말로만이 아니라
- [ ] 이름 붙은 워크플로 단계와 **에이전트 수정 루프**(빌드 → 진단 → 수정 반복), 그리고 워크플로 자체 산출물(피처 브랜치·커밋, 단계 다이어그램)
- [ ] 업그레이드 후에도 **`mvn clean package` 통과** - Java 21에서 컴파일됨
- [ ] **함정이자 이 랩의 핵심**: 빌드가 초록이라고 앱이 뜬 게 아니었음. 실제 기록에서 Bob은 *"Build completed successfully with no errors or warnings"* 라고 했지만 WAR이 Tomcat 10.1에서 기동 실패(context startup failed, HTTP 404)했음. 그 기동 **스택 트레이스**를 Bob에 넘기자 근본 원인(CDI / `@Named` 배선 문제)을 **한 번의 왕복**으로 고쳤음. 워크플로는 변환은 잘하지만 자기 결과를 검증하지 않으니, 게이트는 사람이 대는 것

이 애플리케이션(Legacy Bookstore)에 프리미엄 워크플로를 실제로 돌린 기록(IBM client-engineering Bob-for-Java 평가)에서 시드했습니다. 프리미엄 [Java Modernization 패키지](https://bob.ibm.com/docs/ide/premium-packages/java-modernization/java-modernization-index) 참고.

<!-- Bob-verify: 이 repo에서 아직 Bob으로 안 돌렸고, 여기선 실행 불가 - Java Modernization은 우리가 접근 없는 유료 프리미엄 패키지. 위 수치는 실제 기록(VALUE.md: Java 8→21, Spring 4.3.30→6.1.21, JSF 2.2→4.0.22, 빌드는 성공인데 404였다가 스택트레이스로 수정)에서 시드. 참가자 사용 전 실제 패키지로 한국어 프롬프트를 돌려 Flow Selection 문구와 수정 루프가 배포 실패를 해결하는지 확인할 것. -->

## 팁

- 이건 프리미엄 패키지입니다. Java Upgrade 워크플로는 Java Modernization이 활성화된 계정에서만 실행됩니다. 데모 전에 접근 권한부터 확인하세요. 아니면 프롬프트가 아무것도 안 합니다.
- **빌드 성공은 배포 성공이 아닙니다.** 이 랩의 교훈이 그겁니다. Bob은 빌드가 통과했다고 하는데 컨테이너는 여전히 404를 냅니다. 끝났다고 하기 전에 WAR을 배포해 보거나(패키지의 배포 확인 기능을 쓰거나), 기동 실패가 나면 스택 트레이스로 되먹여 주세요. 그게 고쳐지는 길입니다.
- **워크플로는 하나씩 돌리세요.** Java Upgrade가 취약점 수정으로 자동 체인되게 두면 결과가 흐려집니다(취약점 랩 참고). 업그레이드를 먼저 끝내고 확인하세요.
- Bob이 일반론만 내놓으면 `pom.xml` 과 실제 `javax` import 위치를 대라고 하세요. 그때 찾아봅니다.

## 응용

1. **Liberty 리플랫폼**: AMA 마이그레이션 번들과 함께 `Liberty Replatforming 워크플로를 돌려주세요.` WebSphere 앱을 Open Liberty로 옮기고, `server.xml` 을 생성·검증하고, `liberty-maven-plugin` 으로 배포합니다. 이 앱이 아니라 WebSphere 형 앱이 필요합니다.
2. **끝나면 스캔**: 업그레이드가 착지하고 뜨면 그 결과에 취약점 랩을 돌려보세요. 최신 스택에도 CVE는 남습니다.
3. **우리 WAR로**: 우리가 가진 Java 8/11 서비스에 같은 워크플로를 돌립니다. 두 규칙(동작 약화 금지, 기동 확인)은 그대로 두세요. 결과를 리뷰할 수 있게 만드는 게 그 둘입니다.
