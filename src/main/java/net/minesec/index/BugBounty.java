package net.minesec.index;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 30/06/2017, MineSec. All rights reserved.
 */
@Data
@Builder
class BugBounty {
    private String source;
    private String page;
    private boolean rewardMonetary;
    private boolean rewardAcknowledgement;
    private boolean rewardOther;
}
