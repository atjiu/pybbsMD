package cn.tomoya.android.md.presenter.contract;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.TabType;

public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull TabType tab, String title, String content);

}
