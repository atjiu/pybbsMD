package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import java.util.List;

import cn.tomoya.android.md.model.entity.Notification;

public interface INotificationView {

    void onGetMessagesOk(@NonNull List<Notification> notifications);

    void onGetMessagesFinish();

    void onMarkAllMessageReadOk();

}
