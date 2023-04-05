package searchengine.services.page_parser;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PageValidator {

    private static Set<String> urlsSite = new HashSet<>();

    private static final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex",
                    "google", "webp", "zip", "wa.me", "+", ",", "tel:", "@", "tg://", "#", "telegram");

    public static Set<String> getUrlsSite() {
        return urlsSite;
    }

    public static List<String> getSTOP_WORDS() {
        return STOP_WORDS;
    }

    public static boolean urlNotContainsStopWords(String url) {
        return getSTOP_WORDS().stream().noneMatch(url::contains);
    }

    public static String getPathFromUrl(String pageUrl) {
        String path = "";
        try {
            URL url = new URL(pageUrl);
            if (pageUrl.startsWith("/")) {
                path = pageUrl;
            } else if (pageUrl.startsWith("http")) {
                path = url.getPath();
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public static void removeUrls(Set<String> urlsSet) {
        urlsSet.removeIf(i -> true);
    }
}
