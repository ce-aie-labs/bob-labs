---
title: Security Expert Mode로 코드베이스 보안 감사하기
lang: ko
category: Bob Features
difficulty: 가이드
duration: 25분
stack: Any
work_replaced: 코드베이스 전체에 보안 규칙을 수동 적용
expected_saving: 2시간 → 25분
---

## 문제

보안 체크리스트는 코드 전체에 일관되게 적용되고 엔지니어가 검토할 증거를 남길 때 유용하다. 회사 규칙을 목적별 Skill로 만들고, 코드베이스 전체에 pipeline으로 적용하며, 소스 코드는 바꾸지 않고 개선 보고서만 작성하는 프로젝트 전용 Custom Mode를 구성한다.

## 프롬프트

Bob이 `.bob/`을 만들 수 있는 작업 디렉터리에서 네 단계를 실행한다. 먼저 Bob의 기본 모드와 자동 승인 설정을 확인하고, 감사를 신뢰하거나 사용하기 전에 생성하거나 수정한 제어 항목부터 검토할 것.

### 0단계 - Bob 알아보기

Bob은 기본 모드인 **Agent**, **Plan**, **Ask**를 제공한다. 각 모드는 역할과 사용 목적, 동작 지침, 사용할 수 있는 도구가 다르다. 실습을 시작하기 전에 기본 모드의 구성과 도구 자동 승인 상태를 확인한다.

<details markdown="1">
<summary>설정에서 기본 모드 확인하기</summary>

1. Bob 화면 오른쪽 위의 톱니바퀴 모양을 눌러 **Bob 설정**을 연다.
2. 왼쪽 메뉴에서 **모드** 탭을 누른다.
3. 기본 제공되는 **Agent**, **Plan**, **Ask** 모드가 표시되는지 확인한다.

![Bob 설정의 모드 탭에 표시된 Agent, Plan, Ask 기본 모드](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step0_1.png)

각 모드의 이름을 누르면 세부 설정을 확인할 수 있다. **역할 정의**, **사용 시기**, **커스텀 지침**, **사용 가능한 도구**를 차례로 살펴보고 모드마다 Bob의 역할과 작업 범위가 어떻게 달라지는지 확인한다.

![Plan 모드의 역할 정의, 사용 시기, 커스텀 지침, 사용 가능한 도구](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step0_2.png)

- **Agent**: 파일 편집과 명령 실행 등 실제 구현 작업을 수행할 때 사용한다.
- **Plan**: 코드를 변경하기 전에 요구사항을 분석하고 구현 순서를 설계할 때 사용한다.
- **Ask**: 코드나 개념을 질문하고 설명을 받을 때 사용한다.

</details>

<details markdown="1">
<summary>채팅에서 모든 도구 자동 승인 켜기</summary>

1. 같은 **Bob 설정** 화면의 왼쪽 메뉴에서 **채팅** 탭을 누른다.
2. **자동 승인 켜기/끄기** 목록에서 각 도구의 버튼을 누른다.
3. **읽기**, **편집**, **실행**, **MCP**, **모드**, **하위 작업**, **서브에이전트**, **스킬**, **할 일** 등 표시된 모든 항목의 자동 승인을 켠다.
4. 접혀 있는 항목은 화살표를 눌러 하위 도구의 자동 승인도 모두 켜졌는지 확인한다.

![Bob 설정의 채팅 탭에서 모든 도구의 자동 승인을 켠 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step0_3.png)

자동 승인을 켜면 Bob이 도구를 사용할 때마다 승인을 기다리지 않아 hands-on을 끊김 없이 진행할 수 있다. 자동 승인은 Bob이 사용자 확인 없이 도구를 실행하도록 허용하므로 신뢰할 수 있는 실습 프로젝트에서만 사용한다. 이번 세션에서는 진행을 위해 모든 도구의 자동 승인을 켜고, 세션이 끝나면 필요한 항목만 남기거나 다시 끈다.

</details>

**체크포인트:** Agent, Plan, Ask 모드의 세부 내용을 각각 열어 보고, **설정 > 채팅**에서 표시된 모든 도구와 하위 도구의 자동 승인이 켜졌는지 확인한다.

여기서 Mode는 Bob의 말투만 바꾸는 설정이 아니라 **역할, 행동 지침, 사용할 수 있는 도구**를 함께 제한한다는 점을 확인할 수 있다. 도구 설정이 "Bob이 무엇을 할 수 있는가"를 정한다면 자동 승인은 "허용된 작업을 실행하기 전에 언제 사용자에게 물어볼 것인가"를 정한다. 실습이 끝나면 자동 승인을 다시 줄이고 실제 업무에서는 필요한 Mode, 도구, 권한만 허용한다.

### 1단계 - 프로젝트 전용 보안 환경 만들기

**Agent 모드**에서 프로젝트 전용 보안 Mode를 생성한다. 아래 프롬프트 전체를 복사한 뒤 Bob의 새 작업 입력창에 붙여 넣고 실행한다.

```
이 프로젝트에서 보안전문가 mode가 필요해.
이 모드는 각 보안 감사 스킬들로 구성되어 모드 내의 pipeline 을 따라서 코드를 감사하고 취약점 및 개선코드 레포트를 작성해야해
이를위해 보안 규칙을 정하고 프로젝트 내의 모든 코드들이 보안규칙을 준수하였는지를 판단해야해.
어떤 코드가 어떤 규칙을 위반했는지와 개선한 코드를 레포트 형식으로 취약점_및_개선코드.md 파일로 작성할 수 있어야해.

우리회사의 보안 규칙은
1. 비밀번호 보호에 MD5 또는 SHA-1을 사용하지 않는다.
2. NIST SP800-53 / OWASP ASVS Level1 / CWE Top25 를 준수한다.

이걸 반영한 필요한 skill 들과 mode 와 보안 규칙을 만들어줘

생성하는 모든 스킬과 파일들은 한글로 작성해줘
```

<details markdown="1">
<summary>프롬프트 복사 및 붙여 넣기</summary>

![1단계 프롬프트를 복사해 Bob의 새 작업 입력창에 붙여 넣는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step1_1.png)

</details>

Bob이 작업을 마칠 때까지 기다린다. 오른쪽의 할 일 목록에서 Mode 설계, 각 Skill 작성, Custom Mode 생성, 최종 검증이 모두 완료됐는지 확인한다.

<details markdown="1">
<summary>생성 결과</summary>

![Bob이 생성한 보안 감사 Skill과 Security Expert Custom Mode 결과](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step1_2.png)

</details>

실행 결과로 프로젝트의 `.bob/` 디렉터리에 다음 파일을 얻는다:

```text
.bob/custom_modes.yaml
.bob/skills/code-security-scan/SKILL.md
.bob/skills/security-rule-validate/SKILL.md
.bob/skills/vulnerability-report/SKILL.md
```

- `code-security-scan`: 프로젝트 전체 코드를 스캔해 보안 위반 후보를 찾는다.
- `security-rule-validate`: 발견한 코드가 회사 규칙과 보안 기준을 위반했는지 검증한다.
- `vulnerability-report`: 확인한 취약점, 위반 규칙, 심각도, 개선 코드를 보고서로 정리한다.
- `custom_modes.yaml`: 세 Skill을 순서대로 사용하는 **Security Expert** (`security-expert`) Mode를 정의한다.

생성된 파일의 이름과 개수는 Bob 버전에 따라 달라질 수 있다. 파일 개수보다 전체 코드 스캔, 규칙 검증, 보고서 작성이 각각 Skill로 분리되고 Custom Mode에서 순서대로 연결됐는지를 확인한다.

이 단계에서는 실제 업무를 구체적으로 설명하면 Bob이 이를 재사용 가능한 프로젝트 자산으로 구조화할 수 있다는 점을 확인한다. Skill은 반복 절차를 나누어 저장하고, `custom_modes.yaml`은 역할, 지침, 도구 범위와 Skill 실행 순서를 하나의 작업 방식으로 연결한다. 생성된 `.bob/` 파일은 일회성 채팅 답변과 달리 사람이 검토하고, 버전 관리하고, 팀과 공유할 수 있다.

**체크포인트:** Bob이 **Security Expert** (`security-expert`)를 만들었는지 확인하고, 생성된 모든 파일을 직접 연다. Mode가 전체 코드 스캔, 규칙 검증, 취약점 보고서 작성 순서로 동작하는지 확인한다. Edit 권한은 `취약점_및_개선코드.md` 같은 보고서 파일에만 허용하고 소스 코드 수정 권한은 포함하지 않는다. 다음 단계 전에 Mode picker에서 **Security Expert**를 선택한다.

### 2단계 - 동작하는 예제 프로젝트 감사하기

1단계에서 만든 Custom Mode와 Skill은 새 Bob 세션을 시작할 때 로드된다. 오른쪽 Bob 채팅 위쪽의 `X`를 눌러 기존 채팅을 닫고 새 작업 입력창이 표시되는지 확인한다. 기존 채팅에 이어서 입력하지 않는다.

<details markdown="1">
<summary>기존 채팅 닫기</summary>

![새로 만든 Custom Mode와 Skill을 로드하기 위해 기존 Bob 채팅을 닫는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_0.png)

</details>

같은 작업 디렉터리에서 새 터미널을 연다. 상단 메뉴에서 **터미널 > 새 터미널**을 누르거나 터미널 패널의 `+`를 누른다.

<details markdown="1">
<summary>새 터미널 열기</summary>

![예제 프로젝트를 받을 새 터미널을 여는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_1.png)

</details>

예제 branch를 아직 받지 않았다면 새 터미널에서 다음 명령을 실행한다:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

명령이 끝나면 왼쪽 탐색기에 `galaxium-travels` 디렉터리와 하위 파일이 표시되는지 확인한다.

<details markdown="1">
<summary>예제 프로젝트 Clone 결과</summary>

![터미널에서 예제 branch를 clone하고 탐색기에서 galaxium-travels 프로젝트를 확인하는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_3.png)

</details>

Bob의 새 작업 입력창에 다음 프롬프트를 붙여 넣고 실행한다:

```
galaxium-travels 라는 프로젝트에 보안 점검이 필요해
```

<details markdown="1">
<summary>보안 점검 프롬프트 붙여 넣기</summary>

![보안 점검 프롬프트를 복사해 Bob의 새 작업 입력창에 붙여 넣는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_2.png)

</details>

Bob이 새로 로드한 **Security Expert** (`security-expert`) Mode로 전환하고 보안 점검을 시작하는지 확인한다. 할 일 목록에서 대상 파일 수집, 규칙별 pattern 검색, 탐지 결과 정리, scan 결과 보고, report 작성이 차례로 진행된다. 작업 중에는 Bob 채팅을 닫거나 세션을 바꾸지 않는다.

<details markdown="1">
<summary>Security Expert Mode 실행 과정</summary>

![Security Expert Mode로 전환한 뒤 다섯 단계의 보안 점검을 진행하는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_4.png)

</details>

할 일 목록은 저장된 Skill 절차가 현재 어느 단계까지 실행됐는지를 보여 준다. 대상 파일 수집부터 보고서 작성까지 빠진 단계나 멈춘 단계가 없는지 확인할 수 있어, Bob의 작업 과정을 관찰 가능한 workflow로 만든다.

할 일 목록이 5/5로 완료되면 `galaxium-travels/취약점_및_개선코드.md`를 연다. 요약의 심각도별 건수와 각 항목의 파일, line, 위반 규칙, 설명, 개선 코드를 확인한다.

<details markdown="1">
<summary>보안 점검 보고서 결과</summary>

![보안 점검을 완료하고 취약점 및 개선 코드 보고서를 생성한 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step2_5.png)

</details>

**체크포인트:** `취약점_및_개선코드.md`의 각 항목에서 파일과 line이 실제 코드와 일치하는지, 규칙이 해당 코드에 적용되는지, 심각도와 개선 코드가 적절한지, 해당 파일이 실제 검사 범위에 포함됐는지를 확인한다. 보고서는 확정된 취약점 목록이 아니라 검토 입력으로 다룬다.

일반 Agent 대신 검토한 Security Expert Mode를 사용하면 역할, 도구 범위, 실행 절차를 매번 프롬프트에 다시 적지 않아도 같은 조건으로 감사를 시작할 수 있다. 이는 절차의 일관성과 수동 검색 효율을 높이지만, 생성되는 문장이나 finding이 실행할 때마다 완전히 같아진다는 뜻은 아니다. 최종 판단은 근거 코드를 확인한 사람이 내린다.

### 3단계 - Security Expert Mode 수정 및 관리하기

기존 **Security Expert** Mode는 보안 감사와 보고서 작성에 필요한 권한만 가진다. Mode와 Skill 파일을 수정하려면 편집 권한이 있는 **Agent 모드**를 사용해야 한다. 오른쪽 Bob 채팅 위쪽의 `X`를 눌러 보안 점검 세션을 닫고, 새 작업 입력창 아래에 **Agent**가 선택됐는지 확인한다.

<details markdown="1">
<summary>Agent 모드로 새 세션 열기</summary>

![Security Expert 세션을 닫고 Agent 모드의 새 작업 입력창을 여는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step3_0.png)

</details>

회사 보안 정책 변경을 반영하기 위해 다음 프롬프트를 복사해 Bob의 새 작업 입력창에 붙여 넣고 실행한다:

```
보안 전문가 모드를 수정해야해

비밀번호 보호에는 사내 프로토콜을 사용할 것이기 때문에 MD5와 SHA-1을 사용하더라도 사내 프로토콜을 통해 보호되므로 코드상 사용을 허용할거야. 대신 Session Cookie에는 Secure, HttpOnly, SameSite 등의 보안 속성을 적용하고, 로그인 성공 시 Session ID를 재생성하며, 로그아웃 또는 세션 만료 시 해당 세션을 무효화하는 등의 세션 관련 보안 규칙을 추가해야해
```

<details markdown="1">
<summary>정책 변경 프롬프트 붙여 넣기</summary>

![보안 정책 변경 프롬프트를 복사해 Agent 모드의 새 작업 입력창에 붙여 넣는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step3_1.png)

</details>

Bob이 현재 Mode와 Skill을 읽고 정책 변경이 필요한 파일을 식별하는지 확인한다. 이번 실행에서는 `custom_modes.yaml`과 보안 스캔, 규칙 검증, 보고서 작성 Skill까지 총 4개 파일을 수정한다.

<details markdown="1">
<summary>수정 대상 분석</summary>

![Agent 모드가 보안 정책 변경이 필요한 네 개 파일을 분석하는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step3_2.png)

</details>

작업이 4/4로 완료되면 `.bob/custom_modes.yaml`과 `.bob/skills/` 아래의 수정된 Skill을 직접 연다. MD5와 SHA-1 허용 조건이 명시되고 세션 보안 규칙과 탐지 항목이 추가됐는지 확인한다.

<details markdown="1">
<summary>Mode와 Skill 수정 결과</summary>

![수정된 Security Expert Mode의 세션 보안 규칙과 변경된 네 개 파일을 확인하는 화면](https://raw.githubusercontent.com/ce-aie-labs/bob-labs/main/assets/images/security-expert-mode/step3_3.png)

</details>

**체크포인트:** `.bob/custom_modes.yaml`, `.bob/skills/code-security-scan/SKILL.md`, `.bob/skills/security-rule-validate/SKILL.md`, `.bob/skills/vulnerability-report/SKILL.md`의 변경 내용을 검토한다. MD5와 SHA-1 허용이 사내 프로토콜을 통한 비밀번호 보호에만 적용되는지 확인한다. Session Cookie의 Secure, HttpOnly, SameSite 속성, 로그인 성공 후 Session ID 재생성, 로그아웃 또는 만료 시 세션 무효화가 구체적인 탐지 pattern, 검증 항목, 개선 코드로 연결됐는지도 확인한다.

이 단계에서는 자연어로 전달한 정책 변경을 Bob이 관련 Mode와 Skill 전체의 수정으로 연결할 수 있다는 점을 확인한다. 여러 파일을 사람이 하나씩 찾아 고치는 수고는 줄어들지만, 변경의 정확성을 보장하는 것은 프롬프트가 아니라 검토다. 실제 diff에서 규칙의 의미, Skill 실행 순서, 보고서 형식, 도구 권한이 의도하지 않게 바뀌지 않았는지 확인한 뒤 변경을 승인한다.

## 기대 결과

실제 한국어 실행에서는 프로젝트 전용 Security Expert Mode와 세 개의 Skill이 생성됐다:

- [ ] `.bob/custom_modes.yaml`: 역할, 지침, 허용 도구와 세 Skill의 실행 순서를 정의한다.
- [ ] `.bob/skills/code-security-scan/SKILL.md`: 감사 대상 파일을 수집하고 보안 위반 후보를 찾는다.
- [ ] `.bob/skills/security-rule-validate/SKILL.md`: 후보가 회사 규칙과 선택한 보안 기준을 위반하는지 검증한다.
- [ ] `.bob/skills/vulnerability-report/SKILL.md`: 파일과 line, 위반 규칙, 설명, 심각도, 개선 코드를 보고서로 정리한다.

Security Expert Mode를 새 세션에서 실행하면 할 일 목록에 다음 다섯 단계가 표시되고 순서대로 완료된다:

1. 대상 파일 수집
2. 규칙별 pattern 검색
3. 탐지 결과 정리
4. scan 결과 보고
5. report 작성

- [ ] `galaxium-travels/취약점_및_개선코드.md`: 문제 위치와 코드, 적용 규칙, 제안된 개선 코드를 함께 기록한다.

정책 변경 실행에서는 다음 네 파일이 함께 수정됐다:

- [ ] `.bob/custom_modes.yaml`: 변경된 보안 정책과 기존 report-only 도구 범위를 유지한다.
- [ ] `.bob/skills/code-security-scan/SKILL.md`: Session Cookie, Session ID 재생성, 세션 무효화와 관련된 탐지 항목을 추가한다.
- [ ] `.bob/skills/security-rule-validate/SKILL.md`: 사내 프로토콜로 보호되는 비밀번호 목적의 MD5와 SHA-1만 허용하고, 세션 규칙 검증 기준을 추가한다.
- [ ] `.bob/skills/vulnerability-report/SKILL.md`: 새 세션 규칙의 근거와 개선 코드를 보고서에 기록하도록 갱신한다.

Bob의 예제 감사 보고서는 **8개 finding**을 제시했다. 하드코딩된 설정 1개, 응답 정보 노출 2개, 인증 및 인가 부재, network binding, CORS, container 실행, dependency pinning과 관련된 항목 5개였다. 이 수치는 Bob이 생성한 보고서 내용이지 독립적으로 확정된 취약점 수가 아니다.

보고서에는 유용한 조사 출발점도 있었다. 예를 들어 `services/booking.py:34`에서 error response가 다른 사용자의 저장된 이름을 반환하는 코드를 인용하고, 관련 규칙과 연결한 뒤 이름을 제외한 generic response를 제안했다. 예약 endpoint의 인증 및 ownership 검사 부재도 우선순위가 높은 수동 검토 대상이다.

이 Mode는 검토된 역할, 도구 범위, 규칙과 workflow를 반복 적용한다. 결과 문구나 finding을 매번 동일하게 만들거나 악용 가능성을 증명하지 않으며, SAST, dependency scanning, DAST, threat modeling, exploit 검증, 사람의 보안 리뷰를 대체하지 않는다.

## 팁

- 첫 감사 전에 `.bob/custom_modes.yaml`과 모든 Skill을 검토한다. 표준 이름을 넓게 적는 것만으로 완전하고 테스트 가능한 control set이 되지는 않는다.
- 인증이 필요한 endpoint, resource별 접근 주체, TLS와 network 노출이 강제되는 위치를 명시한다. 이런 trust boundary는 분리된 코드 pattern만으로 안정적으로 추론할 수 없다.
- 검토 Mode는 report-only로 유지한다. 소스 수정은 별도의 Plan mode 또는 승인 gate가 있는 개선 workflow로 분리한다.
- 모든 finding에 정확한 파일, 현재 line, 관련 context, 규칙 원문, confidence, 검증 방법을 요구한다.
- 실제 실행된 도구를 기록한다. LLM 판단, regex match, SAST 결과, dependency advisory, 재현된 exploit은 서로 다른 증거다.
- `.bob/` 파일을 코드와 함께 version control하면 검토한 Mode와 Skill을 팀의 재사용 가능한 작업 자산으로 공유할 수 있다.

## 응용

1. **Skill 하나만 실행**: pre-commit 검토에서 `code-security-scan`, `security-rule-validate` 등 필요한 절차만 호출한다.
2. **stack별 제어 항목 추가**: 같은 보고서 contract를 유지하면서 검토된 FastAPI, Spring Security, Express, container 규칙을 추가한다.
3. **scanner로 검증**: Mode 실행 뒤 SAST와 dependency scan을 수행하고 각 finding을 확인, 기각, 수동 검토 필요로 표시한다.
4. **승인 후 개선**: 검토를 통과한 보고서 항목만 받아 계획을 제안하고, 승인 전에는 소스 파일을 수정하지 않는 별도 Mode를 만든다.
5. **외부 맥락과 전문 작업 결합**: Skill에 MCP server를 연결해 issue tracker, 사내 문서, database의 맥락을 불러오거나 보안, 테스트, 문서화 작업을 Subagent로 나눈다. Custom Mode에는 필요한 Skill, MCP server, Tool, Subagent와 최소 권한만 허용한다.

**반복 작업은 Bob, 마지막 검토는 사람.**
