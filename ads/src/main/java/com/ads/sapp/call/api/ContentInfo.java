package com.ads.sapp.call.api;

import com.google.gson.annotations.SerializedName;

public class ContentInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("path")
    private String path;

    @SerializedName("content")
    private String content;

    @SerializedName("encoding")
    private String encoding;

    public ContentInfo(String name, String path, String content, String encoding) {
        this.name = name;
        this.path = path;
        this.content = content;
        this.encoding = encoding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
