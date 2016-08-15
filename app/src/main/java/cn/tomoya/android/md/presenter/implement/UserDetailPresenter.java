package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.List;

import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.model.api.DefaultCallback;
import cn.tomoya.android.md.model.api.ForegroundCallback;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.User;
import cn.tomoya.android.md.presenter.contract.IUserDetailPresenter;
import cn.tomoya.android.md.ui.util.ActivityUtils;
import cn.tomoya.android.md.ui.view.IUserDetailView;
import cn.tomoya.android.md.util.HandlerUtils;
import retrofit2.Response;

public class UserDetailPresenter implements IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;

    private boolean loading = false;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        if (!loading) {
            loading = true;
            userDetailView.onGetUserStart();
            ApiClient.service.getUser(loginName, ApiDefine.MD_RENDER).enqueue(new ForegroundCallback<Result<User>>(activity) {

                private long startLoadingTime = System.currentTimeMillis();

                private long getPostTime() {
                    long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                    if (postTime > 0) {
                        return postTime;
                    } else {
                        return 0;
                    }
                }

                @Override
                public boolean onResultOk(final Response<Result<User>> response, final Result<User> result) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserOk(result.getDetail());
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onResultError(final Response<Result<User>> response, final Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserError(response.code() == 404 ? error.getErrorMessage() : getActivity().getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(getActivity())) {
                                userDetailView.onGetUserError(getActivity().getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public void onFinish() {
                    userDetailView.onGetUserFinish();
                    loading = false;
                }

            });
            ApiClient.service.getCollectTopicList(loginName).enqueue(new DefaultCallback<Result<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<List<Topic>>> response, Result<List<Topic>> result) {
                    userDetailView.onGetCollectTopicListOk(result.getDetail());
                    return false;
                }

            });
        }
    }

}
