package net.minesec.rules.image;

import net.minesec.rules.api.Context;
import net.minesec.rules.api.Rule;

/**
 * Copyright (c) 21-7-17, MineSec. All rights reserved.
 */
public class ImageRule implements Rule {
    @Override
    public Moment moment() {
        return Moment.RESPONSE;
    }

    @Override
    public void apply(Context ctx) {
        // TODO: Image size limit checker
    }
}