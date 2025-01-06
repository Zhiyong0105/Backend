package org.springframe.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UrlEnum {
    GITHUB_USER_INFO("https://api.github.com/user","GET","Get github user info");
    private final String url;
    private final String method;
    private final String desc;
}
