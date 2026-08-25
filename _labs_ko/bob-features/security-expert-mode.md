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

Bob이 `.bob/`을 만들 수 있는 작업 디렉터리에서 세 단계를 실행한다. 감사를 신뢰하거나 사용하기 전에 생성하거나 수정한 제어 항목부터 검토할 것.

### 1단계 - 프로젝트 전용 보안 환경 만들기

**Agent 모드**에서 실제 사용한 다음 프롬프트를 그대로 실행한다:

```
이 프로젝트에서 보안전문가 mode가 필요해 프로젝트의 보안 규칙을 먼저 만들고 해당 모드가 프로젝트 내의 모든 코드들에 대해서 보안규칙을 준수하였는지를 판단해야해. 그리고 어떤 문서의 어떤 코드가 어떤 문제점이 있는지 어떤 규칙을 어겼는지 판단하고 개선 코드를 제안할 수 있어야해.

우리회사의 보안 규칙은
1. 비밀번호 보호에 MD5 또는 SHA-1을 사용하지 않는다.
2. NIST SP800-53 / OWASP ASVS Level1 / CWE Top25 를 준수해야해

이정도고 이외에 최소 보안 정책이 있다면 추가 되어야해.

이 모드는 각 스킬들과 pipeline 으로 구성되어야해 규칙은 규칙이고 각 규칙을 판단하기 위해서 구체적으로 기술된 skill이 따로 있어야해 pipeline 은 코드를 읽고 정해진 규칙을 순차적으로 적용하여 코드를 평가하고 모든 규칙이 통과하면 다음 코드로 넘어가고 아니면 취약점_및_개선코드.md 에 작성되어야해.
작성할때는 {문제가 되는 위차와 코드 + 통과하지 못한 규칙 + 개선코드}가 세트가 되어서 작성이 되어야해. 이렇게 전체 코드를 대상으로 진행되어야해.
이걸 반영한 필요한 skill 들과 mode 와 보안 규칙을 만들어줘
```

**체크포인트:** Bob이 **Security Expert** (`security-expert`)를 만들었는지 확인하고, 사용하기 전에 생성된 파일을 모두 읽는다:

```text
.bob/custom_modes.yaml
.bob/rules/security.md
.bob/skills/secret-scan/SKILL.md
.bob/skills/crypto-weakness/SKILL.md
.bob/skills/info-disclosure-check/SKILL.md
.bob/skills/compliance-check/SKILL.md
.bob/skills/security-audit-pipeline/SKILL.md
```

실제 생성된 Mode는 Read를 허용하되 다음 도구 규칙으로 Edit 대상을 보고서 하나로 제한했다:

```yaml
- - edit
  - fileRegex: "^취약점_및_개선코드\\.md$"
```

더 넓은 Edit 권한이 없는지 확인한다. 다음 단계 전에 Mode picker에서 **Security Expert**를 선택한다.

### 2단계 - 동작하는 예제 프로젝트 감사하기

같은 작업 디렉터리에서 예제 branch를 아직 받지 않았다면 clone한다:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

**Security Expert** Mode에서 실제 사용한 다음 프롬프트를 실행한다:

```
galaxium-travels 라는 프로젝트에 보안 점검이 필요해
```

**체크포인트:** `취약점_및_개선코드.md`를 연다. 각 항목의 파일과 line, 규칙 적용 가능성, 심각도, 개선 코드, 해당 파일이 실제 검사 범위에 포함됐는지를 확인한다. 보고서는 확정된 취약점 목록이 아니라 검토 입력으로 다룬다.

### 3단계 - Security Expert Mode 수정 및 관리하기

회사 보안 정책이 바뀌면 **Agent 모드**에서 실제 사용한 다음 프롬프트로 기존 Mode를 수정한다:

```
보안 전문가 모드를 수정해야해

비밀번호 보호에는 사내 프로토콜을 사용할 것이기 때문에 MD5와 SHA-1을 사용하더라도 사내 프로토콜을 통해 보호되므로 코드상 사용을 허용할거야. 대신 Session Cookie에는 Secure, HttpOnly, SameSite 등의 보안 속성을 적용하고, 로그인 성공 시 Session ID를 재생성하며, 로그아웃 또는 세션 만료 시 해당 세션을 무효화하는 등의 세션 관련 보안 규칙을 추가해야해
```

**체크포인트:** `.bob/rules/security.md`, `.bob/skills/crypto-weakness/SKILL.md`, `.bob/skills/compliance-check/SKILL.md`의 변경 내용을 검토한다. MD5와 SHA-1 예외가 승인된 사내 프로토콜을 통한 비밀번호 보호에만 적용되고, 그 밖의 보안 목적 사용은 계속 금지되는지 확인한다. Session Cookie 속성, 로그인 성공 후 Session ID 재생성, 로그아웃 또는 만료 시 세션 무효화가 각각 구체적인 탐지 pattern과 개선 지침으로 연결됐는지도 확인한다.

## 기대 결과

실제 한국어 실행에서는 프로젝트 전용 Mode와 규칙 Skill 네 개, orchestration Skill 한 개가 생성됐다:

- [ ] SR-01 → `secret-scan`: 하드코딩된 credential과 secret
- [ ] SR-02 → `crypto-weakness`: MD5·SHA-1 사용과 사용 맥락
- [ ] SR-03 → `info-disclosure-check`: 클라이언트 응답의 민감 정보
- [ ] SR-04 → `compliance-check`: NIST SP800-53, OWASP ASVS Level 1, CWE Top 25 중 선택된 제어 항목
- [ ] `security-audit-pipeline`: 보고서 초기화 → 파일 수집 → 파일마다 SR-01~SR-04 적용 → 결과 집계
- [ ] `취약점_및_개선코드.md`: 문제 위치와 코드 + 통과하지 못한 규칙 + 제안된 개선 코드를 한 세트로 기록

Mode 수정 결과 기존 파일 3개에 정책 변경이 반영됐다:

- [ ] `.bob/rules/security.md`
  - SEC-02에서 사내 프로토콜을 통한 비밀번호 보호 목적의 MD5와 SHA-1을 예외로 허용했다.
  - SEC-02의 금지 pattern을 `random` module과 Session ID, token 등 비밀번호 이외 보안 목적의 MD5 또는 SHA-1 사용으로 재정의하고, finding을 보고하기 전에 사용 목적을 구분하도록 했다.
  - SEC-04c에서 Secure, HttpOnly, SameSite 속성을 모두 필수로 지정하고, 로그인 성공 후 Session ID 재생성, 로그아웃 또는 만료 시 서버 측 세션 무효화 규칙을 추가했다.
  - SEC-04c에 허용 및 금지 Flask 예제 코드를 추가했다.
- [ ] `.bob/skills/crypto-weakness/SKILL.md`
  - 사내 프로토콜이 적용된 비밀번호 목적의 MD5와 SHA-1을 위한 `EXEMPT` 등급을 추가했다.
  - Pattern Group A와 B의 context filter를 `EXEMPT`, `CRITICAL`, `LOW`로 세분화했다.
  - Step 3 수집 기준에 보고하지 않는 `EXEMPT` case를 추가했다.
  - Step 4에서 비밀번호 목적의 변경은 권장 사항으로만 제시하고, 비밀번호 이외 보안 목적 사용에 대한 개선 코드를 추가했다.
- [ ] `.bob/skills/compliance-check/SKILL.md`
  - Session Cookie pattern에 SameSite 미설정을 trigger로 추가했다.
  - CWE-384 Session Fixation 탐지 heuristic을 포함한 Session ID 재생성 검사를 추가했다.
  - 로그아웃 및 만료 시 Session 무효화 검사를 추가했다.
  - Step 8에 Session Fixation과 세션 무효화 개선 코드를 추가했다.
  - Step 9 결과 형식에 `cookie_flags`, `session_fixation`, `session_invalidation`, `jwt_verify` 중 하나를 기록하는 `sub_issue` field를 추가했다.

Bob의 예제 감사 보고서는 **8개 finding**을 제시했다. 하드코딩된 설정 1개, 응답 정보 노출 2개, 인증·인가 부재, network binding, CORS, container 실행, dependency pinning과 관련된 항목 5개였고 SR-02 결과는 0개였다. 이 수치는 Bob이 생성한 보고서 내용이지 독립적으로 확정된 취약점 수가 아니다.

보고서에는 유용한 조사 출발점도 있었다. 예를 들어 `services/booking.py:34`에서 error response가 다른 사용자의 저장된 이름을 반환하는 코드를 인용하고, SR-03과 연결한 뒤 이름을 제외한 generic response를 제안했다. 예약 endpoint의 인증 및 ownership 검사 부재도 우선순위가 높은 수동 검토 대상이다.

이 Mode는 반복 가능한 LLM·규칙 기반 검토를 제공한다. 악용 가능성을 증명하거나 SAST, dependency scanning, DAST, threat modeling, exploit 검증, 사람의 보안 리뷰를 대체하지 않는다.

## 팁

- 첫 감사 전에 `.bob/rules/security.md`와 모든 Skill을 검토한다. 표준 이름을 넓게 적는 것만으로 완전하고 테스트 가능한 control set이 되지는 않는다.
- 인증이 필요한 endpoint, resource별 접근 주체, TLS와 network 노출이 강제되는 위치를 명시한다. 이런 trust boundary는 분리된 코드 pattern만으로 안정적으로 추론할 수 없다.
- 검토 Mode는 report-only로 유지한다. 소스 수정은 별도의 Plan mode 또는 승인 gate가 있는 개선 workflow로 분리한다.
- 모든 finding에 정확한 파일, 현재 line, 관련 context, 규칙 원문, confidence, 검증 방법을 요구한다.
- 실제 실행된 도구를 기록한다. LLM 판단, regex match, SAST 결과, dependency advisory, 재현된 exploit은 서로 다른 증거다.

## 응용

1. **규칙 하나만 실행**: pre-commit 검토에서 `secret-scan`, `crypto-weakness` 등 필요한 규칙 Skill만 호출한다.
2. **stack별 제어 항목 추가**: 같은 보고서 contract를 유지하면서 검토된 FastAPI, Spring Security, Express, container 규칙을 추가한다.
3. **scanner로 검증**: Mode 실행 뒤 SAST와 dependency scan을 수행하고 각 finding을 확인, 기각, 수동 검토 필요로 표시한다.
4. **승인 후 개선**: 검토를 통과한 보고서 항목만 받아 계획을 제안하고, 승인 전에는 소스 파일을 수정하지 않는 별도 Mode를 만든다.
