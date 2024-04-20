package com.github.catvod.net;

import android.text.TextUtils;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class OkCookieJar implements CookieJar {

    private CookieManager manager;

    public OkCookieJar() {
        try {
            manager = CookieManager.getInstance();
        } catch (Throwable ignored) {
        }
    }

    public static void sync(String url, String cookie) {
        if (TextUtils.isEmpty(cookie)) return;
        for (String split : cookie.split(";")) {
            CookieManager.getInstance().setCookie(url, split);
        }
    }

    @NonNull
    @Override
    public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        try {
            List<Cookie> items = new ArrayList<>();
            String cookie = manager.getCookie(url.toString());
            if (TextUtils.isEmpty(cookie)) return Collections.emptyList();
            for (String split : cookie.split(";")) items.add(Cookie.parse(url, split));
            return items;
        } catch (Throwable e) {
            return Collections.emptyList();
        }
    }

    @Override
    public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        try {
            for (Cookie cookie : cookies) manager.setCookie(url.toString(), cookie.toString());
        } catch (Throwable ignored) {
        }
    }
}