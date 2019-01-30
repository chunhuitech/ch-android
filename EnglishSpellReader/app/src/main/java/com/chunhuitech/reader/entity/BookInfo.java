package com.chunhuitech.reader.entity;

import java.util.List;
import java.util.Map;

public class BookInfo {

    private String bookId;

    private List<Map<String, Object>> listResource;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public List<Map<String, Object>> getListResource() {
        return listResource;
    }

    public void setListResource(List<Map<String, Object>> listResource) {
        this.listResource = listResource;
    }
}
