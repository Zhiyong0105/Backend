package org.springframe.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginEnum {
    EMAIL(0,"login by emain","email"),
    GITHUB(2,"login by github","github");


    private final Integer loginType;
    private final String description;
    private final String strategy;
}
