package cn.tomoya.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.model.entity.Reply;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.TopicWithReply;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.presenter.contract.IReplyPresenter;
import cn.tomoya.android.md.presenter.contract.ITopicHeaderPresenter;
import cn.tomoya.android.md.presenter.contract.ITopicPresenter;
import cn.tomoya.android.md.presenter.implement.ReplyPresenter;
import cn.tomoya.android.md.presenter.implement.TopicHeaderPresenter;
import cn.tomoya.android.md.presenter.implement.TopicPresenter;
import cn.tomoya.android.md.ui.base.StatusBarActivity;
import cn.tomoya.android.md.ui.dialog.CreateReplyDialog;
import cn.tomoya.android.md.ui.listener.DoubleClickBackToContentTopListener;
import cn.tomoya.android.md.ui.listener.NavigationFinishClickListener;
import cn.tomoya.android.md.ui.listener.TopicJavascriptInterface;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.ui.util.RefreshUtils;
import cn.tomoya.android.md.ui.util.ThemeUtils;
import cn.tomoya.android.md.ui.view.ICreateReplyView;
import cn.tomoya.android.md.ui.view.IReplyView;
import cn.tomoya.android.md.ui.view.ITopicHeaderView;
import cn.tomoya.android.md.ui.view.ITopicView;
import cn.tomoya.android.md.ui.widget.TopicWebView;

public class TopicCompatActivity extends StatusBarActivity implements ITopicView, ITopicHeaderView, IReplyView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_topic)
    protected TopicWebView webTopic;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    @BindView(R.id.fab_reply)
    protected FloatingActionButton fabReply;

    private String topicId;
    private Topic topic;

    private ICreateReplyView createReplyView;

    private ITopicPresenter topicPresenter;
    private ITopicHeaderPresenter topicHeaderPresenter;
    private IReplyPresenter replyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_compat);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC_ID);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webTopic));

        topicPresenter = new TopicPresenter(this, this);
        topicHeaderPresenter = new TopicHeaderPresenter(this, this);
        replyPresenter = new ReplyPresenter(this, this);

        createReplyView = CreateReplyDialog.createWithAutoTheme(this, topicId, this);

        webTopic.setFabReply(fabReply);
        webTopic.setBridgeAndLoadPage(new TopicJavascriptInterface(this, createReplyView, topicHeaderPresenter, replyPresenter));

        RefreshUtils.init(refreshLayout, this);
        RefreshUtils.refresh(refreshLayout, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (topic != null) {
                    Navigator.openShare(this, "《" + topic.getTitle() + "》\n" + ApiDefine.TOPIC_LINK_URL_PREFIX + topicId + "\n—— 来自朋也社区");
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        topicPresenter.getTopicAsyncTask(topicId);
    }

    @OnClick(R.id.fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null && LoginActivity.startForResultWithLoginCheck(this)) {
            createReplyView.showWindow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            refreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Override
    public void onGetTopicOk(@NonNull TopicWithReply topic) {
        this.topic = topic.getTopic();
        webTopic.updateTopicAndUserId(topic, LoginShared.getId(this));
        iconNoData.setVisibility(View.GONE);
    }

    @Override
    public void onGetTopicFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void appendReplyAndUpdateViews(@NonNull Reply reply) {
        webTopic.appendReply(reply);
    }

    @Override
    public void onCollectTopicOk() {
        webTopic.updateTopicCollect(true);
    }

    @Override
    public void onDecollectTopicOk() {
        webTopic.updateTopicCollect(false);
    }

    @Override
    public void onUpReplyOk(@NonNull Reply reply) {
        webTopic.updateReply(reply);
    }

}
