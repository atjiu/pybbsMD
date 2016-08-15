package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.model.storage.SettingShared;
import cn.tomoya.android.md.presenter.contract.ICreateTopicPresenter;
import cn.tomoya.android.md.ui.view.ICreateTopicView;
import retrofit2.Response;

public class CreateTopicPresenter implements ICreateTopicPresenter {

    private final Activity activity;
    private final ICreateTopicView createTopicView;

    public CreateTopicPresenter(@NonNull Activity activity, @NonNull ICreateTopicView createTopicView) {
        this.activity = activity;
        this.createTopicView = createTopicView;
    }

    @Override
    public void createTopicAsyncTask(@NonNull TabType tab, String title, String content) {
        if (TextUtils.isEmpty(title) || title.length() > 120) {
            createTopicView.onTitleError(activity.getString(cn.tomoya.android.md.R.string.title_empty_error_tip));
        } else {
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            createTopicView.onCreateTopicStart();
            ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content).enqueue(new DefaultCallback<Result<Topic>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Topic>> response, Result<Topic> result) {
                    createTopicView.onCreateTopicOk(String.valueOf(result.getDetail()));
                    return false;
                }

                @Override
                public void onFinish() {
                    createTopicView.onCreateTopicFinish();
                }

            });
        }
    }

}
