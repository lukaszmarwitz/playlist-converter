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
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class Engine {

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);
    private static final String URL_APPLE = "https://itunes.apple.com/pl/playlist/tech/pl.2af7d699174148d98c6ae5f66ab70591";
    private static final String CODE_BEFORE_ARTIST = "dir=\"ltr\" aria-label=\"";
    private static final String CODE_AFTER_ARTIST = "\" id";
    private static final String CODE_BEFORE_TITLE = "class=\"spread\" aria-label=\"";
    private static final String CODE_AFTER_TITLE = "\" data-test-song-link-wrapper";
    private static final String URL_YOUTUBE = "https://www.youtube.com/results?search_query=";
    private static final String CODE_BEFORE_YT_URL = "ytimg.com/vi/";
    private static final String CODE_AFTER_YT_URL = "/hqdefault";

    public ArrayList<String> urlList(URL url) {
        ArrayList<URL> list = youtubeSearchUrlBuilder(url);
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            result.add(youtubeUrlResults(list.get(i)));
        }

        return result;
    }

    private String websiteSourceReader(URL url) {

        LOG.info("Website source reader URL: {}", url.toString());

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine.trim());
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        return stringBuilder.toString();
    }

    private Map<Integer, String> patternMatcher(String source, String prefix, String postfix) {

        Pattern p = Pattern.compile(prefix + "(.*)" + postfix);
        Matcher m = p.matcher(source);

        int counter = 1;

        Map<Integer, String> list = new TreeMap<>();

        while (m.find()) {
            String result = m.group(1).replaceAll(" ", "+").replaceAll("&amp;", "");
            list.put(counter++, result);
        }

        return list;
    }

    private ArrayList<URL> youtubeSearchUrlBuilder(URL url) {

        LOG.info("String builder URL: {}", url.toString());

        Map<Integer, String> artistsList = patternMatcher(websiteSourceReader(url), CODE_BEFORE_ARTIST, CODE_AFTER_ARTIST);
        Map<Integer, String> titlesList = patternMatcher(websiteSourceReader(url), CODE_BEFORE_TITLE, CODE_AFTER_TITLE);
        ArrayList<URL> queryList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            URL output = null;
            try {
                output = new URL(URL_YOUTUBE + artistsList.get(i) + "+" + titlesList.get(i));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            queryList.add(output);
        }

        return queryList;
    }

    private String youtubeUrlResults(URL url) {

        Pattern p = Pattern.compile(CODE_BEFORE_YT_URL + "(\\S+)" + CODE_AFTER_YT_URL);
        Matcher m = p.matcher(websiteSourceReader(url));

        String result = "";

        if (m.find()) {
            result = (m.group(1));
        }

        return result;
    }
}
