package com.wooky.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class Engine {

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);
    private static final String URL_APPLE = "https://itunes.apple.com/pl/playlist/tech/pl.2af7d699174148d98c6ae5f66ab70591";
    private static final String URL_YOUTUBE = "https://www.youtube.com/results?search_query=";
    private static final Pattern PATTERN_APPLE_MUSIC_ARTIST = Pattern.compile("dir=\"ltr\" aria-label=\"(.*)\" id");
    private static final Pattern PATTERN_APPLE_MUSIC_TITLE = Pattern.compile("class=\"spread\" aria-label=\"(.*)\">");
    private static final Pattern PATTERN_YOUTUBE = Pattern.compile("ytimg.com/vi/(\\S+)/hqdefault");
    private static final int THRESHOLD = 5;

    public List<String> getVideoIdList(String urlAddress) {

        Engine engine = new Engine();

        String source = engine.websiteSourceReader(urlAddress);
        List<String> artistsList = engine.patternMatcher(source, PATTERN_APPLE_MUSIC_ARTIST);
        List<String> titlesList = engine.patternMatcher(source, PATTERN_APPLE_MUSIC_TITLE);
        String[] youtubeQueryArray = engine.youtubeQueryUrlBuilder(artistsList, titlesList);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        return forkJoinPool.invoke(new ForkJoinSourceReader(THRESHOLD, PATTERN_YOUTUBE, youtubeQueryArray));
    }

    String websiteSourceReader(String urlAddress) {

        LOG.info("Reading source from URL: {}", urlAddress);

        URL url = null;
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            LOG.error(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine.trim());
                stringBuilder.append(System.lineSeparator());
            }
        } catch (IOException e) {
            LOG.error(e.toString());
        }

        return stringBuilder.toString();
    }

    private List<String> patternMatcher(String source, Pattern pattern) {

        Matcher matcher = pattern.matcher(source);

        List<String> list = new ArrayList<>();

        while (matcher.find()) {
            list.add(matcher.group(1).replaceAll(" ", "+").replaceAll("&amp;", ""));
        }

        return list;
    }

    private String[] youtubeQueryUrlBuilder(List<String> firstList, List<String> secondList) {

        String[] array = new String[firstList.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = (URL_YOUTUBE + firstList.get(i) + "+" + secondList.get(i));
        }

        return array;
    }
}
