package cn.tomoya.android.md.ui.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import cn.tomoya.android.md.model.storage.SettingShared;

public final class AlertDialogUtils {

    private AlertDialogUtils() {}

    public static AlertDialog.Builder createBuilderWithAutoTheme(@NonNull Activity activity) {
        return new AlertDialog.Builder(activity, SettingShared.isEnableThemeDark(activity) ? cn.tomoya.android.md.R.style.AppDialogDark_Alert : cn.tomoya.android.md.R.style.AppDialogLight_Alert);
    }

}
