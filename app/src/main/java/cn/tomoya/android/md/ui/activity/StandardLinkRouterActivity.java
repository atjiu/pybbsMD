package cn.tomoya.android.md.ui.activity;

import android.os.Bundle;

import cn.tomoya.android.md.R;
import cn.tomoya.android.md.ui.base.BaseActivity;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.ui.util.ToastUtils;

public class StandardLinkRouterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Navigator.openStandardLink(this, getIntent().getDataString())) {
            ToastUtils.with(this).show(R.string.invalid_link);
        }
        finish();
    }

}
