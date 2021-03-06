package com.example.thirdplatform.constant;

public class KeyValue {
    private String key;
    private Object value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, Binary binary) {
        this.key = key;
        this.value = binary;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    public Object getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
