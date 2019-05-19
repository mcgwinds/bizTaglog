package com.mcg.bizlog.boot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BizLogBanner {

    private final static String FILENAME="banner";

    public void printBanner() {

        try {
            ClassLoader classLoader = findClassLoader();
            InputStream inputStream;
            if (classLoader != null) {
                inputStream = classLoader.getResourceAsStream(FILENAME);
            } else {
                inputStream = ClassLoader.getSystemResourceAsStream(FILENAME);
            }
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            try {

                while(((line = reader.readLine()) != null) ) {
                    System.out.println(line);
                }
            }
            finally {
                reader.close();
            }
        }
        catch (Exception e) {

        }

    }

        private static ClassLoader findClassLoader() {
                return BizLogBanner.class.getClassLoader();
        }

}
