package cn.tomoya.android.md.ui.view;

import android.support.annotation.NonNull;

import cn.tomoya.android.md.model.entity.Author;
import cn.tomoya.android.md.model.entity.Result;
import retrofit2.Call;

public interface ILoginView {

    void onAccessTokenError(@NonNull String message);

    void onLoginOk(@NonNull String accessToken, @NonNull Result<Author> loginInfo);

    void onLoginStart(@NonNull Call<Result<Author>> call);

    void onLoginFinish();

}
