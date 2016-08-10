package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.Reply;
import cn.tomoya.android.md.model.entity.TopicWithReply;

public interface ITopicView {

    void onGetTopicOk(@NonNull TopicWithReply topic);

    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
