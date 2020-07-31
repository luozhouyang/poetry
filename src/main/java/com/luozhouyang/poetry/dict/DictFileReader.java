package com.luozhouyang.poetry.dict;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * A helper class to read dict file(s).
 *
 * @author luozhouyang
 */
public class DictFileReader {

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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
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
