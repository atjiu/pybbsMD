package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.ui.viewholder.LoadMoreFooter;

import java.util.List;

public interface IMainView {

    void onSwitchTabOk(@NonNull TabType tab);

    void onRefreshTopicListOk(@NonNull List<Topic> topicList);

    void onRefreshTopicListFinish();

    void onLoadMoreTopicListOk(@NonNull List<Topic> topicList);

    void onLoadMoreTopicListFinish(@NonNull LoadMoreFooter.State state);

    void updateUserInfoViews();

    void updateMessageCountViews(int count);

}
