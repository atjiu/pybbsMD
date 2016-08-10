package cn.tomoya.android.md.presenter.contract;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.Reply;

public interface IReplyPresenter {

    void upReplyAsyncTask(@NonNull Reply reply);

}
