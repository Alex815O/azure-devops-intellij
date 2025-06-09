package com.microsoft.alm.plugin.context.rest;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class UrlUtils {


    public static String replacePathParameters(final String url, final Map<String, String> pathParameters) {

        AtomicReference<String> replacedUrl = new AtomicReference<>(url);
        pathParameters.forEach((key, value) ->
                replacedUrl.set(replacedUrl.get().replaceAll("\\{" + key + "\\}", value))
        );
        return replacedUrl.get();
    }

    public static String addParameters(final String url, final Map<String, String> parameters) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        parameters.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
        return builder.substring(0, builder.length() - 1);
    }
}
