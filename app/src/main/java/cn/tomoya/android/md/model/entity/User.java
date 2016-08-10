package cn.tomoya.android.md.model.entity;

import java.util.List;

public class User {

    private Author currentUser;
    private List<Topic> replies;
    private List<Topic> topics;
    private List<Topic> collects;

    public Author getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Author currentUser) {
        this.currentUser = currentUser;
    }

    public List<Topic> getReplies() {
        return replies;
    }

    public void setReplies(List<Topic> replies) {
        this.replies = replies;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Topic> getCollects() {
        return collects;
    }

    public void setCollects(List<Topic> collects) {
        this.collects = collects;
    }
}
