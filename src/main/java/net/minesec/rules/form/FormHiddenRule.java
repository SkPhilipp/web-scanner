package net.minesec.rules.form;

import net.minesec.rules.api.Context;
import net.minesec.rules.api.Rule;

/**
 * Copyright (c) 21-7-17, MineSec. All rights reserved.
 */
public class FormHiddenRule implements Rule {
    @Override
    public Moment moment() {
        return Moment.PAGE_LOAD;
    }

    @Override
    public void apply(Context ctx) {
        // TODO: Check for hidden fields and report them
    }
}