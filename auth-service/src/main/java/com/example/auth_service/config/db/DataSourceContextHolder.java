package com.example.auth_service.config.db;

public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> context = new ThreadLocal<>();

    public static void setMode(DataSourceType mode) {
        context.set(mode);
    }

    public static DataSourceType getCurrentMode() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

}
