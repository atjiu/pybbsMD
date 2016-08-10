package cn.tomoya.android.md.model.api;

public final class ApiDefine {

    private ApiDefine() {}

    public static final String HOST_BASE_URL = "https://bbs.tomoya.cn";
    public static final String API_BASE_URL = HOST_BASE_URL + "/api/";
    public static final String USER_PATH_PREFIX = "/user/";
    public static final String USER_LINK_URL_PREFIX = HOST_BASE_URL + USER_PATH_PREFIX;
    public static final String TOPIC_PATH_PREFIX = "/t/";
    public static final String TOPIC_LINK_URL_PREFIX = HOST_BASE_URL + TOPIC_PATH_PREFIX;

    public static final boolean MD_RENDER = true; // 使用服务端Markdown渲染可以轻微提升性能

}
