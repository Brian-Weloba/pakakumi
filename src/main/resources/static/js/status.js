$(document).ready(function () {
    setInterval(function () {
        $.getJSON("/api/status", function (status) {
            $("#requestCount").text(status.requestCount);
            $("#cacheClearCount").text(status.cacheClearCount);
            $("#browserRestartCount").text(status.browserRestartCount);
        });
    }, 1000); // Update every 1 seconds
});
