package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String argPath = "";
        String prefix = "";
        boolean rewrite = true;
        boolean infFull = false;

        List<String> list = new ArrayList<>();
        List<Integer> listInt = new ArrayList<>();
        List<Float> listFlt = new ArrayList<>();


        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("-o")) {//путь

                argPath = args[i + 1];
                try {
                    Files.createDirectories(Paths.get(argPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (args[i].matches("-p")) {//префикс
                prefix = args[i + 1];
            }
            if (args[i].matches("-a")) {//дозаписать в файл
                rewrite = false;
            }
            if (args[i].matches("-s")) {//краткая информация
                infFull = false;
            }
            if (args[i].matches("-f")) {//полная информация
                infFull = true;
            }
            if (!args[i].matches("\\w+(.(?i)txt)")) continue;
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[i]))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    } else if (isInt(line)) {
                        listInt.add(Integer.parseInt(line.trim()));
                    } else if (IsFloat(line)) {
                        listFlt.add(Float.parseFloat(line.trim()));
                    } else list.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("Что-то не так с входными данными");
            }
        }

        if (!listInt.isEmpty()) {
            Path integerFile = Paths.get(argPath + prefix + "integers.txt");
            try {
                if (rewrite) {
                    Files.deleteIfExists(integerFile);
                    Files.createFile(integerFile);
                }
                for (Integer num : listInt) {
                    Files.writeString(integerFile, num + System.lineSeparator(),
                            StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                System.out.println("Проблема с выходным файлом integers");
            }
            countStrings(integerFile, infFull);
        }

        if (!listFlt.isEmpty()) {
            Path floatFile = Paths.get(argPath + prefix + "floats.txt");
            try {
                if (rewrite) {
                    Files.deleteIfExists(floatFile);
                    Files.createFile(floatFile);
                }
                for (Float flt : listFlt) {
                    Files.writeString(floatFile, flt + System.lineSeparator(),
                            StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                System.out.println("Проблема с выходным файлом floats");
            }
            countStrings(floatFile, infFull);
        }

        if (!list.isEmpty()) {
            Path strFile = Paths.get(argPath + prefix + "strings.txt");
            try {
                if (rewrite) {
                    Files.deleteIfExists(strFile);
                    Files.createFile(strFile);
                }
                for (String str : list) {
                    Files.writeString(strFile, str + System.lineSeparator(),
                            StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                System.out.println("Проблема с выходным файлом strings");
            }
            countStrings(strFile, infFull);
        }
    }

    public static void countStrings (Path path, boolean isInfFull) {

        List<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }  list.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Проблема прочтения файла при подсчете");
        }

        if(!isInfFull) System.out.println(path.getFileName() + " include " + list.size() +" string(s)!");
        if(isInfFull) {
        if ((path.getFileName()).toString().matches("\\S+(integers)+(.(?i)txt)")) {
        IntSummaryStatistics intSum = list.stream().mapToInt(Integer::parseInt).summaryStatistics();
            System.out.println(intSum.toString());
        } else if ((path.getFileName()).toString().matches("\\S+(floats)+(.(?i)txt)")) {
        DoubleSummaryStatistics doubleSum = list.stream().mapToDouble(Double::parseDouble).summaryStatistics();
            System.out.println(doubleSum.toString());
        } else if ((path.getFileName()).toString().matches("\\S+(strings)+(.(?i)txt)")) {
            String min = Collections.min(list, String.CASE_INSENSITIVE_ORDER);
            String max = Collections.max(list, String.CASE_INSENSITIVE_ORDER);
            System.out.println("String min is: " + min + "String max is: " + max);
        }
        }
    }

    public static boolean isInt(String str) {
        try {
            int v = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            System.out.println("Строка не Integer");
        }
        return false;
    }

    public static boolean IsFloat(String str) {
        try {
            float v = Float.parseFloat(str);
            return true;
        } catch (NumberFormatException nfe) {
            System.out.println("Строка не Float");
        }
        return false;
    }
}
