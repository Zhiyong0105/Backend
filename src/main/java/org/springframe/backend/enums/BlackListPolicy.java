package org.springframe.backend.enums;

import lombok.Getter;

@Getter
public enum BlackListPolicy {
    DDOS_ATTACK_10_YEARS(200,10,"DDOS","DDOS"),
    DDOS_ATTACK_1_MONTH(200,10,"DDOS","DDOS");
    private final int requestThreshold;
    private final int duration;
    private final String reason;
    private final String message;

     BlackListPolicy(int requestThreshold, int duration, String reason, String message) {
        this.requestThreshold = requestThreshold;
        this.duration = duration;
        this.reason = reason;
        this.message = message;

    }

}
