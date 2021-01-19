package com.github.oliverschen.dynamic.config;

/**
 * @author ck
 */
public class DataSourceHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();


    public static void setDataSource(String dsType) {
        HOLDER.set(dsType);
    }

    public static String getDataSource() {
       return HOLDER.get();
    }

    public static void clearDataSource() {
        HOLDER.remove();
    }
}
