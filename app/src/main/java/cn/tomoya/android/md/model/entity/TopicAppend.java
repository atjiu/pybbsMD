package cn.tomoya.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by tomoya on 16/8/9.
 */
public class TopicAppend {

    private int id;
    private String content;
    @SerializedName("in_time")
    private Date inTime;
    private int tid;
    private boolean isdelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public boolean isdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }
}
