package cn.tomoya.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicWithReply {
    private List<Reply> replies;

    @SerializedName("authorinfo")
    private Author authorInfo;

    private List<TopicAppend> topicAppends;

    private int collectCount;

    private Boolean isCollect;

    private Topic topic;

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public Author getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(Author authorInfo) {
        this.authorInfo = authorInfo;
    }

    public List<TopicAppend> getTopicAppends() {
        return topicAppends;
    }

    public void setTopicAppends(List<TopicAppend> topicAppends) {
        this.topicAppends = topicAppends;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
