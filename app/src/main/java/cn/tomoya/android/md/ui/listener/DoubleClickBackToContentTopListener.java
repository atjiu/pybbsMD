package cn.tomoya.android.md.ui.listener;

import android.support.annotation.NonNull;
import android.view.View;

import cn.tomoya.android.md.ui.view.IBackToContentTopView;

public class DoubleClickBackToContentTopListener extends OnDoubleClickListener {

    private final IBackToContentTopView backToContentTopView;

    public DoubleClickBackToContentTopListener(@NonNull IBackToContentTopView backToContentTopView) {
        super(300);
        this.backToContentTopView = backToContentTopView;
    }

    @Override
    public void onDoubleClick(View v) {
        backToContentTopView.backToContentTop();
    }

}
