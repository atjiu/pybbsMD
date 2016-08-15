package cn.tomoya.android.md.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import cn.tomoya.android.md.model.api.ApiDefine;
import cn.tomoya.android.md.util.FormatUtils;

public class Reply {

    private String id;

    private int tid;

    private String author;

    private String avatar;

    private String content;

    @SerializedName("in_time")
    private Date inTime;

    private boolean isdelete;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public boolean isdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        contentHtml = null; // 清除已经处理的Html渲染缓存
    }

    /**
     * Html渲染缓存
     */

    private String contentHtml;

    public String getContentHtml() {
        System.out.println(getContent());
        if (contentHtml == null) {
            if (ApiDefine.MD_RENDER) {
                contentHtml = FormatUtils.handleHtml(content);
            } else {
                contentHtml = FormatUtils.handleHtml(FormatUtils.renderMarkdown(content));
            }
        }
        return contentHtml;
    }

    public void setContentFromLocal(String content) {
        if (ApiDefine.MD_RENDER) {
            this.content = FormatUtils.renderMarkdown(content);
        } else {
            this.content = content;
        }
        contentHtml = null; // 清除已经处理的Html渲染缓存
    }

}
