package com.luozhouyang.poetry.files;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class FileReader {
    /**
     * A callback when read file line
     */
    public interface ReadLineCallback {

        /**
         * Read a line
         *
         * @param lino number of line
         * @param line content of line
         */
        void readLine(int lino, String line);
    }

    /**
     * Read a file
     *
     * @param filePath file path
     * @param consumer consumer, for lambda functions
     */
    public static void readFile(String filePath, Consumer<String> consumer) {
        readFile(filePath, consumer, "UTF-8");
    }

    /**
     * Read a file
     *
     * @param filePath file path
     * @param consumer consumer, for lambda functions
     * @param charset  charset, default to UTF-8
     */
    public static void readFile(String filePath, Consumer<String> consumer, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (consumer != null)
                    consumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read a file
     *
     * @param filePath file path
     * @param callback read line callback, instance of {@link ReadLineCallback}
     */
    public static void readFile(String filePath, ReadLineCallback callback) {
        readFile(filePath, callback, "UTF-8");
    }

    /**
     * Read a file
     *
     * @param filePath file path
     * @param callback read line callback, instance of {@link ReadLineCallback}
     * @param charset  charset, default to UTF-8
     */
    public static void readFile(String filePath, ReadLineCallback callback, String charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset))) {
            String line;
            int lino = 0;
            while ((line = reader.readLine()) != null) {
                if (callback != null)
                    callback.readLine(lino, line);
                lino++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
