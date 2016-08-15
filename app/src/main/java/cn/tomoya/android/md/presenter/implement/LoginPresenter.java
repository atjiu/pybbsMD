package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.entity.Author;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.presenter.contract.ILoginPresenter;
import cn.tomoya.android.md.ui.view.ILoginView;
import cn.tomoya.android.md.util.FormatUtils;
import retrofit2.Call;
import retrofit2.Response;

public class LoginPresenter implements ILoginPresenter {

    private final Activity activity;
    private final ILoginView loginView;

    public LoginPresenter(@NonNull Activity activity, @NonNull ILoginView loginView) {
        this.activity = activity;
        this.loginView = loginView;
    }

    @Override
    public void loginAsyncTask(final String accessToken) {
        if (!FormatUtils.isAccessToken(accessToken)) {
            loginView.onAccessTokenError(activity.getString(R.string.access_token_format_error));
        } else {
            Call<Result<Author>> call = ApiClient.service.accessToken(accessToken);
            loginView.onLoginStart(call);
            call.enqueue(new DefaultCallback<Result<Author>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Author>> response, Result<Author> loginInfo) {
                    loginView.onLoginOk(accessToken, loginInfo);
                    return false;
                }

                @Override
                public boolean onResultAuthError(Response<Result<Author>> response, Result.Error error) {
                    loginView.onAccessTokenError(getActivity().getString(R.string.access_token_auth_error));
                    return false;
                }

                @Override
                public void onFinish() {
                    loginView.onLoginFinish();
                }

            });
        }
    }

}
