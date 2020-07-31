package com.luozhouyang.poetry.dict;

/**
 * Dict files loader interface.
 *
 * @author luozhouyang
 */
public interface IDictLoader {

    /**
     * Load start
     */
    void onStart();

    /**
     * Load dict file(s)
     *
     * @throws Exception exceptions
     */
    void onLoad() throws Exception;

    /**
     * Load dict file(s) successfully
     */
    void onSuccess();

    /**
     * Load dict file(s) failed.
     *
     * @param e exception
     */
    void onException(Exception e);
}
