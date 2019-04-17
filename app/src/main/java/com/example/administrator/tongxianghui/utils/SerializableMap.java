package com.example.administrator.tongxianghui.utils;

import java.io.Serializable;
import java.util.Map;

public class SerializableMap implements Serializable {

    public SerializableMap(Map<String, DirectionType> type) {
        this.type = type;
    }

    private Map<String, DirectionType> type;

    public Map<String, DirectionType> getType() {
        return type;
    }

    public SerializableMap setType(Map<String, DirectionType> type) {
        this.type = type;
        return this;
    }
}
