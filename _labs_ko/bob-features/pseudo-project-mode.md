---
title: Custom Mode로 Pseudo Project 설계하기
lang: ko
category: Bob Features
difficulty: 가이드
duration: 20분
stack: Any
work_replaced: 기술 설계 패키지 수동 구성
expected_saving: 2시간 → 20분
---

## 문제

새로운 기술 아이디어를 엔지니어링 팀이 검토할 수 있는 수준까지 구체화하고 싶지만, 아직 실행 환경을 구성하거나 동작하는 코드를 만들 단계는 아니다. 프로젝트 전용 Custom Mode에 재사용 가능한 Skill을 연결하면 기술 조사, 계획, 구현 수준의 pseudo code, 아키텍처 시각화를 하나의 검토 가능한 워크플로우로 만들 수 있다.

## 프롬프트

Bob이 프로젝트 안에 `.bob/` 디렉터리와 설계 문서를 만들 수 있는 작업 공간에서 두 단계를 순서대로 실행한다. 각 체크포인트를 확인한 뒤 다음 단계로 넘어갈 것.

### 1단계 - 프로젝트 전용 환경 만들기

**Agent 모드**에서 실제 사용한 다음 프롬프트를 그대로 실행한다:

```
최근 기술에 새로운 아이디어를 접목해서 pseudo proejct를 만들고싶어 이걸 자동화 하는 mode 를 하나 만들어줘 해당 mode 가 사용할 skill 들도 구성해서 이 프로젝트의 채팅인 bob 에서 사용할 수 있도록 설치해줘 기술 Web search -> 프로젝트 planning -> Pseudo code, architecture.md, -> architecture visualization 의 단계로 구성되었으면 좋겠어 각 코드를 어떻게 구현할것이다는 pseudo code, architecture 설명 과 visualization, README 를 구성하는것이 목표야
```

**체크포인트:** Bob이 `.bob/custom_modes.yaml`에 프로젝트 전용 **Pseudo Project** Mode를 만들고, `.bob/skills/` 아래에 `web-tech-search`, `pseudo-project-plan`, `pseudo-code-gen`, `architecture-viz` 네 Skill을 만들었는지 확인한다. 사용을 승인하기 전에 파일을 직접 읽는다. Mode가 `update_todo_list`로 진행 상황을 표시하고, 단계 사이에 프로젝트 메타데이터를 넘기며, 필수 도메인과 아이디어 결정 이후에는 네 Skill을 자동으로 이어서 실행하도록 구성되어야 한다.

다음 단계 전에 Mode picker에서 **Pseudo Project**를 선택한다.

### 2단계 - GraphRAG 아이디어에 환경 사용하기

새 Mode에서 실제 사용한 다음 프롬프트를 실행한다:

```
graphrag 라는 기술을 써보고싶어 그리고 아직 적용안된 graph 방법론을 찾아서 적용해보면 좋을것같아 환경까지 구현하진 말고 방법론에 대한 구현과 코드만 구축해줘
```

**체크포인트:** 생성된 계획, 아키텍처, pseudo-code 파일, README를 직접 연다. 파일들이 일관된 설계 패키지를 이루고 있는지, Bob이 인프라를 구성하지 않았는지, pseudo code를 실행 가능한 구현이라고 표현하지 않았는지 확인한다.

## 기대 결과

실제 실행에서는 현재 프로젝트에 **Pseudo Project** (`pseudo-project`)가 설치됐다:

- [ ] `.bob/custom_modes.yaml`
- [ ] `.bob/skills/web-tech-search/SKILL.md`
- [ ] `.bob/skills/pseudo-project-plan/SKILL.md`
- [ ] `.bob/skills/pseudo-code-gen/SKILL.md`
- [ ] `.bob/skills/architecture-viz/SKILL.md`
- [ ] 기술 조사 → 프로젝트 계획 → pseudo code 및 architecture → 시각화 및 README의 4단계 파이프라인과 `update_todo_list` 진행 표시

GraphRAG 프롬프트를 실행한 결과 **TemporalHeteroGraphRAG**라는 설계와 다음 산출물이 생성됐다:

```text
docs/project-plan.md
docs/architecture.md
docs/pseudo/01-domain-models.pseudo.md
docs/pseudo/02-temporal-kg-builder.pseudo.md
docs/pseudo/03-hgt-encoder.pseudo.md
docs/pseudo/04-temporal-retriever.pseudo.md
docs/pseudo/05-community-summarizer.pseudo.md
docs/pseudo/06-causal-path-extractor.pseudo.md
docs/pseudo/07-llm-answer-generator.pseudo.md
docs/pseudo/08-pipeline-orchestrator.pseudo.md
README.md
[HTML summary artifact]
```

설계 패키지는 temporal knowledge graph, heterogeneous graph transformer 인코딩, temporal-aware retrieval, Leiden community summarization, causal path 추출, 근거 기반 답변 생성, 전체 pipeline orchestration을 제안했다. `docs/architecture.md`와 `README.md`에는 Mermaid 시스템, 데이터 흐름, sequence diagram도 포함됐다.

이 결과는 **설계 및 pseudo-code 산출물**이다. 실제 실행에서는 동작 환경을 만들거나 pipeline을 실행하거나 benchmark를 수행하지 않았다. 특정 방법론이 아직 사용되지 않았다거나 기존 GraphRAG보다 성능이 높다는 문장은 검증 결과가 아니라 검토할 가설이다. 생성된 조사 Skill의 helper는 2024–2025년 curated fallback 데이터를 사용했으므로, novelty 비교를 유지하려면 최신 1차 출처를 추가하고 성능 주장은 실제 구현과 benchmark 결과가 나온 뒤에만 사용한다.

## 팁

- Mode와 Skill을 참가자 프로젝트의 `.bob/`에 유지할 것. global `~/.bob/` 설치와 달리 이 구성은 프로젝트 범위에만 적용되고 프로젝트와 함께 리뷰할 수 있다.
- 생성된 조사는 증거가 아니라 조사 출발점으로 다룬다. 새롭거나 기존 구현에 없다고 판단하기 전에 인용된 1차 출처와 게시 날짜를 직접 확인한다.
- 아키텍처 주장과 측정값을 구분한다. "더 빠르다", "더 정확하다", "안전하다" 같은 표현에는 실행 가능한 코드, 테스트 조건, 관찰 결과가 필요하다.
- Bob의 완료 메시지만 보지 말고 전체 산출물 목록을 확인한다. 모듈명, 인터페이스, diagram, 계획의 내용이 서로 일치해야 한다.
- 생성된 `docs/`는 참가자의 작업 프로젝트에 두고 사용한다. 다른 실행에서 나온 설계 패키지를 이 lab 저장소로 복사하지 않는다.

## 응용

1. **다른 기술 탐색**: GraphRAG 대신 event streaming, observability, local model 아이디어를 넣고 같은 4단계 Mode로 설계한다.
2. **아키텍처에서 멈추기**: 검토된 계획, 모듈 contract, pseudo code만 필요하면 `architecture-viz` 자동 인계를 끈다.
3. **마지막 산출물 변경**: `architecture-viz`를 승인된 설계에서 ADR, 구현 backlog, 또는 리뷰 ticket을 만드는 Skill로 교체한다.
