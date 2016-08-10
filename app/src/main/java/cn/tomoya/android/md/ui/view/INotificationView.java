package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.Notification;

import java.util.List;

public interface INotificationView {

    void onGetMessagesOk(@NonNull List<Notification> notifications);

    void onGetMessagesFinish();

    void onMarkAllMessageReadOk();

}
