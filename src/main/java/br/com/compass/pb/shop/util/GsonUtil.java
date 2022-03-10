package br.com.compass.pb.shop.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    private final static GsonBuilder GSON_BUILDER = new GsonBuilder();

    public static Gson getGsonWithExclusionStrategy() {
        return GSON_BUILDER
                .setExclusionStrategies(new GsonHiddenAnnotationStrategy())
                .create();
    }
}
