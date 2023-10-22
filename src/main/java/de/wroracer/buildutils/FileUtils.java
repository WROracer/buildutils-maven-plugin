package de.wroracer.buildutils;

import java.io.BufferedReader;
import java.io.File;

public class FileUtils {
    public static String readFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
