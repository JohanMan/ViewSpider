package com.johan.view.spider.processor;

public class StringUtil {

    /**
     * 是否为空
     * @param text 内容
     * @return 结果
     */
    public static boolean isEmpty(String text) {
        return !isNotEmpty(text);
    }

    /**
     * 是否不为空
     * @param text 内容
     * @return 结果
     */
    public static boolean isNotEmpty(String text) {
        return text != null && text.length() > 0;
    }

    /**
     * 转小写 HelloWorld -> hello_world
     * @param text 内容
     * @return 结果
     */
    public static String toLowercase(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                if (builder.length() == 0) {
                    builder.append(Character.toLowerCase(c));
                } else {
                    builder.append("_").append(Character.toLowerCase(c));
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 转大写 hello_world -> helloWorld
     * @param text 内容
     * @return 结果
     */
    public static String toUppercase(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '_' ) {
                if (i < text.length() - 1) {
                    char nextC = text.charAt(++i);
                    builder.append(Character.toUpperCase(nextC));
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
