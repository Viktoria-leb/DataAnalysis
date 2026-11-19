package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TypeTransaction {
    @JsonProperty("Обычная")
    Regular("Regular"),
    @JsonProperty("Налогооблагаемая")
    Taxable("Taxable"),
    @JsonProperty("Повторяющаяся")
    Recurrent("Recurrent"),
    @JsonProperty("Иностранная")
    ForeignCurrency("ForeignCurrency"),
    @JsonProperty("С комментарием")
    Commentable("Commentable");

    private final String type;

    public String getType() {
        return type;
    }

    TypeTransaction(String type) {
        this.type = type;
    }
}

