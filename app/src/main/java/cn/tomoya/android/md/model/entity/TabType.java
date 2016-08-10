package cn.tomoya.android.md.model.entity;

import android.support.annotation.StringRes;

import cn.tomoya.android.md.R;

public enum TabType {

    all(R.string.tab_all),

    good(R.string.tab_good),

    share(R.string.tab_share),

    ask(R.string.tab_ask),

    pybbs(R.string.tab_pybbs),

    news(R.string.tab_news);

    @StringRes
    private int nameId;

    TabType(@StringRes int nameId) {
        this.nameId = nameId;
    }

    @StringRes
    public int getNameId() {
        return nameId;
    }

}
