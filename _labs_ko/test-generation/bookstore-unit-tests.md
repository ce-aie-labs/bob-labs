---
title: 테스트 불가능한 레거시 코드에 테스트 생성하기
lang: ko
category: Test Generation
difficulty: 가이드
duration: 30분
stack: Java
work_replaced: 테스트 없는 레거시 코드에 단위 테스트 스위트를 수동 작성
expected_saving: 3일 → 15분
---

## 문제

레거시 서비스에 테스트가 하나도 없습니다. 게다가 테스트하기 좋은 구조도 아닙니다. 의존성을 필드 초기화에서 `new` 로 만들고, 리포지터리는 `static` 공유 맵을 들고 있습니다. 가짜 객체를 주입할 수 있어야 좋은 테스트를 쓰는데, 그 이음새(seam)를 코드 전반에 손으로 넣는 게 다들 미루는 느리고 성가신 부분입니다.

Bob의 프리미엄 Java Unit Testing 패키지는 실제 스위트를 생성합니다. 필요한 이음새를 찾아 런타임 동작을 바꾸지 않고 넣고, 생성-실행-수정을 커버리지 목표까지 반복합니다. 이 랩은 그걸 테스트 없는 실제 앱에 돌리고, 만든 스위트가 정말 통과하는지 확인합니다. 참고: 이건 **유료 프리미엄 패키지**라, 활성화된 계정에서만 실행됩니다.

## 프롬프트

### Step 1 - Java 21 과 Maven 설치

`java -version` 이 21 이상이고 `mvn -v` 가 되면 건너뛰세요. **Windows** - <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/> 에서 Semeru 21 `.msi`(Set JAVA_HOME + Add to PATH), Maven 은 <https://maven.apache.org/download.cgi>. **macOS** - `brew install --cask semeru-jdk-open@21 && brew install maven`.

### Step 2 - 애플리케이션 받기

**[bob-lab-bookstore.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore.zip)** 을 받아 압축을 풉니다. `START-HERE.md` 가 있는 폴더에서 터미널을 여세요. 안에 든 건 **Legacy Bookstore** 입니다. Java 8, Spring MVC + JSF, 자바 16파일, 테스트 없음.

### Step 3 - 테스트가 없는 걸 확인

```
mvn test
```

빌드되고 돌지만 **테스트가 없다**고 나옵니다. 이게 시작 상태입니다.

### Step 4 - Unit Test Generation 워크플로 실행

Bob에서 `app` 폴더를 엽니다. 워크플로를 시작하고(**Start Workflow** 버튼 / `/start-java-mod` / Agent 모드), Flow Selection 화면에서 **Unit Test Generation** 을 고릅니다.

```
이 프로젝트에 Unit Test Generation 워크플로를 돌려주세요.

목표: JaCoCo 라인 커버리지 80% 이상
운영 동작을 바꾸지 마세요. 테스트가 안 되는 클래스는 로직을 다시 쓰지 말고
테스트용 이음새(예: 주입용 package-private 생성자)를 추가하세요.
테스트가 통과할 때까지 생성-실행-수정을 반복해 주세요.
```

### Step 5 - 스위트 확인

```
mvn test
```

Bob이 쓴 테스트가 돌고 통과해야 합니다. 생성된 JaCoCo 리포트(`target/site/jacoco/index.html`)를 열어 커버리지를 봅니다.

## 기대 결과

- [ ] **0 → 통과하는 완전한 스위트** (실제 기록에서는 **132개 테스트 전부 통과**). 해피패스 스텁 몇 개가 아니라
- [ ] **커버리지를 주장이 아니라 측정으로**: JaCoCo 리포트. 실제 기록은 **instruction 91% / branch 89%** 도달
- [ ] **테스트 불가 이음새를 제대로 처리** - 레거시 테스트에서 가장 어려운 부분. 빈이 의존성을 필드에서 만들던 곳(`this.bookService = new BookServiceImpl();`)에, 운영 생성자와 동작은 그대로 두고 테스트용 **주입 생성자**를 추가함:
  ```java
  public BookBean() { this.bookService = new BookServiceImpl(); }  // 운영, 그대로
  BookBean(BookService bookService) { this.bookService = bookService; }  // 테스트용
  ```
- [ ] 실제 로직(재고 차감, 주문 합계, `OrderServiceImpl.placeOrder` 의 검증)에 의미 있는 검증문. `assertNotNull` 채우기가 아니라
- [ ] 생성-실행-수정을 스스로 돌림: 처음에 실패한 테스트를 사람에게 떠넘기지 않고 Bob이 고침

이 애플리케이션(Legacy Bookstore)에 프리미엄 워크플로를 실제로 돌린 기록(0 → 132 테스트, 91% / 89% 커버리지, 5.42 credits, IBM client-engineering Bob-for-Java 평가)에서 시드했습니다.

<!-- Bob-verify: 이 repo에서 아직 Bob으로 안 돌렸고 여기선 실행 불가 - Java Unit Testing은 우리가 접근 없는 유료 프리미엄 패키지. 위 수치는 실제 기록(VALUE.md: 0→132 전부 통과, instruction 91% / branch 89%, BookBean 주입 생성자 이음새)에서 시드. 참가자 사용 전 실제 패키지로 한국어 프롬프트를 돌리고 커버리지 실측을 확인할 것. -->

## 팁

- 이건 프리미엄 패키지라 Java Unit Testing이 활성화된 계정에서만 실행됩니다. 데모 전에 접근 권한을 확인하세요.
- **테스트 스위트는 자산이지 게이트가 아닙니다.** 이 테스트들은 갖고 있으면 아주 좋지만, 서블릿 컨테이너를 띄우지는 않습니다. 실제 평가에서 테스트 0개 버전과 132개 버전이 **배포에서 똑같이 실패**했습니다. 스위트는 회귀를 잡는 데 쓰고, "뜨는가"는 배포 확인으로 잡으세요(업그레이드 랩 참고).
- 가치는 개수가 아니라 **이음새**에 있습니다. 테스트 불가 클래스를 동작을 안 건드리고 어떻게 테스트 가능하게 만들었는지 읽어보세요. 사람이라면 한참 고민했을 부분입니다.
- 가능하면 업그레이드 **전에** 돌리세요. 믿을 만한 스위트가 있으면 업그레이드가 도약이 아니라 확인된 단계가 됩니다.

## 응용

1. **먼저 안전망**: 현대화하기 **전에** 레거시 서비스에 테스트를 생성해, 업그레이드가 대조할 기준을 만들어 두세요.
2. **기준을 올려서**: `라인 커버리지 90% 이상` 으로 다시 돌려, 안 덮인 분기에 어떤 새 테스트를 쓰는지 비교하세요.
3. **우리 모듈로**: 테스트 없는 우리 모듈 하나를 지목합니다. 전략 패스를 먼저, 그다음 생성-실행-수정으로. 전체를 한 번에 말고.
