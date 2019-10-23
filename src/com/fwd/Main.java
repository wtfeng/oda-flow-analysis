package com.fwd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.*;

public class Main {

    int previousLevel = 0;
    int previousStartNumOfSpace = 0;

    public static void main(String[] args) {
        String flow = readLineByLineJava8("D:\\doc\\ready go\\oda flow analysis\\flow.source");

        //System.out.println(flow);

        List<Intent> intents = new Main().parseIntents(flow);

        System.out.println("digraph G{");
        for (Intent intent : intents) {
            if (intent.getNextIntents() != null)
                for (Intent nextIntent : intent.getNextIntents()) {
                    System.out.println(intent.getName() + "->" + nextIntent.getName() + ";");
                }
        }
        System.out.println("}");
    }


    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public List<Intent> parseIntents(String lines) {
        List<Intent> intents = new ArrayList<>();
        String[] lineArray = lines.split("\n");
        Intent intent = null;
        List<Intent> nextIntents = null;
        boolean actionStarted = false;
        for (int i = 0; i < lineArray.length; i++) {
            //String line = lineArray[i];

            String line = lineArray[i].trim();

            if (line.startsWith("#") ||line.startsWith("\\") || line.length() == 0) {
                continue;
            }

            int level = this.getLevelAndUpdate(lineArray[i]);


//            if (line.startsWith("  ") && !line.substring(2, 3).equals(" ") && line.endsWith(":")) {
            if (level == 1) {
                String name = line.substring(0, line.length() - 1);
                intent = new Intent();
                intent.setName(name);
                intents.add(intent);
                continue;
            }

            if (level == 3 && line.startsWith("next:")) {
                String name = null;
                try {
                    name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                } catch (Exception e) {
                    throw e;
                }
                nextIntents = new ArrayList<>();
                intent.setNextIntents(nextIntents);

                Intent nextIntent = new Intent();
                nextIntent.setName(name);
                nextIntents.add(nextIntent);
                continue;
            }

            if (level == 3 && line.equals("actions:")) {
                actionStarted = true;
                nextIntents = new ArrayList<>();
                intent.setNextIntents(nextIntents);
                continue;
            }

            if (actionStarted) {
                if (level == 4) {
                    String name = null;
                    try {
                        name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    } catch (Exception e) {
                        throw e;
                    }
                    Intent nextIntent = new Intent();
                    nextIntent.setName(name);
                    nextIntents.add(nextIntent);
                } else {
                    actionStarted = false;
                }
            }


            //System.out.println(line);
        }
        return intents;
    }

    public int getLevelAndUpdate(String line) {

        int startNumOfSpace = getStartNumOfSpace(line);
        int level = 0;
        if (startNumOfSpace > 0) {
            if (startNumOfSpace > previousStartNumOfSpace) {
                level = previousLevel + 1;
            } else {
                level = getHistoryLevel(startNumOfSpace);
            }
            updateHistoryLevel(startNumOfSpace, level);
        }
        //update
        previousLevel = level;
        previousStartNumOfSpace = startNumOfSpace;
        return level;

    }

    private void updateHistoryLevel(int startNumOfSpace, int level) {
        if (historyLevels.size() > level) {
            historyLevels.set(level, startNumOfSpace);
        } else {
            historyLevels.add(startNumOfSpace);
            if (historyLevels.size() - 1 != level) {
                throw new RuntimeException("error");
            }
        }
    }

    List<Integer> historyLevels = new ArrayList<>();

    {
        historyLevels.add(0, 0);
    }

    private int getHistoryLevel(int startNumOfSpace) {
        for (int level = 0; level < historyLevels.size(); level++) {
            if (historyLevels.get(level) == startNumOfSpace) {
                return level;
            }
        }
        throw new RuntimeException("error");
    }


    public static int getStartNumOfSpace(String line) {
        int num = 0;
        for (int i = 0; i < line.length(); i++) {
            if (" ".charAt(0) == line.charAt(i)) {
                num++;
            } else {
                return num;
            }
        }
        return num;
    }


    public static String rightTrim(String line) {
        int count = line.length();
        for (int i = line.length() - 1; i >= 0; i--) {
            if (line.charAt(i) == " ".charAt(0)) {
                count = i;
            } else {
                break;
            }
        }
        return line.substring(0, count);
    }
}
