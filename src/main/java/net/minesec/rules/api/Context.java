package net.minesec.rules.api;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.openqa.selenium.WebDriver;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Copyright (c) 20-7-17, MineSec. All rights reserved.
 */
public interface Context {

    WebDriver getWebDriver();

    void queueTask(Consumer<WebDriver> task);

    HttpRequest getRequest();

    HttpResponse getResponse();

    void setRequest(HttpRequest httpRequest);

    void setResponse(HttpResponse httpResponse);

    // TODO: Could differentiate between events and attributes, instead of one generic "meta" name
    // TODO: Could mark a certain rule as completed, and allow others to listen for completion
    <T> Optional<T> getMeta(Class<T> type);

    <T> void setMeta(Class<? extends T> type, T instance);

    default <T> void setMeta(T instance) {
        this.setMeta(instance.getClass(), instance);
    }

    <T> void addMetaListener(Class<T> type, Consumer<T> consumer);

}