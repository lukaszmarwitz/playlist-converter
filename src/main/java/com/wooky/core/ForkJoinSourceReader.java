package com.wooky.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForkJoinSourceReader extends RecursiveTask<List<String>> {

    private final transient Engine engine;
    private final int threshold;
    private final Pattern pattern;
    private final String[] urlArray;
    private int lowestIndex;
    private int highestIndex;

    ForkJoinSourceReader(Engine engine, int threshold, Pattern pattern, String[] urlArray) {
        this.engine = engine;
        this.threshold = threshold;
        this.pattern = pattern;
        this.urlArray = urlArray;
        this.lowestIndex = 0;
        this.highestIndex = urlArray.length;
    }

    private ForkJoinSourceReader(Engine engine, int threshold, Pattern pattern, String[] urlArray, int lowestIndex, int highestIndex) {
        this.engine = engine;
        this.threshold = threshold;
        this.pattern = pattern;
        this.urlArray = urlArray;
        this.lowestIndex = lowestIndex;
        this.highestIndex = highestIndex;
    }

    @Override
    protected List<String> compute() {
        if ((highestIndex - lowestIndex) <= threshold) {

            List<String> list = new ArrayList<>();

            for (int i = lowestIndex; i < highestIndex; i++) {

                Matcher matcher = this.pattern.matcher(engine.websiteSourceReader(urlArray[i]));

                    if (matcher.find()) {
                        list.add(matcher.group(1));
                    }
            }

            return list;
        } else {
            int middleIndex = (highestIndex - lowestIndex) / 2 + lowestIndex;

            ForkJoinSourceReader left = new ForkJoinSourceReader(engine, threshold, pattern, urlArray, lowestIndex, middleIndex);
            ForkJoinSourceReader right = new ForkJoinSourceReader(engine, threshold, pattern, urlArray, middleIndex, highestIndex);

            left.fork();
            right.fork();

            left.join().addAll(right.join());

            return left.join();
        }
    }
}
