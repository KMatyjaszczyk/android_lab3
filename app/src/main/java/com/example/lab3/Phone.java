package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phone")
public class Phone {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "producer")
    private String producer;

    @NonNull
    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "version")
    private String version;

    @ColumnInfo(name = "website_url")
    private String websiteUrl;

    public Phone(@NonNull String producer, @NonNull String model) {
        this.producer = producer;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getProducer() {
        return producer;
    }

    public void setProducer(@NonNull String producer) {
        this.producer = producer;
    }

    @NonNull
    public String getModel() {
        return model;
    }

    public void setModel(@NonNull String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
