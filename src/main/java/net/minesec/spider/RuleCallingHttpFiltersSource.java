package net.minesec.spider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Setter;
import net.minesec.rules.api.Context;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;

import java.util.function.Supplier;

/**
 * Copyright (c) 11-7-17, MineSec. All rights reserved.
 * <p>
 * Implements 100 MB per-request buffers
 */
@Setter
public class RuleCallingHttpFiltersSource implements HttpFiltersSource {

    private static final int MAXIMUM_BUFFER_SIZE_IN_BYTES_REQUEST = 1024 * 1024 * 100;
    private static final int MAXIMUM_BUFFER_SIZE_IN_BYTES_RESPONSE = 1024 * 1024 * 100;
    private Supplier<Context> contextSupplier;

    @Override
    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        if (contextSupplier == null) {
            return null;
        }
        return new RuleCallingHttpFilter(originalRequest, ctx, contextSupplier.get());
    }

    @Override
    public int getMaximumRequestBufferSizeInBytes() {
        return MAXIMUM_BUFFER_SIZE_IN_BYTES_REQUEST;
    }

    @Override
    public int getMaximumResponseBufferSizeInBytes() {
        return MAXIMUM_BUFFER_SIZE_IN_BYTES_RESPONSE;
    }

}
