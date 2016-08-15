package cn.tomoya.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import cn.tomoya.android.md.util.FormatUtils;

public class Author {

    @SerializedName("nickname")
    private String nickName;

    private String avatar;

    private String signature;

    private String url;

    private int score;

    @SerializedName("in_time")
    private Date inTime;

    private int id;

    private String email;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() { // 修复头像地址的历史遗留问题
        return FormatUtils.getCompatAvatarUrl(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
