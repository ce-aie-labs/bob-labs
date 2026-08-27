(function () {
  document.querySelectorAll("[data-copy]").forEach(function (button) {
    button.addEventListener("click", function () {
      var target = document.getElementById(button.getAttribute("data-copy"));
      if (!target) return;
      var content = target.cloneNode(true);
      content.querySelectorAll("button").forEach(function (item) { item.remove(); });
      navigator.clipboard.writeText(content.textContent.trim()).then(function () {
        var original = button.textContent;
        button.textContent = document.documentElement.lang === "en" ? "Copied" : "복사됨";
        setTimeout(function () { button.textContent = original; }, 1400);
      });
    });
  });

  document.querySelectorAll("[data-check-id]").forEach(function (checkbox) {
    var key = "bobathon-guide:" + checkbox.getAttribute("data-check-id");
    checkbox.checked = localStorage.getItem(key) === "done";
    checkbox.addEventListener("change", function () {
      if (checkbox.checked) localStorage.setItem(key, "done");
      else localStorage.removeItem(key);
    });
  });

  document.querySelectorAll("[data-screenshot]").forEach(function (figure) {
    var image = new Image();
    image.className = "step-screenshot";
    image.alt = figure.getAttribute("data-alt") || "단계별 화면 캡처";
    image.addEventListener("load", function () {
      var placeholder = figure.querySelector(":scope > div");
      if (placeholder) placeholder.replaceWith(image);
    });
    image.src = figure.getAttribute("data-screenshot");
  });

  var themeToggle = document.getElementById("theme-toggle");
  if (themeToggle) {
    themeToggle.addEventListener("click", function () {
      var current = document.documentElement.getAttribute("data-theme");
      if (!current) {
        current = window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
      }
      var next = current === "dark" ? "light" : "dark";
      document.documentElement.setAttribute("data-theme", next);
      localStorage.setItem("theme", next);
    });
  }
})();
