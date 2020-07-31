package com.luozhouyang.poetry.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manager for loading dict(s).
 *
 * @author luozhouyang
 */
public class DictManager {

    private static final Logger logger = LoggerFactory.getLogger(DictManager.class);

    private final Map<String, IDictLoader> mDictionaries = new HashMap<>();

    private static volatile boolean initialized = false;

    private DictManager() {

    }

    public static DictManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Add a dict
     *
     * @param dictLoader dict
     * @return this
     */
    public DictManager add(IDictLoader dictLoader) {
        mDictionaries.put(dictLoader.getClass().getSimpleName(), dictLoader);
        return this;
    }

    /**
     * Load dict(s) one by one.
     */
    public void manage() {
        if (initialized) {
            logger.info("DictManager has been initialized.");
            return;
        }
        for (IDictLoader dict : mDictionaries.values()) {
            DictRunnableAdapter adapter = new DictRunnableAdapter(dict);
            adapter.run();
        }
        initialized = true;
    }

    /**
     * Load dict(s) in parallel
     *
     * @param parallel         max threads
     * @param keepAliveSeconds thread's keep alive time in seconds
     */
    public void manage(int parallel, long keepAliveSeconds) {
        if (initialized) {
            logger.info("DictManager has been initialized.");
            return;
        }
        ExecutorService pool = new ThreadPoolExecutor(0, parallel, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (IDictLoader dict : mDictionaries.values()) {
            pool.submit(new DictRunnableAdapter(dict));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(120L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("ThreadPool await exception: ", e);
            e.printStackTrace();
        }
        initialized = true;
    }

    /**
     * Load dict(s) in parallel
     *
     * @param parallel max threads
     */
    public void manage(int parallel) {
        this.manage(parallel, 120L);
    }

    /**
     * Runnable adapter for dict loader.
     */
    private static class DictRunnableAdapter implements Runnable {

        private final IDictLoader dict;

        public DictRunnableAdapter(IDictLoader dict) {
            this.dict = dict;
        }

        @Override
        public void run() {
            try {
                dict.onStart();
                dict.onLoad();
                dict.onSuccess();
            } catch (Exception e) {
                dict.onException(e);
            }
        }
    }

    private static final class Holder {
        private static final DictManager INSTANCE = new DictManager();
    }
}
