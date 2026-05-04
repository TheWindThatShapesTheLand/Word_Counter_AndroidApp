package com.example.wordcounter;

import java.util.*;

public class WordSorter {

    public static Map<String, Integer> sortAscending(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        return toMap(list);
    }

    public static Map<String, Integer> sortDescending(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        return toMap(list);
    }

    public static Map<String, Integer> sortAlphaAsc(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());

        return toMap(list);
    }

    public static Map<String, Integer> sortAlphaDesc(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getKey().compareTo(a.getKey()));

        return toMap(list);
    }

    private static Map<String, Integer> toMap(List<Map.Entry<String, Integer>> list) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : list) {
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }
}