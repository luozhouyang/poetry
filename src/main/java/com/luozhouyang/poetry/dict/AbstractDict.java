package com.luozhouyang.poetry.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract dict loader
 *
 * @author luozhouyang
 */
public abstract class AbstractDict implements IDictLoader {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected long mStartTime;

    @Override
    public void onStart() {
        logger.info("Load dict started...");
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onSuccess() {
        long end = System.currentTimeMillis();
        logger.info("Load dict successfully. Cost {}ms.", end - mStartTime);
    }

    @Override
    public void onException(Exception e) {
        long end = System.currentTimeMillis();
        logger.warn("Load dict failed. Cost {}ms. Exception: ", end - mStartTime, e);
    }
}
