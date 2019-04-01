package com.test.demo.helper;

public class LineSyntaxHelper {

    public static boolean verify (String line) {
        if (line.startsWith("{") && line.endsWith("}")) {
            return true;
        }
        return false;
    }
}
