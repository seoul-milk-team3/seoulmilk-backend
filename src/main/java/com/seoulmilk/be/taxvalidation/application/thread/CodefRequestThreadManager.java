package com.seoulmilk.be.taxvalidation.application.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodefRequestThreadManager {
    private static final Map<String, List<CodefRequestThread>> runningThreads = new HashMap<>();

    public static synchronized void addThread(String key, CodefRequestThread thread) {  //key는 유저의 이메일 해시값
        runningThreads.computeIfAbsent(key, k -> new ArrayList<>()).add(thread);
    }

    public static synchronized void notifyUserThread(String key) {
        List<CodefRequestThread> threads = runningThreads.get(key);
        if (threads != null) {
            for (CodefRequestThread thread : threads) {
                synchronized (thread.getMonitor()) {
                    thread.getMonitor().notifyAll();
                }
            }
        }
        runningThreads.remove(key);
    }
}