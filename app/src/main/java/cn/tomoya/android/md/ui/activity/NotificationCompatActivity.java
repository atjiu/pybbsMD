package cn.tomoya.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.entity.Notification;
import cn.tomoya.android.md.presenter.contract.INotificationPresenter;
import cn.tomoya.android.md.presenter.implement.NotificationPresenter;
import cn.tomoya.android.md.ui.base.StatusBarActivity;
import cn.tomoya.android.md.ui.listener.DoubleClickBackToContentTopListener;
import cn.tomoya.android.md.ui.listener.NavigationFinishClickListener;
import cn.tomoya.android.md.ui.util.RefreshUtils;
import cn.tomoya.android.md.ui.util.ThemeUtils;
import cn.tomoya.android.md.ui.view.INotificationView;
import cn.tomoya.android.md.ui.widget.NotificationWebView;

public class NotificationCompatActivity extends StatusBarActivity implements INotificationView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_notification)
    protected NotificationWebView webNotification;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_compat);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webNotification));

        notificationPresenter = new NotificationPresenter(this, this);

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    public void onRefresh() {
        notificationPresenter.getMessagesAsyncTask();
    }

    @Override
    public void onGetMessagesOk(@NonNull List<Notification> notifications) {
        List<Notification> messageList = new ArrayList<>();
        messageList.addAll(notifications);
        webNotification.updateMessageList(messageList);
        iconNoData.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        webNotification.markAllMessageRead();
    }

}
