package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.entity.TopicWithReply;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.presenter.contract.ITopicPresenter;
import cn.tomoya.android.md.ui.view.ITopicView;
import retrofit2.Response;

public class TopicPresenter implements ITopicPresenter {

    private final Activity activity;
    private final ITopicView topicView;

    public TopicPresenter(@NonNull Activity activity, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.topicView = topicView;
    }

    @Override
    public void getTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new DefaultCallback<Result<TopicWithReply>>(activity) {

            @Override
            public boolean onResultOk(Response<Result<TopicWithReply>> response, Result<TopicWithReply> result) {
                topicView.onGetTopicOk(result.getDetail());
                return false;
            }

            @Override
            public void onFinish() {
                topicView.onGetTopicFinish();
            }

        });
    }

}
