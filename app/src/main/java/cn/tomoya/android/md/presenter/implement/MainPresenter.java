package cn.tomoya.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.tomoya.android.md.model.api.ApiClient;
import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.model.api.ForegroundCallback;
import cn.tomoya.android.md.model.entity.Page;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.User;
import cn.tomoya.android.md.model.storage.LoginShared;
import cn.tomoya.android.md.presenter.contract.IMainPresenter;
import cn.tomoya.android.md.ui.util.ToastUtils;
import cn.tomoya.android.md.ui.view.IMainView;
import cn.tomoya.android.md.ui.viewholder.LoadMoreFooter;
import retrofit2.Call;
import retrofit2.Response;

public class MainPresenter implements IMainPresenter {

    private static final int PAGE_LIMIT = 20;

    private final Activity activity;
    private final IMainView mainView;

    private TabType tab = TabType.all;
    private Call<Result<Page<Topic>>> refreshCall = null;
    private Call<Result<Page<Topic>>> loadMoreCall = null;

    public MainPresenter(@NonNull Activity activity, @NonNull IMainView mainView) {
        this.activity = activity;
        this.mainView = mainView;
    }

    private void cancelRefreshCall() {
        if (refreshCall != null) {
            if (!refreshCall.isCanceled()) {
                refreshCall.cancel();
            }
            refreshCall = null;
        }
    }

    private void cancelLoadMoreCall() {
        if (loadMoreCall != null) {
            if (!loadMoreCall.isCanceled()) {
                loadMoreCall.cancel();
            }
            loadMoreCall = null;
        }
    }

    @Override
    public void switchTab(@NonNull TabType tab) {
        if (this.tab != tab) {
            this.tab = tab;
            cancelRefreshCall();
            cancelLoadMoreCall();
            mainView.onSwitchTabOk(tab);
        }
    }

    @Override
    public void refreshTopicListAsyncTask() {
        if (refreshCall == null) {
            refreshCall = ApiClient.service.getTopicList(tab, 1);
            refreshCall.enqueue(new ForegroundCallback<Result<Page<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Page<Topic>>> response, Result<Page<Topic>> result) {
                    cancelLoadMoreCall();
                    mainView.onRefreshTopicListOk(result.getDetail().getList());
                    return false;
                }

                @Override
                public boolean onResultError(Response<Result<Page<Topic>>> response, Result.Error error) {
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallCancel() {
                    return true;
                }

                @Override
                public void onFinish() {
                    refreshCall = null;
                    mainView.onRefreshTopicListFinish();
                }

            });
        }
    }

    @Override
    public void loadMoreTopicListAsyncTask(int page) {
        if (loadMoreCall == null) {
            loadMoreCall = ApiClient.service.getTopicList(tab, page);
            loadMoreCall.enqueue(new ForegroundCallback<Result<Page<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Page<Topic>>> response, Result<Page<Topic>> result) {
                    if (result.getDetail().getList().size() > 0) {
                        mainView.onLoadMoreTopicListOk(result.getDetail().getList());
                        mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.endless);
                    } else {
                        mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.nomore);
                    }
                    return false;
                }

                @Override
                public boolean onResultError(Response<Result<Page<Topic>>> response, Result.Error error) {
                    mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.fail);
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.fail);
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallCancel() {
                    return true;
                }

                @Override
                public void onFinish() {
                    loadMoreCall = null;
                }

            });
        }
    }

    @Override
    public void getUserAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(activity);
        if (!TextUtils.isEmpty(accessToken)) {
            ApiClient.service.getUser(LoginShared.getLoginName(activity), ApiDefine.MD_RENDER).enqueue(new ForegroundCallback<Result<User>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<User>> response, Result<User> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
                        LoginShared.update(getActivity(), result.getDetail());
                        mainView.updateUserInfoViews();
                    }
                    return false;
                }

            });
        }
    }

    @Override
    public void getMessageCountAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(activity);
        if (!TextUtils.isEmpty(accessToken)) {
            ApiClient.service.getMessageCount(accessToken).enqueue(new ForegroundCallback<Result<Integer>>(activity) {

                @Override
                public boolean onResultOk(Response<Result<Integer>> response, Result<Integer> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
                        mainView.updateMessageCountViews(result.getDetail());
                    }
                    return false;
                }

            });
        }
    }

}
