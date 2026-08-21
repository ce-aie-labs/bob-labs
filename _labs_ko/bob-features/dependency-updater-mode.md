---
title: Local Dependency Updater Mode로 여러 프로젝트 최신화하기
lang: ko
category: Bob Features
difficulty: 가이드
duration: 20분
stack: Any
work_replaced: 프로젝트마다 의존성 현황 파악, 조사, 승인, 검증 workflow를 수작업으로 구성하는 작업
expected_saving: 1일 → 20분
---

## 문제

프로젝트 library를 안전하게 최신화하려면 모든 버전을 `latest`로 바꾸는 것만으로는 부족하다. 프로젝트마다 직접·전이 의존성의 실제 resolved version, 재현 가능한 baseline, 현재 공식 호환성 근거, 사람이 승인한 변경 범위, 동일한 검사로 수행한 사후 검증이 필요하다.

이번 예제에서는 먼저 의존성 최신화 전용 workspace의 local `.bob/` 환경에 Dependency Updater Mode를 만든다. 그다음 대상 프로젝트들을 이 workspace 아래에 clone하고, 같은 Mode로 프로젝트를 하나씩 최신화한다. 사용자의 global `~/.bob/` 설정은 변경하지 않는다.

## 프롬프트

승인된 실행에서는 manifest, lockfile, source code가 변경될 수 있으므로 일회용 clone이나 깨끗한 branch를 사용한다.

### 1단계 - Local Mode 먼저 만들기

**Agent 모드**에서 Bob에게 이 workspace 안에 Mode와 Skill을 만들도록 요청한다:

```
현재 workspace에서만 사용하는 Dependency Updater라는 이름의 project-scoped Custom Mode를 만들어줘. 이 Mode가 준비된 다음 대상 프로젝트들을 하위 디렉터리에 clone해서 사용할 거야.

Mode와 Mode가 사용하는 모든 Skill은 이 workspace의 .bob 디렉터리 아래에만 설치해줘. .bob/custom_modes.yaml과 .bob/skills/ 아래의 project Skill을 만들어야 해. ~/.bob/ 아래의 Mode나 Skill은 만들거나 수정하지 마. 이 Mode는 현재 workspace를 열었을 때만 사용할 수 있어야 하고, 실행할 때마다 대상 프로젝트 디렉터리를 입력받아야 해.

이 Mode의 목적은 선택한 대상 프로젝트에서 사용 중인 직접·전이 의존성을 파악하고, 업데이트가 필요한 library를 조사하고, 공식 문서와 release note를 근거로 호환성 영향을 분석한 뒤, 사람이 승인한 범위만 최신화하는 것이야.

다음의 규칙이 기반이 되어야 해.

1. 현재 버전과 목표 버전을 선택한 대상 프로젝트의 manifest와 lockfile에서 직접 확인한다.
2. "latest"라는 이유만으로 버전을 선택하지 않는다. 목표 버전, 공개일, 지원 상태, 현재 runtime과의 호환성을 근거로 선택한다.
3. 공개 library는 공식 문서, 공식 release note, migration guide, package registry의 정보를 우선 사용한다.
4. blog, 검색 요약, LLM의 기존 지식만으로 breaking change나 호환성을 확정하지 않는다.
5. 사내 library는 대상 프로젝트 안의 문서, 사내 package metadata, changelog 등 Bob이 실제로 접근할 수 있는 자료만 사용한다. 사내 코드나 package 정보를 외부 검색에 포함하지 않는다.
6. 업데이트 전에 대상 프로젝트의 기존 build와 test를 실행해서 baseline을 기록한다.
7. deprecated API, 제거된 API, configuration 변경, runtime 요구 버전, peer dependency, transitive dependency, lockfile 변화를 조사한다.
8. source code, manifest, lockfile은 사람이 변경 계획을 승인하기 전에는 수정하지 않는다.
9. 각 대상 프로젝트의 의존성 현황, baseline, 조사 결과, 변경 계획, command output, report는 해당 대상 프로젝트 디렉터리 안에 저장한다. 서로 다른 대상의 근거를 섞지 않는다.

파이프라인은 아래의 방향으로 진행해줘.
의존성 현황 파악 -> 현재 상태 테스트 -> 공식 문서와 release note 조사 -> breaking change 및 deprecated API 분석 -> 변경 계획 작성 -> 사람의 승인 -> 의존성과 코드 수정 -> 테스트·빌드·검증 -> 결과 보고

위 규칙과 파이프라인에 필요한 각각의 workspace Skill을 만들고, local Mode가 실행할 때 전달받은 대상 디렉터리에 대해 해당 Skill을 순서대로 orchestration하도록 구성해줘. 이미 존재하는 관련 없는 workspace Mode나 Skill은 덮어쓰지 마.
```

**체크포인트:** Bob은 **Dependency Updater** (`dep-updater`)와 여덟 개의 Skill을 다음 경로에 만들어야 한다:

```text
.bob/custom_modes.yaml
.bob/skills/dep-scan/SKILL.md
.bob/skills/dep-baseline/SKILL.md
.bob/skills/dep-research/SKILL.md
.bob/skills/dep-breaking-change/SKILL.md
.bob/skills/dep-plan/SKILL.md
.bob/skills/dep-apply/SKILL.md
.bob/skills/dep-verify/SKILL.md
.bob/skills/dep-report/SKILL.md
```

`~/.bob/` 아래에 어떤 file도 생성하거나 변경하지 않았는지 확인한다. Skill은 위 순서로 실행되고, 대상 프로젝트 디렉터리를 입력받고, 사내 package 이름과 metadata를 공개 조회에서 제외하고, 검증 단계에서 baseline과 동일한 command를 다시 실행한다.

생성된 tool group도 확인한다. 관찰한 prototype에는 제한 없는 `edit`와 `execute`가 허용돼 있었으므로 승인 전 정지는 tool의 file restriction으로 강제되는 경계가 아니라 **instruction 기반 경계**였다. Bob이 실제로 멈추는지 확인하고 모든 edit와 command 요청을 검토한다.

다음 단계 전에 Mode picker에서 이 workspace의 local **Dependency Updater**를 선택한다.

### 2단계 - 첫 번째 프로젝트 clone 후 최신화하기

Mode가 준비된 다음 관찰된 예제를 현재 workspace 아래에 clone한다:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

현재 workspace를 그대로 연 상태에서 다음 프롬프트를 실행한다:

```
./galaxium-travels 프로젝트의 의존성을 최신화해줘.
```

Bob은 해당 대상 디렉터리에 대해 `dep-scan`부터 `dep-plan`까지 실행한 뒤 멈춰야 한다. 제안된 현재·목표 버전, 1차 출처 URL, 호환성 판단, manifest 또는 source 변경, lockfile command, 취약점 조치 command를 각각 검토한다.

처음에는 작은 package subset만 승인하는 편이 좋다. `dep-apply` 이후 Bob이 baseline과 정확히 같은 command로 `dep-verify`를 실행하고, `dep-report`를 생성하게 한다.

**체크포인트:** manifest와 source diff, 전체 lockfile diff, 변경 전후 build·test·lint 결과, scanner 또는 advisory 근거, 단계별 측정 시간, 최종 report를 직접 확인한다. 완료 메시지, registry 응답, elapsed-time 숫자만으로 호환성 또는 보안이 입증되지는 않는다.

### 3단계 - 다른 프로젝트 clone 후 최신화하기

첫 번째 실행을 마친 다음 다른 프로젝트를 같은 workspace의 별도 하위 디렉터리에 clone한다. Mode를 다시 만들거나 복사하지 않는다. **Dependency Updater**를 선택한 상태에서 두 번째 대상을 프롬프트에 명시한다:

```text
./another-project 프로젝트의 의존성을 최신화해줘.
```

같은 workspace-local Mode가 대상 디렉터리를 하나씩 처리하되, 각 대상의 의존성 현황, baseline, 근거, 승인, diff, 시간 측정, report는 분리해야 한다. 프로젝트에 완전히 다른 의존성 규칙이 필요하다면 공통 workflow를 실행 도중 바꾸지 말고 별도 modernization workspace와 `.bob/` 정의를 만든다.

## 기대 결과

대상 프로젝트를 clone하기 전에 modernization workspace의 `.bob/` 아래에 local Mode 한 개와 local Skill 여덟 개가 생성돼야 한다. Clone한 각 대상은 의존성 현황, baseline, 승인 기록, 검증 비교, report를 자신의 프로젝트 디렉터리 안에 보관해야 한다. 같은 Mode가 여러 sibling 프로젝트를 하나씩 처리하고 global `~/.bob/` 설정은 변경하지 않는다.

관찰한 한국어 실행은 project-scoped pipeline을 만든 뒤 `galaxium-travels`에 사용했으며 다음 baseline을 기록했다:

- Build: 통과
- Test: 29개 통과, 0개 실패
- Lint: 기존부터 실패, error 9개와 warning 1개
- npm audit: HIGH 3건
- 의존성 범위: npm 직접 의존성 24개, Python 직접 의존성 10개, resolved npm 전이 의존성 326개

최신화 승인 응답 후 report에는 frontend version constraint 18개 변경, 기존에 version이 지정되지 않았던 Python requirement 10개의 exact pin 전환, `npm audit fix` 실행, npm lockfile 변경이 기록됐다. 승인된 batch에서 source file은 변경되지 않았다. TypeScript, Tailwind CSS 4, Vite 8, `lucide-react`는 적용하지 않고 blocked 또는 deferred로 분류했다.

변경 후 build가 통과했고 test 29개가 계속 통과했으며 npm audit 결과는 0건으로 기록됐다.

## 팁

- 대상 프로젝트를 clone하거나 Mode를 선택하기 전에 `.bob/custom_modes.yaml`과 모든 `SKILL.md`를 검토한다. 생성된 orchestration도 script와 같은 수준으로 검토해야 한다.
- Mode는 modernization workspace에 두고, 각 대상의 package data와 report는 해당 대상 디렉터리에 둔다. 한 프로젝트의 version이나 근거를 다른 프로젝트에 복사하지 않는다.
- 팀이 이 modernization workflow를 공유하고 검토하려는 경우에만 workspace의 `.bob/`을 version control에 포함한다. 그렇지 않으면 local로 유지한다.
- 한 ecosystem이나 작은 package subset부터 승인한다. Batch가 작을수록 lockfile 변경과 regression의 원인을 추적하기 쉽다.
- Baseline command와 실행 환경을 그대로 유지한다. 전후에 test flag나 runtime을 바꾸면 비교가 무효가 된다.
- 사내 package 이름, version, source 일부, private registry metadata를 공개 registry나 검색 서비스로 보내지 않는다. 조직이 승인한 내부 출처만 사용한다.

## 응용

1. **Workspace별로 다른 Mode 구성**: Frontend modernization workspace에는 더 엄격한 lint와 browser test 단계를 넣고, backend workspace에는 database migration 검사를 추가한다.
2. **Patch 및 보안 수정만 적용**: 각 package를 현재 major version 안에 유지하고 검토한 advisory와 연결된 update만 승인한다.
3. **사내 registry workflow**: 사내 의존성의 공개 조사 command를 조직 승인 registry metadata와 changelog 조회로 바꾸고 정보 유출 금지 경계는 유지한다.
