package com.kohanevich.service;

public enum Status {
    AVAILABLE, AVAILABLE_ONLY, EMPTY_ATM, BANKNOTES_OVERFLOW;
    public int amount;

    public static Status build(Status status, int amount) {
        status.amount = amount;
        return status;
    }
}
