package tech.saturdev.pakakumi.service;

import org.springframework.stereotype.Service;

@Service
public class ScraperStatusService {
    private int requestCount = 0;
    private int cacheClearCount = 0;
    private int browserRestartCount = 0;

    public synchronized void incrementRequestCount() {
        requestCount++;
    }

    public synchronized void incrementCacheClearCount() {
        cacheClearCount++;
    }

    public synchronized void incrementBrowserRestartCount() {
        browserRestartCount++;
    }

    public synchronized int getRequestCount() {
        return requestCount;
    }

    public synchronized int getCacheClearCount() {
        return cacheClearCount;
    }

    public synchronized int getBrowserRestartCount() {
        return browserRestartCount;
    }

    public synchronized void setBrowserRestartCount(int browserRestartCount) {
        this.browserRestartCount = browserRestartCount;
    }

    public synchronized void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public synchronized void setCacheClearCount(int cacheClearCount) {
        this.cacheClearCount = cacheClearCount;
    }
}
