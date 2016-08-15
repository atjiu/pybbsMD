package cn.tomoya.android.md.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.model.storage.SettingShared;
import cn.tomoya.android.md.presenter.contract.IMainPresenter;
import cn.tomoya.android.md.presenter.implement.MainPresenter;
import cn.tomoya.android.md.ui.adapter.TopicListAdapter;
import cn.tomoya.android.md.ui.base.FullLayoutActivity;
import cn.tomoya.android.md.ui.dialog.AlertDialogUtils;
import cn.tomoya.android.md.ui.listener.DoubleClickBackToContentTopListener;
import cn.tomoya.android.md.ui.listener.NavigationOpenClickListener;
import cn.tomoya.android.md.ui.util.DisplayUtils;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.ui.util.RefreshUtils;
import cn.tomoya.android.md.ui.util.ThemeUtils;
import cn.tomoya.android.md.ui.util.ToastUtils;
import cn.tomoya.android.md.ui.view.IBackToContentTopView;
import cn.tomoya.android.md.ui.view.IMainView;
import cn.tomoya.android.md.ui.viewholder.LoadMoreFooter;
import cn.tomoya.android.md.ui.widget.ListView;
import cn.tomoya.android.md.util.FormatUtils;
import cn.tomoya.android.md.util.HandlerUtils;

public class MainActivity extends FullLayoutActivity implements IMainView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener, LoadMoreFooter.OnLoadMoreListener {

    // 抽屉导航布局
    @BindView(cn.tomoya.android.md.R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    // 状态栏
    @BindView(cn.tomoya.android.md.R.id.center_adapt_status_bar)
    protected View centerAdaptStatusBar;

    @BindView(cn.tomoya.android.md.R.id.nav_adapt_status_bar)
    protected View navAdaptStatusBar;

    // 导航部分的个人信息
    @BindView(cn.tomoya.android.md.R.id.img_avatar)
    protected ImageView imgAvatar;

    @BindView(cn.tomoya.android.md.R.id.tv_login_name)
    protected TextView tvLoginName;

    @BindView(cn.tomoya.android.md.R.id.badge_nav_notification)
    protected TextView tvBadgeNotification;

    @BindView(cn.tomoya.android.md.R.id.btn_logout)
    protected View btnLogout;

    @BindView(cn.tomoya.android.md.R.id.btn_theme_dark)
    protected ImageView imgThemeDark;

    @BindView(cn.tomoya.android.md.R.id.img_nav_top_background)
    protected ImageView imgTopBackground;

    // 主要导航项
    @BindViews({
            cn.tomoya.android.md.R.id.btn_nav_all,
            cn.tomoya.android.md.R.id.btn_nav_good,
            cn.tomoya.android.md.R.id.btn_nav_share,
            cn.tomoya.android.md.R.id.btn_nav_ask,
            cn.tomoya.android.md.R.id.btn_nav_pybbs
    })
    protected List<CheckedTextView> navMainItemList;

    // 内容部分
    @BindView(cn.tomoya.android.md.R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(cn.tomoya.android.md.R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(cn.tomoya.android.md.R.id.list_view)
    protected ListView listView;

    @BindView(cn.tomoya.android.md.R.id.icon_no_data)
    protected View iconNoData;

    @BindView(cn.tomoya.android.md.R.id.fab_create_topic)
    protected FloatingActionButton fabCreateTopic;

    private LoadMoreFooter loadMoreFooter;
    private TopicListAdapter adapter;

    private IMainPresenter mainPresenter;

    // 当前分页位置
    private int page = 0;

    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    // 是否启用夜间模式
    private boolean enableThemeDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableThemeDark = ThemeUtils.configThemeBeforeOnCreate(this, cn.tomoya.android.md.R.style.AppThemeLight_FitsStatusBar, cn.tomoya.android.md.R.style.AppThemeDark_FitsStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(cn.tomoya.android.md.R.layout.activity_main);
        ButterKnife.bind(this);

        DisplayUtils.adaptStatusBar(this, centerAdaptStatusBar);
        DisplayUtils.adaptStatusBar(this, navAdaptStatusBar);

        drawerLayout.setDrawerShadow(cn.tomoya.android.md.R.drawable.navigation_drawer_shadow, GravityCompat.START);
        drawerLayout.addDrawerListener(drawerListener);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        loadMoreFooter = new LoadMoreFooter(this, listView, this);
        adapter = new TopicListAdapter(this);
        listView.setAdapter(adapter);
        fabCreateTopic.attachToListView(listView);

        mainPresenter = new MainPresenter(this, this);

        updateUserInfoViews();

        imgThemeDark.setImageResource(enableThemeDark ? cn.tomoya.android.md.R.drawable.ic_wb_sunny_white_24dp : cn.tomoya.android.md.R.drawable.ic_brightness_3_white_24dp);
        imgTopBackground.setVisibility(enableThemeDark ? View.INVISIBLE : View.VISIBLE);

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.getMessageCountAsyncTask();
        // 判断是否需要切换主题
        if (SettingShared.isEnableThemeDark(this) != enableThemeDark) {
            ThemeUtils.notifyThemeApply(this, true);
        }
    }

    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {

        @Override
        public void onDrawerOpened(View drawerView) {
            updateUserInfoViews();
            mainPresenter.getUserAsyncTask();
            mainPresenter.getMessageCountAsyncTask();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            TabType tab = TabType.all;
            for (CheckedTextView navItem : navMainItemList) {
                if (navItem.isChecked()) {
                    switch (navItem.getId()) {
                        case cn.tomoya.android.md.R.id.btn_nav_all:
                            tab = TabType.all;
                            break;
                        case cn.tomoya.android.md.R.id.btn_nav_good:
                            tab = TabType.good;
                            break;
                        case cn.tomoya.android.md.R.id.btn_nav_share:
                            tab = TabType.share;
                            break;
                        case cn.tomoya.android.md.R.id.btn_nav_ask:
                            tab = TabType.ask;
                            break;
                        case cn.tomoya.android.md.R.id.btn_nav_pybbs:
                            tab = TabType.pybbs;
                            break;
                        default:
                            throw new AssertionError("Unknow tab.");
                    }
                    break;
                }
            }
            mainPresenter.switchTab(tab);
        }

    };

    @Override
    public void onRefresh() {
        mainPresenter.refreshTopicListAsyncTask();
    }

    @Override
    public void onLoadMore() {
        mainPresenter.loadMoreTopicListAsyncTask(page + 1);
    }

    /**
     * 主导航项单击事件
     */
    @OnClick({
            cn.tomoya.android.md.R.id.btn_nav_all,
            cn.tomoya.android.md.R.id.btn_nav_good,
            cn.tomoya.android.md.R.id.btn_nav_share,
            cn.tomoya.android.md.R.id.btn_nav_ask,
            cn.tomoya.android.md.R.id.btn_nav_pybbs
    })
    public void onNavigationMainItemClick(CheckedTextView itemView) {
        for (CheckedTextView navItem : navMainItemList) {
            navItem.setChecked(navItem.getId() == itemView.getId());
        }
        drawerLayout.closeDrawers();
    }

    /**
     * 次要菜单导航
     */

    @OnClick({
            cn.tomoya.android.md.R.id.btn_nav_notification,
            cn.tomoya.android.md.R.id.btn_nav_setting,
            cn.tomoya.android.md.R.id.btn_nav_about
    })
    public void onNavigationItemOtherClick(View itemView) {
        switch (itemView.getId()) {
            case cn.tomoya.android.md.R.id.btn_nav_notification:
                if (LoginActivity.startForResultWithLoginCheck(this)) {
                    notificationAction.startDelayed();
                    drawerLayout.closeDrawers();
                }
                break;
            case cn.tomoya.android.md.R.id.btn_nav_setting:
                settingAction.startDelayed();
                drawerLayout.closeDrawers();
                break;
            case cn.tomoya.android.md.R.id.btn_nav_about:
                aboutAction.startDelayed();
                drawerLayout.closeDrawers();
                break;
        }
    }

    private class OtherItemAction implements Runnable {

        private Class gotoClz;

        protected OtherItemAction(Class gotoClz) {
            this.gotoClz = gotoClz;
        }

        @Override
        public void run() {
            if (gotoClz == NotificationActivity.class) {
                Navigator.NotificationWithAutoCompat.start(MainActivity.this);
            } else {
                startActivity(new Intent(MainActivity.this, gotoClz));
            }
        }

        public void startDelayed() {
            HandlerUtils.postDelayed(this, 400);
        }

    }

    private OtherItemAction notificationAction = new OtherItemAction(NotificationActivity.class);
    private OtherItemAction settingAction = new OtherItemAction(SettingActivity.class);
    private OtherItemAction aboutAction = new OtherItemAction(AboutActivity.class);

    /**
     * 注销按钮
     */
    @OnClick(cn.tomoya.android.md.R.id.btn_logout)
    protected void onBtnLogoutClick() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(cn.tomoya.android.md.R.string.logout_tip)
                .setPositiveButton(cn.tomoya.android.md.R.string.logout, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginShared.logout(MainActivity.this);
                        tvBadgeNotification.setText(null); // 未读消息清空
                        updateUserInfoViews();
                    }

                })
                .setNegativeButton(cn.tomoya.android.md.R.string.cancel, null)
                .show();
    }

    /**
     * 主题按钮
     */
    @OnClick(cn.tomoya.android.md.R.id.btn_theme_dark)
    protected void onBtnThemeDarkClick() {
        SettingShared.setEnableThemeDark(this, !enableThemeDark);
        ThemeUtils.notifyThemeApply(this, false);
    }

    /**
     * 用户信息按钮
     */
    @OnClick(cn.tomoya.android.md.R.id.layout_info)
    protected void onBtnInfoClick() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            LoginActivity.startForResult(this);
        } else {
            UserDetailActivity.startWithTransitionAnimation(this, LoginShared.getLoginName(this), imgAvatar, LoginShared.getAvatarUrl(this));
        }
    }

    /**
     * 发帖按钮
     */
    @OnClick(cn.tomoya.android.md.R.id.fab_create_topic)
    protected void onBtnCreateTopicClick() {
        if (LoginActivity.startForResultWithLoginCheck(this)) {
            startActivity(new Intent(this, CreateTopicActivity.class));
        }
    }

    /**
     * 判断登录是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            updateUserInfoViews();
            mainPresenter.getUserAsyncTask();
        }
    }

    /**
     * 返回键关闭导航
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long secondBackPressedTime = System.currentTimeMillis();
            if (secondBackPressedTime - firstBackPressedTime > 2000) {
                ToastUtils.with(this).show(cn.tomoya.android.md.R.string.press_back_again_to_exit);
                firstBackPressedTime = secondBackPressedTime;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onSwitchTabOk(@NonNull TabType tab) {
        page = 0;
        toolbar.setTitle(tab.getNameId());
        adapter.getTopicList().clear();
        adapter.notifyDataSetChanged();
        loadMoreFooter.setState(LoadMoreFooter.State.disable);
        iconNoData.setVisibility(View.VISIBLE);
        fabCreateTopic.show(true);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefreshTopicListOk(@NonNull List<Topic> topicList) {
        adapter.getTopicList().clear();
        adapter.getTopicList().addAll(topicList);
        adapter.notifyDataSetChanged();
        if (adapter.getTopicList().size() > 0) {
            loadMoreFooter.setState(LoadMoreFooter.State.endless);
            iconNoData.setVisibility(View.GONE);
        } else {
            loadMoreFooter.setState(LoadMoreFooter.State.disable);
            iconNoData.setVisibility(View.VISIBLE);
        }
        page = 1;
    }

    @Override
    public void onRefreshTopicListFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreTopicListOk(@NonNull List<Topic> topicList) {
        adapter.getTopicList().addAll(topicList);
        adapter.notifyDataSetChanged();
        page++;
    }

    @Override
    public void onLoadMoreTopicListFinish(@NonNull LoadMoreFooter.State state) {
        loadMoreFooter.setState(state);
    }

    @Override
    public void updateUserInfoViews() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            Glide.with(this).load(cn.tomoya.android.md.R.drawable.image_placeholder).placeholder(cn.tomoya.android.md.R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(cn.tomoya.android.md.R.string.click_avatar_to_login);
            btnLogout.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(LoginShared.getAvatarUrl(this)).placeholder(cn.tomoya.android.md.R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(LoginShared.getLoginName(this));
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateMessageCountViews(int count) {
        tvBadgeNotification.setText(FormatUtils.getNavigationDisplayCountString(count));
    }

    @Override
    public void backToContentTop() {
        listView.setSelection(0);
    }

}
