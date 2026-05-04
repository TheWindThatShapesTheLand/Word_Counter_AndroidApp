package com.example.wordcounter;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordFilter {
    public static Map<String, Integer> keepRussianOnly(Map<String, Integer> map) {

        Map<String, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String word = entry.getKey();

            if (word.matches("[а-яё]+")) {
                result.put(word, entry.getValue());
            }
        }

        return result;
    }
}