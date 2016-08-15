package cn.tomoya.android.md.ui.listener;

import android.webkit.JavascriptInterface;

import java.util.Date;

import cn.tomoya.android.md.util.FormatUtils;

public final class FormatJavascriptInterface {

    public static final FormatJavascriptInterface instance = new FormatJavascriptInterface();
    public static final String NAME = "formatBridge";

    private FormatJavascriptInterface() {}

    @JavascriptInterface
    public String getRelativeTimeSpanString(String time) {
        return FormatUtils.getRelativeTimeSpanString(new Date(time));
    }

}
