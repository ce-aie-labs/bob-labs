---
title: 재사용 가능한 Custom Mode 만들기
lang: ko
category: Bob Features
difficulty: 가이드
duration: 10분
stack: Any
work_replaced: 워크플로 페르소나 수동 구성
expected_saving: 30분 → 5분
---

## 문제

같은 역할을 Bob에게 계속 다시 설명하고 있다 - "리뷰어처럼 굴어, 읽고 코멘트만 해, 내 파일은 건드리지 마". Custom Mode는 그 페르소나를 한 번에 캡처한다: 역할, 지시문, 그리고 Bob이 넘어설 수 없는 잠긴 도구 세트. 커밋해두면 팀 전체가 같은 전문화된 Bob을 모드 선택기에서 쓸 수 있다.

## 프롬프트

**Agent 모드**에서 실행:

```
코드 리뷰용 커스텀 모드를 만들어서 프로젝트 루트의 .bobmodes 파일에
써줘. 팀이 공유할 수 있게.

- Slug: reviewer
- 역할: 변경사항을 읽고 파일과 줄 단위로 이슈를 보고하는 시니어 리뷰어.
  blocking / worth fixing / optional 등급을 매기고, 코드는 절대 수정 안 함
- 사용 시점: 머지 전 diff나 PR을 리뷰할 때
- 도구: Read만 허용. Edit, Execute, MCP 없음 - 파일을 바꾸거나 명령을
  실행할 수 없어야 함.

이 도구 제한이 실제로 어떻게 강제되는지 설명해줘.
```

## 기대 결과

- [ ] 프로젝트 루트에 `.bobmodes` 파일이 생성되고 모드의 slug, name, description, 역할 정의, 사용 시점, 커스텀 지시문, 도구를 정의함 - 산문 설명만이 아니라 전체 필드 세트
- [ ] Tools 필드가 **Read만** 허용 - Edit, Execute, MCP는 없음 - 그리고 Bob이 도구 목록은 결정적으로 강제되므로 이 모드에서는 수정 지시가 그냥 무시된다고 설명함
- [ ] reviewer 모드로 전환해 뭔가 고쳐달라고 하면 보고로 넘겨짐. 모드가 파일을 쓸 수 없기 때문 - 안전성은 프롬프트 문구가 아니라 도구 세트에서 나옴
- [ ] `.bobmodes` 파일은 커밋 가능하므로 레포를 클론하는 모든 팀원이 같은 reviewer 페르소나를 얻음 - 각자 손으로 다시 만드는 세팅이 아니라

IBM Bob Level 3 Tailor 모듈 [Custom Modes](https://ibm.github.io/bob-l3/tailor/3-3/) 기준입니다. Product Manager 모드(Read / Edit / MCP 허용, Execute 제외)를 만들고, Bob이 할 수 있는 것을 제약하는 건 지시문 산문이 아니라 결정적인 Tools 필드임을 강조합니다.

<!-- Bob-verify: 아직 Bob에 안 돌려봤음. IBM Bob L3 실습 워크스루(ibm.github.io/bob-l3/tailor/3-3/) 기반으로 작성. 워크스루는 프롬프트가 아니라 설정 UI로 모드를 만듦. 참가자에게 쓰기 전에 실제 한국어 프롬프트로 Bob에서 검증 필요 - 특히 이 프롬프트로 Bob이 유효한 .bobmodes를 쓰는지, 모드 전환 후 Read 전용 도구 세트가 실제로 수정 요청을 막는지 확인할 것. -->

## 팁

- Tools 필드가 진짜 안전 장치이고, 유일하게 결정적인 필드다. 목록에 없는 도구를 요청하는 커스텀 지시는 조용히 무시된다 - 그러니 "수정하지 마"에 기대지 말고 도구를 잠글 것.
- 설정 UI로도 모드를 만들 수 있지만(모드 선택기 → 톱니 → `+`), Bob에게 `.bobmodes`를 생성시키면 커밋 가능한 산출물과 손으로 다듬을 출발점이 생긴다.
- 스코프가 중요하다: 프로젝트 스코프 모드는 `.bobmodes`에 있고 레포로 공유되며, 글로벌 모드는 모든 프로젝트에서 쓸 수 있지만 코드와 함께 커밋되지는 않는다.

## 응용

1. **기획 / PM 모드**: "...두루뭉술한 아이디어를 MVP 카드, now/next/later 로드맵, 유저 스토리로 바꾸는 `product-manager` 모드 - Read, Edit, MCP 허용, Execute 제외."
2. **문서 작성 모드**: "...코드를 읽고 Markdown을 쓰지만 명령은 절대 실행 못 하는 `docs` 모드 - Read와 Edit만."
3. **기존 모드 조이기**: "내 `.bobmodes`를 검토해서 reviewer 모드에 있으면 안 되는 도구를 제거해줘. 각 제거 이유도 설명하고."
