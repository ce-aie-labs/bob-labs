---
title: 아키텍처 다이어그램 생성하기
lang: ko
difficulty: 가이드
duration: 10분
stack: Any
work_replaced: 아키텍처 다이어그램 작성
expected_saving: 1시간 → 10분
---

## 문제

코드베이스의 아키텍처를 문서화해야 하는 상황 - 온보딩, 설계 리뷰, 또는 큰 리팩토링 전에 - 인데, UML 다이어그램을 손으로 그리는 건 번거롭고 코드가 바뀌는 순간 바로 낡아버린다.

## 프롬프트

```
이 레포지토리의 전체 아키텍처를 분석해줘. 주요 컴포넌트나 서비스가 뭐가 있고,
서로 어떻게 연결돼 있고, 어떤 데이터나 호출이 오가는지.

이걸 시각화하는 Mermaid 다이어그램을 만들어줘 (대부분 레포에는 flowchart가
적당해), 그리고 다이어그램 코드 블록 위에 짧은 설명과 함께
docs/architecture/overview.md 파일로 저장해줘.
```

## 기대 결과

- [ ] 레포지토리에 실제로 존재하는 컴포넌트/서비스를 담은 Mermaid 다이어그램 - "서비스 A", "서비스 B" 같은 일반적인 placeholder가 아니라
- [ ] 화살표에 실제로 오가는 것(API 호출, 메시지, 공유 데이터베이스 등)이 라벨로 붙어있음 - 라벨 없는 화살표가 아니라
- [ ] 여러 언어/서비스로 이루어진 레포라면 각 컴포넌트의 역할이나 언어가 표시됨
- [ ] 다이어그램이 실제 파일(`docs/architecture/overview.md`)로 저장돼서, 위에 짧은 설명과 함께 GitHub에서 바로 렌더링되어 보임

IBM의 공식 튜토리얼([Generate architecture diagrams](https://bob.ibm.com/docs/ide/tutorials/generate-architecture-diagrams), 공식 데모 레포인 Galaxium Travels - React + Python FastAPI + Java로 이루어진 폴리글랏 예약 시스템 - 기준)을 바탕으로 작성한 것이며, 직접 검증 실행한 것은 아니다.

<!-- Bob-verify: 스프링 부트 랩과 달리 이건 Bob은 물론 Claude Code로도 아직 한 번도 돌려보지 않은 상태. Bob의 공식 튜토리얼 문서(docs/bob/ide/tutorials/generate-architecture-diagrams.md)를 바탕으로 바로 작성함. 참가자에게 쓰기 전에 실제 Bob으로 - 생성된 Mermaid가 실제로 파싱/렌더링되는지까지 - 반드시 검증 필요. -->

## 팁

- 코드베이스가 크면 다이어그램 범위를 모듈이나 서비스 하나로 좁혀서 요청("...결제 서비스만") - 노드가 너무 많으면 Bob이 Mermaid 문법을 틀릴 확률이 올라감.
- Mermaid 파서는 꽤 엄격함. 다이어그램이 렌더링 안 되면 파서 에러를 그대로 Bob에게 붙여넣고 그 줄만 고쳐달라고 요청 - 전체를 다시 만들게 하지 말고.
- 아직 안 했다면 먼저 `/init`으로 프로젝트 컨텍스트를 잡아두기 - Bob이 코드베이스 지도를 이미 갖고 있으면 다이어그램 품질이 올라감.

## 응용

1. **클래스 다이어그램**: "핵심 데이터 모델의 Mermaid `classDiagram`을 만들어줘 - 주요 엔티티, 필드, 그리고 서로의 관계."
2. **시퀀스 다이어그램**: "사용자가 `<동작>`을 했을 때 처음부터 끝까지 무슨 일이 일어나는지 추적하는 Mermaid `sequenceDiagram`을 만들어줘."
3. **유스케이스 다이어그램**: "이 시스템의 기능을 행위자(최종 사용자, 외부 시스템, 내부 서비스)별로 묶은 Mermaid flowchart를 유스케이스 다이어그램처럼 만들어줘."
