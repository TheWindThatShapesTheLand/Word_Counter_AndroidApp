package com.example.wordcounter;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordCounter {

    public static Map<String, Integer> countWords(String text) {

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-zA-Zа-яА-Я0-9 ]", " ")
                .trim();

        String[] words = cleaned.split("\\s+");

        Map<String, Integer> map = new LinkedHashMap<>();

        for (String w : words) {
            if (w.isEmpty()) continue;

            map.put(w, map.getOrDefault(w, 0) + 1);
        }

        return map;
    }

    public static int getTotalWords(Map<String, Integer> map) {
        int total = 0;
        for (int v : map.values()) {
            total += v;
        }
        return total;
    }
}