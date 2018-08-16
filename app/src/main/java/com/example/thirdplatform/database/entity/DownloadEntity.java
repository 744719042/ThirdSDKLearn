package com.example.thirdplatform.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DownloadEntity {
    @com.example.thirdplatform.database.Id
    @Id(autoincrement = true)
    private Long id;
    private String downloadUrl;
    private Integer threadId;
    @Generated(hash = 1850300772)
    public DownloadEntity(Long id, String downloadUrl, Integer threadId) {
        this.id = id;
        this.downloadUrl = downloadUrl;
        this.threadId = threadId;
    }
    @Generated(hash = 1671715506)
    public DownloadEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDownloadUrl() {
        return this.downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public Integer getThreadId() {
        return this.threadId;
    }
    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }
}
