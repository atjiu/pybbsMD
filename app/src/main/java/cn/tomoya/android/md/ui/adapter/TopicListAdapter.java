package cn.tomoya.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.ui.activity.UserDetailActivity;
import cn.tomoya.android.md.ui.util.Navigator;
import cn.tomoya.android.md.util.FormatUtils;

public class TopicListAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Topic> topicList = new ArrayList<>();

    public TopicListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<Topic> getTopicList() {
        return topicList;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(cn.tomoya.android.md.R.layout.item_topic, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(topicList.get(position));
        return convertView;
    }

    public class ViewHolder {

        @BindView(cn.tomoya.android.md.R.id.ctv_tab)
        protected CheckedTextView ctvTab;

        @BindView(cn.tomoya.android.md.R.id.tv_title)
        protected TextView tvTitle;

        @BindView(cn.tomoya.android.md.R.id.img_avatar)
        protected ImageView imgAvatar;

        @BindView(cn.tomoya.android.md.R.id.tv_author)
        protected TextView tvAuthor;

        @BindView(cn.tomoya.android.md.R.id.tv_create_time)
        protected TextView tvCreateTime;

        @BindView(cn.tomoya.android.md.R.id.tv_reply_count)
        protected TextView tvReplyCount;

        @BindView(cn.tomoya.android.md.R.id.tv_visit_count)
        protected TextView tvVisitCount;

        @BindView(cn.tomoya.android.md.R.id.tv_last_reply_time)
        protected TextView tvLastReplyTime;

        @BindView(cn.tomoya.android.md.R.id.icon_good)
        protected View iconGood;

        private Topic topic;

        protected ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }

        protected void update(@NonNull Topic topic) {
            this.topic = topic;

            tvTitle.setText(topic.getTitle());
            ctvTab.setText(topic.isTop() ? cn.tomoya.android.md.R.string.tab_top : topic.getTab().getNameId());
            ctvTab.setChecked(topic.isTop());
            Glide.with(activity).load(topic.getAvatar()).placeholder(cn.tomoya.android.md.R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvAuthor.setText(topic.getAuthor());
            tvCreateTime.setText(activity.getString(cn.tomoya.android.md.R.string.create_at_$) + FormatUtils.formatDate(topic.getInTime()));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getView()));
            tvLastReplyTime.setText(FormatUtils.getRelativeTimeSpanString(topic.getLastReplyTime()));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(cn.tomoya.android.md.R.id.img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor(), imgAvatar, topic.getAvatar());
        }

        @OnClick(cn.tomoya.android.md.R.id.btn_item)
        protected void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, topic);
        }

    }

}
