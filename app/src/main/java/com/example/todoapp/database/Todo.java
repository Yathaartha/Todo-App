package com.example.todoapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.util.UUID;

@Entity(tableName="TODO")
public class Todo {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private UUID id;
    private String title;
    private String detail;
    private Boolean isComplete;
    private Date createdAt;
    private Date lastUpdated;
    private Date completedAt;
    private int priority;

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", isComplete=" + isComplete +
                ", createdAt=" + createdAt +
                ", lastUpdated=" + lastUpdated +
                ", completedAt=" + completedAt +
                ", priority=" + priority +
                '}';
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean complete) {
        isComplete = complete;
    }

    public Todo() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date(System.currentTimeMillis());
        this.isComplete = false;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public UUID getId() {
        return this.id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}