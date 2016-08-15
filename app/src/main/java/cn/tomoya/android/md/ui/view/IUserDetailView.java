package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import java.util.List;

import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.User;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetCollectTopicListOk(@NonNull List<Topic> topicList);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
