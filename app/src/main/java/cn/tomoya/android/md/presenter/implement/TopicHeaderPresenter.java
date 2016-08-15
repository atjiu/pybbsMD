package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.presenter.contract.ITopicHeaderPresenter;
import cn.tomoya.android.md.ui.view.ITopicHeaderView;
import retrofit2.Response;

public class TopicHeaderPresenter implements ITopicHeaderPresenter {

    private final Activity activity;
    private final ITopicHeaderView topicHeaderView;

    public TopicHeaderPresenter(@NonNull Activity activity, @NonNull ITopicHeaderView topicHeaderView) {
        this.activity = activity;
        this.topicHeaderView = topicHeaderView;
    }

    @Override
    public void collectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new DefaultCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                topicHeaderView.onCollectTopicOk();
                return false;
            }

        });
    }

    @Override
    public void decollectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new DefaultCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                topicHeaderView.onDecollectTopicOk();
                return false;
            }

        });
    }

}
