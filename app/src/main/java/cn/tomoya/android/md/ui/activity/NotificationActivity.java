package cn.tomoya.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tomoya.android.md.model.entity.Notification;
import cn.tomoya.android.md.presenter.contract.INotificationPresenter;
import cn.tomoya.android.md.presenter.implement.NotificationPresenter;
import cn.tomoya.android.md.ui.adapter.MessageListAdapter;
import cn.tomoya.android.md.ui.base.StatusBarActivity;
import cn.tomoya.android.md.ui.listener.DoubleClickBackToContentTopListener;
import cn.tomoya.android.md.ui.listener.NavigationFinishClickListener;
import cn.tomoya.android.md.ui.util.RefreshUtils;
import cn.tomoya.android.md.ui.util.ThemeUtils;
import cn.tomoya.android.md.ui.view.IBackToContentTopView;
import cn.tomoya.android.md.ui.view.INotificationView;

public class NotificationActivity extends StatusBarActivity implements INotificationView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(cn.tomoya.android.md.R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(cn.tomoya.android.md.R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(cn.tomoya.android.md.R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(cn.tomoya.android.md.R.id.icon_no_data)
    protected View iconNoData;

    private MessageListAdapter adapter;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, cn.tomoya.android.md.R.style.AppThemeLight, cn.tomoya.android.md.R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(cn.tomoya.android.md.R.layout.activity_notification);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageListAdapter(this);
        recyclerView.setAdapter(adapter);

        notificationPresenter = new NotificationPresenter(this, this);

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    public void onRefresh() {
        notificationPresenter.getMessagesAsyncTask();
    }

    @Override
    public void onGetMessagesOk(@NonNull List<Notification> notification) {
        adapter.getMessageList().clear();
        adapter.getMessageList().addAll(notification);
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(adapter.getMessageList().size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        adapter.markAllMessageRead();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
    }

}
