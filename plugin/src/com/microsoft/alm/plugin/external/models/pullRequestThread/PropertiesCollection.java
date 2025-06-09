package com.microsoft.alm.plugin.external.models.pullRequestThread;

import java.util.Map;

public class PropertiesCollection {
    private int count;
    private Map<String, Object> item;
    private String[] keys;
    private String[] values;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<String, Object> getItem() {
        return item;
    }

    public void setItem(Map<String, Object> item) {
        this.item = item;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}