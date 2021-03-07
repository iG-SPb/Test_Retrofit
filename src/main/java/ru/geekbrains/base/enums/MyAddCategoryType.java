package ru.geekbrains.base.enums;

import lombok.Getter;

public enum MyAddCategoryType {
    ADD_TV(25, "TV"),
    ADD_RADIO(26, "RADIO");

    @Getter
    private final Integer id;
    @Getter
    private final String title;

    MyAddCategoryType(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
