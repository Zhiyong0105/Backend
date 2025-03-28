package org.springframe.backend.constants;

public class SecurityConst {
    public static final String AUTH_CHECK = "user/auth/**";

    public static final String LOGIN = "/user/login";

    public static final String LOGOUT = "/user/logout";

    public static final String GITHUB_LOGIN = "/user/github/login";

    public static final String COMMENT_CHECK = "/comment/auth/**";

    public static final String ARTICLE_CHECK = "/article/auth/**";

    public static final String ADMIN_CHECK = "/admin/auth/**";

    public static final String ROLE_CHECK = "/role/**";

    public static final String PERMISSION_CHECK = "/permission/**";

    public static final String[] AUTH_CHECK_ARRAY = { AUTH_CHECK, COMMENT_CHECK,ARTICLE_CHECK, ADMIN_CHECK,ROLE_CHECK,PERMISSION_CHECK };
}
