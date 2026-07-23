# Contributing to IBM Bob Labs

이 문서는 `README.md`의 "Add a lab"·"Content unit spec" 섹션과 `AGENTS.md`에 있는 규칙의 상세판입니다.
(섹션 제목은 다른 문서·이슈에서 링크로 참조하기 때문에 영어로 둡니다. 본문은 한국어입니다.)

## Start here (day 1)

뭔가 만들기 전에, 첫 한 시간은 전체 흐름이 처음부터 끝까지 도는지 직접 확인하는 데 쓰세요.
이후 만드는 모든 랩은 이 흐름의 반복입니다.

1. **Bob을 실행하고 로그인하세요.**
   접근 권한이 안 잡혀 있으면 바로 요청하세요 - 모든 걸 막는 유일한 단계이고 우회할 수 없습니다.
   이걸 미룬 채 초안부터 쓰지 마세요.
2. **레퍼런스 랩을 직접 돌려보세요.**
   `_labs_en/spring-boot/explain-repo.md`를 열고, [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic)을 클론한 뒤, 그 Prompt를 Bob으로 해당 repo에 돌려보세요.
   나온 결과를 랩의 Expected Output과 비교하세요.
   이게 모든 랩에서 반복할 흐름입니다 - 직접 쓰기 전에 한 번 몸으로 익히세요.
3. **[Reference assets](#reference-assets)의 세 가지 레퍼런스 형태를 훑어보세요.**
   어떤 걸 복사할지 감을 잡기 위해서입니다.
4. **1주차에 첫 랩을 출하하세요.**
   가장 흥미로운 게 아니라 담당 중 *가장 작은* 랩을 고르세요.
   1주차의 목적은 이중언어 한 쌍을 CI와 리뷰까지 끝까지 통과시켜, 막히는 지점을 4주차가 아니라 지금 드러내는 것입니다.

## Ground rules

- 모든 변경은 PR로 들어갑니다.
  `main`에는 직접 커밋하지 않습니다 - branch protection으로 강제됩니다(승인 1개 + 상태 체크 통과 필요).
- `main`에서 브랜치를 땁니다:
  - Lab/Recipe 에셋은 `content/<stack>-<slug>`, 예: `content/spring-boot-explain-repo`.
    이건 이름표일 뿐이라 실제 파일 경로와 일치할 필요는 없습니다.
  - 그 외 전부는 `feat/`, `fix/`, `docs/`.
- 커밋 메시지는 영어, 명령형으로 씁니다.
- 커밋 전에 `git status`로 `NOTE.md`와 `docs/`가 스테이징되지 않았는지 확인하세요 - 로컬 전용 작업 문서입니다.

## Adding a Lab / Recipe asset

에셋 하나는 **한 커밋/PR에 함께 추가하는 파일 두 개**입니다:

- `_labs_en/<stack>/<slug>.md` - 영어
- `_labs_ko/<stack>/<slug>.md` - 한국어

둘 다 필수입니다.
한국 고객도 Bob 프롬프트를 한국어로 붙여넣기 때문에, 한국어 파일의 Prompt는 영어 파일의 번역이 아니라 실제 한국어 사용 참가자가 칠 법한 문장으로 씁니다.

각 파일에 필요한 것:

1. **Front matter**: `title`, `lang` (`en`/`ko`), `category`, `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`.
   값은 파일 자신의 언어로 쓰고, YAML 키는 두 파일 모두 영어로 둡니다.
   `category`만 예외입니다 - 그 *값*도 두 파일 모두 영어로 둡니다.
   홈페이지 필터와 CI가 검증하는 기계 키라서입니다.
   `_data/categories.yml`의 값 중 하나를 고르면, 한국어 라벨은 표시용으로 조회됩니다.
2. **전체 콘텐츠 스펙, 제목은 파일 자신의 언어로**:
   - 영어: `Problem → Prompt → Expected Output → Tips → Variations`
   - 한국어: `문제 → 프롬프트 → 기대 결과 → 팁 → 응용`
   빈 섹션이 있으면 안 됩니다.
3. **그 언어로 실제 Bob에 돌린 Prompt.**
   Expected Output은 관찰한 것을 적습니다 - Bob이 이럴 것이라 가정한 게 아니라, 다른 언어 파일 출력의 번역도 아니라.

`validate-content` CI 체크가 이 모든 걸 자동으로 강제합니다 - 두 언어 파일이 다 있는지까지 포함해서, 한 언어만 추가한 PR은 실패합니다.

### Copy this skeleton

가장 빠른 시작: 아래 두 블록을 각 파일에 붙여넣고 빈칸을 채우세요.
모든 필드와 섹션이 필수입니다 - 비워두면 CI가 PR을 거부합니다.
채우면서 `<!-- 주석 -->`은 지우세요.
한국어 파일의 차이에 주의하세요: 제목·`difficulty`·`duration`·`work_replaced`·`expected_saving`는 한국어로 쓰지만, `lang`·`category`·`stack` 값은 보이는 그대로 둡니다.

**`_labs_en/<stack>/<slug>.md`** (English)

````markdown
---
title:            # page heading, in English
lang: en
category:         # exact value from _data/categories.yml, e.g. Code Review
difficulty:       # Guided or Challenge
duration:         # e.g. 5 min
stack:            # e.g. Java, Spring Boot  (or  Any  when not tied to a stack)
work_replaced:    # the repetitive task, one line
expected_saving:  # e.g. 30 min → 5 min
---

## Problem

<!-- Who is stuck on what, and why it keeps happening. 2-3 sentences. -->

## Prompt

<!-- The exact prompt, in a fenced code block so the site gives it a copy button. -->

```
<paste the prompt>
```

## Expected Output

<!-- What Bob actually produced. Name the practice repo you ran it against. Not a guess. -->

## Tips

<!-- What to check, the common failure, how to reprompt. -->

## Variations

<!-- 1-3 ways to adapt it, e.g. per stack. -->
````

**`_labs_ko/<stack>/<slug>.md`** (Korean - 영어 파일과 같은 slug·같은 폴더)

````markdown
---
title:            # 페이지 제목, 한국어로
lang: ko
category:         # _data/categories.yml 값 그대로, 영어로 (예: Code Review)
difficulty:       # 가이드 또는 챌린지
duration:         # 예: 5분
stack:            # 예: Java, Spring Boot  (스택과 무관하면  Any)
work_replaced:    # 대체하는 반복 업무, 한 줄
expected_saving:  # 예: 30분 → 5분
---

## 문제

<!-- 누가 무엇에 막혀 있고 왜 반복되는지. 2~3문장. -->

## 프롬프트

<!-- 실제 프롬프트를 코드블록 안에 (그래야 복사 버튼이 붙음). 한국어 참가자가 실제로 칠 법한 문장으로 - 영어 번역이 아니라. -->

```
<프롬프트 붙여넣기>
```

## 기대 결과

<!-- Bob을 한국어로 직접 돌려서 나온 결과. 어떤 연습 repo에 대고 돌렸는지 명시. 영어 결과의 번역이 아님. -->

## 팁

<!-- 확인할 것, 흔한 실패, 다시 프롬프트하는 법. -->

## 응용

<!-- 1~3가지 변형 (예: 스택별). -->
````

Challenge 랩도 섹션은 동일합니다 - `## Prompt`만 형태가 다릅니다(목표를 먼저 쓰고, 도움말은 `<details>` 뒤에 숨김).
아래 [Challenge labs](#challenge-labs) 참고.

### Reference assets

복사해 쓸 완성된 세 쌍, 서로 다른 세 가지 형태:

| Reference | 형태 |
|---|---|
| `_labs_en/spring-boot/explain-repo.md` | 스택 종속 (`stack: Java, Spring Boot`), 단일 프롬프트 |
| `_labs_en/bob-features/generate-architecture-diagram.md` | 스택 무관 (`stack: Any`), 단일 프롬프트, Bob 기능 데모 |
| `_labs_en/bob-features/plan-then-build.md` | 여러 단계 - `## Prompt` 안에 `### Step N` 하위 제목 4개, 각 단계마다 체크포인트 |

각 파일은 같은 경로에 `_labs_ko/` 짝이 있습니다.
`README.md`의 "Content unit spec"에 전체 필드/섹션 레퍼런스가 있습니다.

### Practice repositories

모든 Prompt를 그때그때 열려 있는 아무 repo가 아니라 같은 공유 세트에 대고 돌리세요.
입력이 일관돼야 리뷰어가 Expected Output을 재현할 수 있고, 라이브러리 전체 랩이 서로 비교 가능해집니다.
랩의 stack에 맞는 repo를 고르고, `stack: Any` 랩은 예시에 맞는 걸 쓰되 어떤 걸 썼는지 Expected Output에 적으세요(레퍼런스 `explain-repo`가 바로 그렇게 합니다).

| Stack | Repo |
|---|---|
| Java / Spring Boot | [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic) |
| Java / 레거시 모놀리스 | [mybatis/jpetstore-6](https://github.com/mybatis/jpetstore-6) - Spring + MyBatis, 의도적으로 구형 |
| Python / FastAPI | [grillazz/fastapi-sqlalchemy-asyncpg](https://github.com/grillazz/fastapi-sqlalchemy-asyncpg) |
| Frontend / React | [alan2207/bulletproof-react](https://github.com/alan2207/bulletproof-react) |
| Data & Documents | OpenAPI 스펙: [Swagger Petstore](https://github.com/OAI/OpenAPI-Specification/blob/main/examples/v3.0/petstore.yaml). Excel: 첫 Data & Documents 랩과 함께 추가하는 `fixtures/` 아래 샘플 워크북 |

이 목록에 없는 repo가 정말 필요하면, 같은 PR에서 이 표에 한 줄 이유와 함께 추가하세요 - 조용히 다른 걸 가리키지 마세요, 다음 사람이 재현할 수 없습니다.

이건 랩을 **만들 때** 쓰는 repo입니다.
행사 당일 참가자가 무엇에 대고 돌릴지는 별개 문제이고(IP 때문에 자기 코드를 못 가져올 수 있음) `README.md`에 아직 미결로 남아 있습니다.

### Challenge labs

`difficulty: Challenge`는 값이지 별도 콘텐츠 타입이 아닙니다.
섹션 다섯 개는 동일하고, 차이는 `## Prompt`가 목표를 제시한 뒤 도움말을 `<details>` 뒤에 숨기는 것뿐입니다 - 스켈레톤 먼저, 동작하는 프롬프트 나중.
난이도를 올리려고 `## Prompt`를 비워두지 마세요; CI가 거부하고, 아무에게도 도움이 안 됩니다.
형태는 AGENTS.md를 참고하세요.

### Prefer `stack: Any`

대부분의 반복 작업은 스택에 종속되지 않습니다 - "explain this repo"는 Java든 Python이든 같은 랩입니다.
`stack: Any`로 한 번 쓰고 스택별 차이는 **Variations** 섹션에 넣으세요, 거의 같은 걸 다섯 번 쓰는 대신.
스택이 프롬프트를 실제로 바꾸는 경우에만 스택 전용 랩으로 남기세요(Spring Boot 2→3 마이그레이션은 그렇습니다).

## Previewing your work

**랩이라면 거의 확실히 미리보기가 필요 없습니다.**
`.md` 파일을 브라우저로 여는 건 안 됩니다 - Markdown이고, Jekyll이 빌드 시점에 HTML로 바꿉니다.
`_site/`의 빌드된 파일을 여는 것도 안 됩니다: 사이트가 `/bob-labs/` 아래로 서빙되기 때문에, 모든 스타일시트·폰트·링크가 절대경로라 `file://`에서는 디스크 루트로 풀려 404가 납니다.

그럴 필요가 없는 이유는, 랩 PR에서 확인이 필요한 것들이 이미 다 커버되기 때문입니다:

| 확인할 것 | 누가 확인하나 |
|---|---|
| front matter 필드 전부 존재, `category` 유효 | `validate-content` CI |
| 다섯 섹션 전부 존재하고 비어 있지 않음 | `validate-content` CI |
| 두 언어 파일 모두 존재 | `validate-content` CI |
| 문장이 잘 읽히는가 | PR에서 GitHub가 Markdown을 렌더 |
| 페이지가 제대로 보이는가 | 이미 공개된 랩들과 똑같이 렌더됨 |
| **프롬프트가 Bob에서 실제로 동작하는가** | **당신이 직접 돌려서. 이것만은 다른 무엇도 못 합니다.** |

마지막 줄에 시간을 쓰세요.
그것만이 진짜로 위험이 남는 유일한 줄이고, 어떤 도구도 잡아주지 못하는 유일한 것입니다.

그렇긴 해도, 자기 작업을 눈으로 보는 건 가치가 있고, 빌드하기 전엔 정말 안 보이는 것들도 있습니다: 카드 그리드에서 제목이 어떻게 줄바꿈되는지, 자동 축약된 미리보기 텍스트가 어디서 잘리는지, 긴 메타데이터 값이 배지 줄을 깨뜨리는지, 그리고 라이트/다크 두 테마.
`_layouts/`·`_includes/`·`assets/` 아래의 레이아웃·CSS를 건드린다면 미리보기는 선택이 아닙니다.

### Running the site

```sh
script/preview
```

그게 전부입니다.
스크립트가 Ruby 버전을 확인하고, 첫 실행 시 의존성을 설치하고, URL을 출력합니다.
빠진 게 있으면 처음 들어보는 gem에서 실패하는 대신, 무엇을 하면 되는지 정확히 알려줍니다.

<http://127.0.0.1:4000/bob-labs/>를 여세요, `/en/`으로 리다이렉트됩니다.
**`/bob-labs/`에 주의하세요** - 빼면 404가 납니다, 도메인 루트가 아니라 프로젝트 경로용으로 빌드되기 때문입니다.
두 언어(`/bob-labs/en/`·`/bob-labs/ko/`)와 두 테마(헤더의 토글)를 다 확인하세요.

Jekyll은 **Ruby 3.0 이상이 필요한데 macOS는 2.6을 기본 탑재**하므로, Mac에서는 먼저 `brew install ruby`가 필요할 가능성이 높습니다 - 필요하면 스크립트가 알려줍니다.

이 저장소에서 코딩 에이전트와 함께 작업한다면, `.claude/skills/preview/`가 사이트 실행 방법과 볼 것을 알려줍니다 - "이거 렌더되는지 확인해줘"가 Ruby 버전 함정을 매번 다시 발견하는 일로 번지지 않게요.

### Or skip installing anything

이 저장소에는 dev container가 있어서, 로컬 Ruby 없이도 사이트를 돌릴 수 있습니다:

- **브라우저에서**: GitHub에서 `Code` → `Codespaces` → 생성.
  Codespaces는 먼저 organization에 활성화돼 있어야 하므로, 누군가에게 갑자기 비용이 청구될 일은 없습니다.
- **로컬에서**: Docker가 있는 VS Code에서 `Reopen in Container`.

그다음은 위처럼 `script/preview`. `.devcontainer/README.md`를 참고하세요.

## Other changes (docs, site, tooling)

같은 PR 흐름이고, 브랜치 접두사는 바뀐 내용에 따라 `feat/`·`fix/`·`docs/`.
`AGENTS.md`의 "Current state" 섹션이 사이트 스택(Jekyll, GitHub Actions, GitHub Pages)을 설명합니다 - 먼저 묻지 않고 다른 프레임워크나 빌드 도구를 도입하지 마세요.

## What makes a lab worth keeping

CI는 랩이 *형식이 맞는지*를 확인합니다.
*간직할 가치가 있는지*는 확인하지 못합니다 - 그건 리뷰의 몫이고, 랩을 쓸 때든 남의 걸 리뷰할 때든 같은 기준입니다.
아래 모든 줄이 예스일 때만 머지됩니다.

- [ ] **Bob을 쓰는 진짜 유용한 방법을 보여준다.** 랩이 개발자가 원하는 실제 결과를 만들어낸다, 기능을 자랑하려고 꾸민 상황이 아니라. `work_replaced`는 그 랩이 도와주는 작업의 종류를 가리킨다. 무엇에 좋은지 한 줄로 말할 수 없다면, 그건 랩이 아니다.
- [ ] **복붙으로 5분 안에 첫 결과.** 페이지에 들어와 Prompt를 복사하면, 포기할 만한 시점 전에 쓸 만한 결과가 나온다. 설명을 먼저 읽게 하지 않는다.
- [ ] **모르는 사람도 재사용할 수 있다.** 내 장비·내 비공개 repo·나만 아는 맥락에 의존하지 않는다. [practice repo](#practice-repositories)에 대고 돌리는 게 바로 이걸 보장한다 - 하나를 쓰세요.
- [ ] **Before/After가 자명하다.** `expected_saving`이 구체적이고 믿을 만하며(`30 min → 5 min`), Expected Output이 "after"가 실제로 어떤지 보여준다.
- [ ] **스택이 프롬프트를 바꾸지 않는 한 `stack: Any`.** "explain this repo"는 하나의 랩이지 다섯 개가 아니다. 스택이 *핵심*일 때만 스택 전용 랩으로(예: Spring Boot 2→3 마이그레이션).
- [ ] **Prompt를 실제로, 그 언어로 Bob에 돌렸다.** 이것만은 어떤 도구도 못 잡고, 라이브러리 전체의 신뢰가 여기 걸려 있다. Expected Output은 본 것을 적는다, Bob이 이럴 것이라 가정한 게 아니라.
- [ ] **P0 먼저, 그다음 P1.** P0 랩이 남아 있는 동안 P1/P2 작업은 기다린다.

재사용성이 에셋 개수를 이깁니다.
아무도 다시 안 여는 랩은 랩이 없느니만 못합니다 - 다음 사람이 헤치고 지나가야 할 잡음입니다.
거절된 초안이, 사람을 오도하는 채로 남은 랩보다 쌉니다.

## Questions

- 프로젝트 개요와 콘텐츠 스펙: `README.md`
- 이 저장소에서 일하는 코딩 에이전트(Bob, Claude 등)용 규칙: `AGENTS.md`
