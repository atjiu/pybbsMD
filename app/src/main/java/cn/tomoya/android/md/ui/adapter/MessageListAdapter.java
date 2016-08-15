package cn.tomoya.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.entity.Message;
import cn.tomoya.android.md.model.entity.Notification;
import cn.tomoya.android.md.ui.activity.UserDetailActivity;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.ui.widget.ContentWebView;
import cn.tomoya.android.md.util.FormatUtils;
import cn.tomoya.android.md.util.ResUtils;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Notification> messageList = new ArrayList<>();

    public MessageListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<Notification> getMessageList() {
        return messageList;
    }

    public void markAllMessageRead() {
        for (Notification message : messageList) {
            message.setRead(true);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(messageList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar)
        protected ImageView imgAvatar;

        @BindView(R.id.tv_from)
        protected TextView tvFrom;

        @BindView(R.id.tv_time)
        protected TextView tvTime;

        @BindView(R.id.tv_action)
        protected TextView tvAction;

        @BindView(R.id.badge_read)
        protected View badgeRead;

        @BindView(R.id.web_content)
        protected ContentWebView webContent;

        @BindView(R.id.tv_topic_title)
        protected TextView tvTopicTitle;

        private Notification message;

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void update(@NonNull Notification message) {
            this.message = message;

            Glide.with(activity).load(message.getAvatar()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvFrom.setText(message.getAuthor());
            tvTime.setText(FormatUtils.getRelativeTimeSpanString(message.getInTime()));
            tvTime.setTextColor(ResUtils.getThemeAttrColor(activity, message.isRead() ? android.R.attr.textColorSecondary : R.attr.colorAccent));
            badgeRead.setVisibility(message.isRead() ? View.GONE : View.VISIBLE);
            tvTopicTitle.setText("话题：" + message.getTitle());

            // 判断通知类型
            if (message.getAction().equals(Message.Type.AT.name())) {
                tvAction.setText("在回复中@了您");
                webContent.setVisibility(View.VISIBLE);
                webContent.loadRenderedContent(message.getContentHtml());  // 这里直接使用WebView，有性能问题
            } else if(message.getAction().equals(Message.Type.REPLY.name())) {
                tvAction.setText("回复了您的话题");
                webContent.setVisibility(View.VISIBLE);
                webContent.loadRenderedContent(message.getContentHtml());  // 这里直接使用WebView，有性能问题
            } else if(message.getAction().equals(Message.Type.COLLECT.name())) {
                tvAction.setText("收藏了您的话题");
                webContent.setVisibility(View.VISIBLE);
                webContent.loadRenderedContent(message.getContentHtml());  // 这里直接使用WebView，有性能问题
            }
        }

        @OnClick(R.id.img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, message.getAuthor(), imgAvatar, message.getAvatar());
        }

        @OnClick(R.id.btn_item)
        protected void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, String.valueOf(message.getTid()));
        }

    }

}
