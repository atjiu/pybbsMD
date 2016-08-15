package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Date;

import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Author;
import cn.tomoya.android.md.model.entity.Reply;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.model.storage.SettingShared;
import cn.tomoya.android.md.presenter.contract.ICreateReplyPresenter;
import cn.tomoya.android.md.ui.view.ICreateReplyView;
import retrofit2.Response;

public class CreateReplyPresenter implements ICreateReplyPresenter {

    private final Activity activity;
    private final ICreateReplyView createReplyView;

    public CreateReplyPresenter(@NonNull Activity activity, @NonNull ICreateReplyView createReplyView) {
        this.activity = activity;
        this.createReplyView = createReplyView;
    }

    @Override
    public void createReplyAsyncTask(@NonNull String topicId, String content, final String targetId) {
        if (TextUtils.isEmpty(content)) {
            createReplyView.onContentError(activity.getString(R.string.content_empty_error_tip));
        } else {
            final String finalContent;
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                finalContent = content + "\n\n" + SettingShared.getTopicSignContent(activity);
            } else {
                finalContent = content;
            }
            createReplyView.onReplyTopicStart();
            ApiClient.service.createReply(topicId, LoginShared.getAccessToken(activity), finalContent).enqueue(new DefaultCallback<Result<Reply>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Reply>> response, Result<Reply> result) {
                    Reply reply = new Reply();
                    reply.setId(result.getDetail().getId());
                    Author author = new Author();
                    author.setNickName(LoginShared.getLoginName(getActivity()));
                    author.setAvatar(LoginShared.getAvatarUrl(getActivity()));
                    reply.setAuthor(author.getNickName());
                    reply.setContentFromLocal(finalContent); // 这里要使用本地的访问器
                    reply.setInTime(new Date());
                    createReplyView.onReplyTopicOk(reply);
                    return false;
                }

                @Override
                public void onFinish() {
                    createReplyView.onReplyTopicFinish();
                }

            });
        }
    }

}
