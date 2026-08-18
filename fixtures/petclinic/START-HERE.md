# 여기서 시작하세요 / Start here

낡은 Java 애플리케이션 하나가 들어 있습니다.
Spring PetClinic REST — **Java 8 / Spring Boot 2.1.5**, 2019년 코드입니다.
고장난 앱이 아닙니다. 지금도 잘 돕니다. 다만 오래됐습니다.

**준비물은 JDK 21 하나뿐입니다.** Maven은 안 깔아도 됩니다.

```bash
java -version    # 21 이 나와야 합니다
```

---

## 1. 앱을 띄웁니다

```bash
java -jar petclinic-legacy.jar
```

## 2. `demo.html` 을 브라우저로 엽니다

파일을 더블클릭하면 됩니다. 수의사와 반려동물 주인 목록이 뜨면 **앱이 살아있는 것**입니다.
현대화가 끝난 뒤에도 이 화면이 똑같이 나와야 합니다. 그게 "동작을 보존했다"는 뜻입니다.

## 3. 지금 상태를 봅니다

다른 터미널에서:

```bash
java check.java
```

바뀐 것과 완료 여부를 실제로 빌드하고 띄워 보고 알려줍니다.
처음에는 라이브러리를 받느라 몇 분 걸립니다.

---

무엇을 할지는 랩 페이지에 있습니다 → <https://ce-aie-labs.github.io/bob-labs/>

- `app/` 현대화할 소스
- `check.java` 리포트 (`--ko` / `--en` 로 언어 강제)
- `demo.html` 눈으로 보는 확인
- `baseline-vulnerabilities.json` 동결된 취약점 목록 (재스캔이 아니라 대조용)
- `build.log` `boot.log` `result.json` 은 `check.java` 가 만듭니다

---

# Start here (English)

Inside is one old Java application: Spring PetClinic REST, **Java 8 / Spring Boot 2.1.5**,
code from 2019. It is not broken — it still runs fine. It is just old.

**The only thing you need is JDK 21.** You do not need to install Maven.

```bash
java -version                     # should say 21
java -jar petclinic-legacy.jar    # 1. start the app
                                  # 2. open demo.html in a browser
java check.java                   # 3. see where things stand
```

If `demo.html` lists vets and owners, the app is alive. After the modernization it has to
show the very same thing — that is what "behavior preserved" means.

What to do next is on the lab pages → <https://ce-aie-labs.github.io/bob-labs/>
