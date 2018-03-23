package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class An implements Serializable{
    private int announcementId; //是 int 自增id
    private String title;//是 String 标题
    private int type;//是 int 1app 2cms
    private String content;//是 String 内容
    private Date createDate;//是 Date 创建时间
    private int lookStatus;//是 int 0 未读 1 已读
    private int status;// 是 int 公告状态（1，上线，2，已下线）
    private String keyword; //是 String 关键字
    private int toptag; //是 int 置顶标识（默认 1 置顶 2 未置顶）

    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getLookStatus() {
        return lookStatus;
    }

    public void setLookStatus(int lookStatus) {
        this.lookStatus = lookStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getToptag() {
        return toptag;
    }

    public void setToptag(int toptag) {
        this.toptag = toptag;
    }
}
