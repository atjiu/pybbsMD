package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Notification;
import cn.tomoya.android.md.model.entity.Page;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.presenter.contract.INotificationPresenter;
import cn.tomoya.android.md.ui.view.INotificationView;
import retrofit2.Response;

public class NotificationPresenter implements INotificationPresenter {

    private final Activity activity;
    private final INotificationView notificationView;

    public NotificationPresenter(@NonNull Activity activity, @NonNull INotificationView notificationView) {
        this.activity = activity;
        this.notificationView = notificationView;
    }

    @Override
    public void getMessagesAsyncTask() {
        ApiClient.service.getMessages(LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new DefaultCallback<Result<Page<Notification>>>(activity) {

            @Override
            public boolean onResultOk(Response<Result<Page<Notification>>> response, Result<Page<Notification>> result) {
                notificationView.onGetMessagesOk(result.getDetail().getList());
                return false;
            }

            @Override
            public void onFinish() {
                notificationView.onGetMessagesFinish();
            }

        });
    }

}
