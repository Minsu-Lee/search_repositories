package com.jackson.repositoriestest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Temp {

//    public static String newPassword(String a, String b) {
//        String.valueOf(a.charAt(0));
//    }

    public List<String> deviceNamesSystem(List<String> devicenames) {
        Map<String, Integer> nameCntMap = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (String name : devicenames) {
            int nameCnt = nameCntMap.getOrDefault(name, 0);
            String nameCntStr = nameCnt > 0 ? String.valueOf(nameCnt) : "";
            nameCntMap.put(name, nameCnt +1 );
            result.add(name + nameCntStr);
        }

        return result;
    }

    public char slowestKey(@NotNull List<List<Integer>> keyTimes) {
        final int alphabet = 97;
        Map<Character, Integer> pressTimeMap = new HashMap<>();

        int n = keyTimes.get(0).get(0); // keyTimes의 요소 수
        int col = keyTimes.get(1).get(0);   // 각 keyTimes[i]의 열 수
        char resultKey = 0;
        int maxTime = 0;

        for (int i = 2; i < keyTimes.size(); i++) {
            int key = keyTimes.get(i).get(0);
            int time = keyTimes.get(i).get(1);
            char keyVal = (char) (alphabet + key);

            int keyTotalTime = pressTimeMap.getOrDefault(keyVal, 0);
            pressTimeMap.put(keyVal, keyTotalTime + time);

            if (maxTime < keyTotalTime) {
                resultKey = keyVal;
                maxTime = keyTotalTime;
            }

            System.out.println("key " + key + ", time : " + time + ", result = " + (char) (alphabet + key) + " ( " + (alphabet + key) + " ) ");

        }

        System.out.println();
        System.out.println("result key : " + resultKey);
        System.out.println("result key time : " + maxTime);

        return resultKey;
    }
}
