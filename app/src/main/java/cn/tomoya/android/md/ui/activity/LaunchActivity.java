package cn.tomoya.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.tomoya.android.md.R;
import cn.tomoya.android.md.ui.base.BaseActivity;
import cn.tomoya.android.md.ui.util.ActivityUtils;
import cn.tomoya.android.md.util.HandlerUtils;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        HandlerUtils.postDelayed(this, 2000);
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
