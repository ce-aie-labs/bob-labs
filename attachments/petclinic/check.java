import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.util.zip.*;

/**
 * Bob Lab - what the modernization actually changed, and whether it is finished.
 *
 *   java check.java          (--ko / --en to force a language)
 *
 * No install, no dependencies. Needs only the JDK you already have.
 * Every line it prints comes from reading the built artifact, running the tests,
 * or starting the app - never from what a tool said it did.
 */
public class Check {

    static final int TARGET_MAJOR = 65;          // Java 21 class file version
    static final int BASE_TESTS = 169;
    static final int BASE_TEST_ANNOTATIONS = 102;
    static final int BASE_ASSERTIONS = 225;
    static final int PORT = 18080;
    static final String[] HEALTH_PATHS = {"/petclinic/api/vets", "/api/vets", "/petclinic/api/vets/"};
    static final int BOOT_TIMEOUT_S = 120;
    static final Path BEFORE_JAR = Paths.get("petclinic-legacy.jar");

    static Path app;
    static boolean ko = Locale.getDefault().getLanguage().equals("ko");
    static List<Check_> checks = new ArrayList<>();
    static List<String[]> changes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        for (String a : args) {
            if (a.equals("--ko")) ko = true;
            if (a.equals("--en")) ko = false;
        }
        app = Paths.get("app");
        if (!Files.isDirectory(app)) app = Paths.get(".");
        if (!Files.isRegularFile(app.resolve("pom.xml"))) {
            System.out.println(t("pom.xml을 찾지 못했습니다. 압축을 푼 폴더에서 실행하세요.",
                                 "No pom.xml found. Run this from the unzipped folder."));
            System.exit(2);
        }

        header();
        String buildLog = checkBuild();
        checkTests(buildLog);
        checkStartup();

        collectChanges();
        report();
        writeResultJson();
    }

    // ---- the three things that decide whether the work is finished --------

    static String checkBuild() throws Exception {
        Check_ c = check(t("빌드", "Build"));
        say(t("빌드 중… 처음에는 라이브러리를 받느라 몇 분 걸립니다.",
              "Building... the first run downloads libraries and takes a few minutes."));
        ProcessBuilder pb = new ProcessBuilder(mavenCommand(), "-B", "clean", "package")
                .directory(app.toFile()).redirectErrorStream(true)
                .redirectOutput(Paths.get("build.log").toFile());
        int exit = pb.start().waitFor();
        String out = read(Paths.get("build.log"));
        if (exit == 0) c.pass("BUILD SUCCESS");
        else c.fail(firstError(out), t("자세한 내용은 build.log", "see build.log"));
        return out;
    }

    /** Tests must still run, and must not have been weakened to get there. */
    static void checkTests(String buildLog) throws Exception {
        Check_ c = check(t("테스트", "Tests"));
        Path tests = app.resolve("src/test");
        if (!Files.isDirectory(tests)) { c.fail(t("src/test 가 사라졌습니다", "src/test is gone")); return; }

        int annotations = countMatches(tests, "@Test\\b");
        int disabled = countMatches(tests, "@Ignore\\b|@Disabled\\b");
        int assertions = countMatches(tests, "\\b(assert[A-Za-z]*|verify|andExpect)\\s*\\(");
        Surefire sf = surefire();

        List<String> weakened = new ArrayList<>();
        if (annotations < BASE_TEST_ANNOTATIONS)
            weakened.add(t("@Test 가 " + BASE_TEST_ANNOTATIONS + " → " + annotations,
                           "@Test went " + BASE_TEST_ANNOTATIONS + " → " + annotations));
        if (disabled > 0)
            weakened.add(t(disabled + "개가 비활성화됨", disabled + " disabled"));
        if (assertions < BASE_ASSERTIONS)
            weakened.add(t("검증문이 " + BASE_ASSERTIONS + " → " + assertions,
                           "assertions went " + BASE_ASSERTIONS + " → " + assertions));
        if (sf.ran && sf.skipped > 0)
            weakened.add(t(sf.skipped + "개 건너뜀", sf.skipped + " skipped"));
        String pom = read(app.resolve("pom.xml"));
        if (pom.matches("(?s).*<skipTests>\\s*true.*")) weakened.add("pom.xml: <skipTests>true</skipTests>");
        if (buildLog.contains("-DskipTests") || buildLog.contains("-Dmaven.test.skip"))
            weakened.add(t("빌드가 테스트를 건너뜀", "the build skipped tests"));

        if (!weakened.isEmpty()) {
            c.fail(t("판정 기준이 낮아졌습니다", "the bar was lowered"), String.join(" · ", weakened));
        } else if (!sf.ran) {
            c.skip(t("테스트가 실행되지 않았습니다", "tests never ran"));
        } else if (sf.failures == 0 && sf.errors == 0 && sf.tests >= BASE_TESTS) {
            c.pass(sf.tests + t("개 통과", " passed"), t("비활성 0 · 검증문 " + assertions,
                                                       "0 disabled · " + assertions + " assertions"));
        } else {
            c.fail(sf.tests + " run · " + sf.failures + " failed · " + sf.errors + " errors");
        }
    }

    /** The packaged app has to start on its own and answer a real request. */
    static void checkStartup() throws Exception {
        Check_ c = check(t("기동", "Startup"));
        Path jar = builtJar();
        if (jar == null) { c.skip(t("빌드된 jar가 없습니다", "no jar was built")); return; }

        say(t("앱을 띄우는 중…", "Starting the app..."));
        Process p = new ProcessBuilder("java", "-jar", jar.toAbsolutePath().toString(),
                                       "--server.port=" + PORT)
                .redirectErrorStream(true).redirectOutput(new File("boot.log")).start();
        try {
            HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
            long deadline = System.currentTimeMillis() + BOOT_TIMEOUT_S * 1000L;
            while (System.currentTimeMillis() < deadline) {
                if (!p.isAlive()) { c.fail(t("앱이 뜨지 못했습니다", "the app failed to start"), bootCause()); return; }
                for (String path : HEALTH_PATHS) {
                    try {
                        HttpResponse<String> r = http.send(
                                HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + PORT + path))
                                        .timeout(Duration.ofSeconds(3)).GET().build(),
                                HttpResponse.BodyHandlers.ofString());
                        if (r.statusCode() == 200) {
                            c.pass("HTTP 200  " + path);
                            howToRun(jar);
                            return;
                        }
                    } catch (Exception ignored) { }
                }
                Thread.sleep(2000);
            }
            c.fail(t(BOOT_TIMEOUT_S + "초 안에 응답하지 않았습니다", "no answer within " + BOOT_TIMEOUT_S + "s"), bootCause());
        } finally {
            p.destroy();
            p.waitFor(15, java.util.concurrent.TimeUnit.SECONDS);
            p.destroyForcibly();
        }
    }

    /**
     * The jar's name changes with the version, so print the command rather than
     * making anyone work it out - and the address of the API console the
     * application serves itself, which is the thing worth looking at.
     */
    static void howToRun(Path jar) {
        say(t("직접 띄우려면:  java -jar " + jar, "run it yourself:  java -jar " + jar));
        say(t("API 콘솔:      http://localhost:9966/petclinic/swagger-ui.html",
              "API console:     http://localhost:9966/petclinic/swagger-ui.html"));
    }

    // ---- what changed, read off the built artifact ------------------------

    static void collectChanges() {
        // Before the work starts there is no freshly built jar, so the shipped one
        // is the current artifact - that way the report reads the same at every stage.
        Path built = builtJarQuiet();
        Map<String, String> before = libraries(BEFORE_JAR);
        Map<String, String> after = libraries(built != null ? built : BEFORE_JAR);

        int major = dominantBytecode();
        changes.add(new String[]{t("Java 바이트코드", "Java bytecode"),
                arrow("8", major > 0 ? jdkName(major) : "8", jdkName(TARGET_MAJOR))});

        String boot = springBootVersion();
        if (boot != null)
            changes.add(new String[]{"Spring Boot", arrow("2.1.5.RELEASE", boot, null)});

        if (!before.isEmpty() && !after.isEmpty()) {
            int changed = 0;
            for (Map.Entry<String, String> e : before.entrySet())
                if (after.containsKey(e.getKey()) && !after.get(e.getKey()).equals(e.getValue())) changed++;
            changes.add(new String[]{t("라이브러리", "Libraries"),
                    t(before.size() + "개 중 " + changed + "개 버전 변경",
                      changed + " of " + before.size() + " at a different version")});
        }
    }

    /** "a  →  b", or just the value with the goal in brackets when nothing moved yet. */
    static String arrow(String before, String after, String goal) {
        if (!before.equals(after)) return before + "  →  " + after;
        return after + (goal == null || goal.equals(after) ? "" : t("   (목표 " + goal + ")", "   (goal " + goal + ")"));
    }

    /** artifactId -> version, read from the fat jar's bundled libraries. */
    static Map<String, String> libraries(Path jar) {
        Map<String, String> out = new LinkedHashMap<>();
        if (jar == null || !Files.isRegularFile(jar)) return out;
        Pattern p = Pattern.compile("(.+)-(\\d[^/]*)\\.jar$");
        try (ZipFile zf = new ZipFile(jar.toFile())) {
            for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements(); ) {
                String name = e.nextElement().getName();
                if (!name.startsWith("BOOT-INF/lib/") || !name.endsWith(".jar")) continue;
                Matcher m = p.matcher(name.substring("BOOT-INF/lib/".length()));
                if (m.matches()) out.put(m.group(1), m.group(2));
            }
        } catch (IOException ignored) { }
        return out;
    }

    static int dominantBytecode() {
        Path classes = app.resolve("target/classes");
        if (!Files.isDirectory(classes)) return -1;
        try (Stream<Path> s = Files.walk(classes)) {
            return s.filter(x -> x.toString().endsWith(".class")).map(Check::majorVersion)
                    .filter(v -> v > 0)
                    .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(-1);
        } catch (IOException e) { return -1; }
    }

    // ---- output -----------------------------------------------------------

    static void header() {
        System.out.println();
        System.out.println("  " + t("현대화 리포트", "Modernization report") + "   "
                + app.toAbsolutePath().normalize().getFileName());
        System.out.println();
    }

    static void report() {
        if (!changes.isEmpty()) {
            System.out.println();
            System.out.println("  " + t("바뀐 것", "What changed"));
            for (String[] row : changes) System.out.println("     " + pad(row[0], 26) + row[1]);
        }
        int passed = (int) checks.stream().filter(x -> x.state == 1).count();
        System.out.println();
        System.out.println("  " + t("완료 확인", "Finished?") + "   " + passed + " / " + checks.size());
        System.out.println();
        System.out.println("  " + verdict(passed));
        System.out.println();
    }

    static String verdict(int passed) {
        if (passed == checks.size())
            return t("빌드·테스트·기동을 실제로 실행해서 확인했습니다.",
                     "Build, tests and a real startup were all executed and verified.");
        if (state(t("빌드", "Build")) == -1 && dominantBytecode() < TARGET_MAJOR)
            return t("아직 현대화 전 상태입니다. 앱이 고장난 게 아니라 Java 21로 옮겨지지 않은 것입니다.",
                     "Still the original state - not a broken app, just one that has not moved to Java 21 yet.");
        if (state(t("테스트", "Tests")) == -1)
            return t("테스트가 약해졌습니다. 통과한 게 아니라 판정 기준이 낮아진 것입니다.",
                     "The tests were weakened - the bar moved, not the result.");
        return t("남은 항목을 Bob에게 이어서 맡기세요. build.log 와 boot.log 를 그대로 주면 됩니다.",
                 "Hand the rest back to Bob - build.log and boot.log are the input it needs.");
    }

    static void writeResultJson() {
        StringBuilder sb = new StringBuilder("{\n  \"checks\": {\n");
        for (int i = 0; i < checks.size(); i++) {
            Check_ c = checks.get(i);
            sb.append("    \"").append(esc(c.name)).append("\": {\"state\": \"").append(c.stateName())
              .append("\", \"detail\": \"").append(esc(c.detail)).append("\"}")
              .append(i < checks.size() - 1 ? "," : "").append("\n");
        }
        sb.append("  },\n  \"changes\": {\n");
        for (int i = 0; i < changes.size(); i++)
            sb.append("    \"").append(esc(changes.get(i)[0])).append("\": \"").append(esc(changes.get(i)[1]))
              .append("\"").append(i < changes.size() - 1 ? "," : "").append("\n");
        sb.append("  },\n  \"passed\": ").append(checks.stream().filter(x -> x.state == 1).count())
          .append(",\n  \"total\": ").append(checks.size())
          .append(",\n  \"jdk\": \"").append(esc(System.getProperty("java.version"))).append("\"\n}\n");
        try { Files.write(Paths.get("result.json"), sb.toString().getBytes("UTF-8")); }
        catch (IOException ignored) { }
    }

    // ---- plumbing ---------------------------------------------------------

    static class Check_ {
        final String name; int state = 0; String detail = "";   // 0 skip, 1 pass, -1 fail
        Check_(String name) { this.name = name; }
        void pass(String... d) { state = 1; detail = join(d); print("[ OK ]"); }
        void fail(String... d) { state = -1; detail = join(d); print("[FAIL]"); }
        void skip(String... d) { state = 0; detail = join(d); print("[ -- ]"); }
        String stateName() { return state == 1 ? "pass" : state == -1 ? "fail" : "not_run"; }
        void print(String mark) { System.out.println("  " + mark + "  " + pad(name, 12) + detail); }
    }
    static String join(String[] d) { return String.join(" · ", d); }
    static Check_ check(String name) { Check_ c = new Check_(name); checks.add(c); return c; }
    static int state(String name) {
        return checks.stream().filter(c -> c.name.equals(name)).mapToInt(c -> c.state).findFirst().orElse(0);
    }
    static void say(String s) { System.out.println("        " + s); }
    static String t(String korean, String english) { return ko ? korean : english; }

    /** The project ships a Maven wrapper, so nobody has to install Maven - Windows included. */
    static String mavenCommand() {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        String wrapper = windows ? "mvnw.cmd" : "mvnw";
        if (Files.isRegularFile(app.resolve(wrapper))) return app.resolve(wrapper).toAbsolutePath().toString();
        return windows ? "mvn.cmd" : "mvn";
    }

    static class Surefire { boolean ran; int tests, failures, errors, skipped; }
    static Surefire surefire() {
        Surefire s = new Surefire();
        Path dir = app.resolve("target/surefire-reports");
        if (!Files.isDirectory(dir)) return s;
        try (Stream<Path> f = Files.list(dir)) {
            for (Path p : f.filter(x -> x.toString().endsWith(".xml")).collect(Collectors.toList())) {
                Matcher m = Pattern.compile("<testsuite\\b[^>]*>").matcher(read(p));
                if (!m.find()) continue;
                String a = m.group();
                s.ran = true;
                s.tests += attr(a, "tests"); s.failures += attr(a, "failures");
                s.errors += attr(a, "errors"); s.skipped += attr(a, "skipped");
            }
        } catch (IOException ignored) { }
        return s;
    }
    static int attr(String tag, String key) {
        Matcher m = Pattern.compile(key + "=\"(\\d+)\"").matcher(tag);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    static int countMatches(Path dir, String regex) throws IOException {
        Pattern pat = Pattern.compile(regex);
        try (Stream<Path> s = Files.walk(dir)) {
            return s.filter(p -> p.toString().endsWith(".java"))
                    .mapToInt(p -> { Matcher m = pat.matcher(read(p)); int n = 0; while (m.find()) n++; return n; })
                    .sum();
        }
    }

    static Path builtJar() throws IOException {
        Path target = app.resolve("target");
        if (!Files.isDirectory(target)) return null;
        try (Stream<Path> s = Files.list(target)) {
            return s.filter(p -> p.toString().endsWith(".jar"))
                    .filter(p -> !p.toString().endsWith("-sources.jar"))
                    .max(Comparator.comparingLong(p -> p.toFile().length())).orElse(null);
        }
    }
    static Path builtJarQuiet() { try { return builtJar(); } catch (IOException e) { return null; } }

    static String springBootVersion() {
        Matcher m = Pattern.compile("spring-boot-starter-parent</artifactId>\\s*<version>([^<]+)")
                .matcher(read(app.resolve("pom.xml")));
        return m.find() ? m.group(1) : null;
    }

    static int majorVersion(Path classFile) {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(classFile)))) {
            if (in.readInt() != 0xCAFEBABE) return -1;
            in.readUnsignedShort();
            return in.readUnsignedShort();
        } catch (IOException e) { return -1; }
    }
    static String jdkName(int major) { return String.valueOf(major - 44); }

    static String firstError(String log) {
        for (String line : log.split("\n")) {
            if (!line.startsWith("[ERROR]") || line.length() <= 10) continue;
            String s = line.substring(8).trim().replaceAll("^Failed to execute goal \\S*?:", "")
                                               .replaceAll(" on project \\S+", "").trim();
            return shorten(s);
        }
        return "BUILD FAILURE";
    }

    /** The line from boot.log that says why the app would not start - the line to hand to Bob. */
    static String bootCause() {
        String[] lines = read(Paths.get("boot.log")).split("\n");
        String root = null;                       // the LAST "Caused by" is the root cause
        for (String line : lines) {
            String s = line.trim();
            if (s.startsWith("Caused by:")) root = s.substring(10).trim();
        }
        if (root != null) return shorten(root);
        for (String line : lines) {
            String s = line.trim();
            if (s.contains("Exception") || s.contains("APPLICATION FAILED TO START")) return shorten(s);
        }
        return t("boot.log 를 보세요", "see boot.log");
    }

    static String shorten(String s) { return s.length() > 72 ? s.substring(0, 69) + "..." : s; }

    /** Pad to a column width, counting CJK characters as two cells so the columns line up. */
    static String pad(String s, int width) {
        int w = 0;
        for (char c : s.toCharArray()) w += wide(c) ? 2 : 1;
        StringBuilder sb = new StringBuilder(s);
        while (w++ < width) sb.append(' ');
        return sb.toString();
    }
    static boolean wide(char c) {
        return (c >= 0x1100 && c <= 0x115F) || (c >= 0x2E80 && c <= 0xA4CF)
            || (c >= 0xAC00 && c <= 0xD7A3) || (c >= 0xF900 && c <= 0xFAFF)
            || (c >= 0xFF00 && c <= 0xFF60);
    }

    static String read(Path p) {
        try { return new String(Files.readAllBytes(p), "UTF-8"); } catch (IOException e) { return ""; }
    }
    static String esc(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}
