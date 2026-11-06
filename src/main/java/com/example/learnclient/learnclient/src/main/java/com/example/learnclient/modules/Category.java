package com.example.learnclient.modules;

public enum Category {
    MOVEMENT("Movement"),
    RENDER("Render");

    public final String title;

    Category(String title) {
        this.title = title;
    }
}
