package org.springframe.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestHeaderEnum {
    GITHUB_USER_INFO("Accept","application/vnd.github.v3+json");
    private final String header;
    private final String content;
}
