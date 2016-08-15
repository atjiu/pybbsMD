package cn.tomoya.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.storage.SettingShared;
import cn.tomoya.android.md.model.storage.TopicShared;
import cn.tomoya.android.md.presenter.contract.ICreateTopicPresenter;
import cn.tomoya.android.md.presenter.implement.CreateTopicPresenter;
import cn.tomoya.android.md.ui.base.StatusBarActivity;
import cn.tomoya.android.md.ui.dialog.ProgressDialog;
import cn.tomoya.android.md.ui.listener.NavigationFinishClickListener;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.ui.util.ThemeUtils;
import cn.tomoya.android.md.ui.util.ToastUtils;
import cn.tomoya.android.md.ui.view.ICreateTopicView;
import cn.tomoya.android.md.ui.widget.EditorBarHandler;

public class CreateTopicActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener, ICreateTopicView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.spn_tab)
    protected Spinner spnTab;

    @BindView(R.id.edt_title)
    protected EditText edtTitle;

    @BindView(R.id.layout_editor_bar)
    protected ViewGroup editorBar;

    @BindView(R.id.edt_content)
    protected EditText edtContent;

    private ProgressDialog progressDialog;

    private ICreateTopicPresenter createTopicPresenter;

    private boolean saveTopicDraft = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.create_topic);
        toolbar.setOnMenuItemClickListener(this);

        progressDialog = ProgressDialog.createWithAutoTheme(this);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        // 创建EditorBar
        new EditorBarHandler(this, editorBar, edtContent);

        // 载入草稿
        if (SettingShared.isEnableTopicDraft(this)) {
            spnTab.setSelection(TopicShared.getDraftTabPosition(this));
            edtContent.setText(TopicShared.getDraftContent(this));
            edtContent.setSelection(edtContent.length());
            edtTitle.setText(TopicShared.getDraftTitle(this));
            edtTitle.setSelection(edtTitle.length()); // 这个必须最后调用
        }

        createTopicPresenter = new CreateTopicPresenter(this, this);
    }

    /**
     * 实时保存草稿
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (SettingShared.isEnableTopicDraft(this) && saveTopicDraft) {
            TopicShared.setDraftTabPosition(this, spnTab.getSelectedItemPosition());
            TopicShared.setDraftTitle(this, edtTitle.getText().toString());
            TopicShared.setDraftContent(this, edtContent.getText().toString());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                createTopicPresenter.createTopicAsyncTask(getTabByPosition(spnTab.getSelectedItemPosition()), edtTitle.getText().toString().trim(), edtContent.getText().toString().trim());
                return true;
            default:
                return false;
        }
    }

    private TabType getTabByPosition(int position) {
        switch (position) {
            case 0:
                return TabType.share;
            case 1:
                return TabType.ask;
            case 2:
                return TabType.news;
            case 3:
                return TabType.pybbs;
            default:
                return TabType.share;
        }
    }

    @Override
    public void onTitleError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtTitle.requestFocus();
    }

    @Override
    public void onContentError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtContent.requestFocus();
    }

    @Override
    public void onCreateTopicOk(@NonNull String topicId) {
        saveTopicDraft = false;
        TopicShared.clear(this);
        ToastUtils.with(this).show(R.string.post_success);
        Navigator.TopicWithAutoCompat.start(this, topicId);
        finish();
    }

    @Override
    public void onCreateTopicStart() {
        progressDialog.show();
    }

    @Override
    public void onCreateTopicFinish() {
        progressDialog.dismiss();
    }

}
