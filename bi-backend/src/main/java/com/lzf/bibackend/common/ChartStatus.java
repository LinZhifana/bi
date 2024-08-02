package com.lzf.bibackend.common;

public enum ChartStatus {

    WAIT("wait"),
    RUNNING("running"),
    SUCCEED("succeed"),
    FAILED("failed");

    private final String message;

    private ChartStatus(String message) {
        this.message = message;
    }

    public String getMessage() {return this.message;}
}
