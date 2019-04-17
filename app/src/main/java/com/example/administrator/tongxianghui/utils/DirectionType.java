package com.example.administrator.tongxianghui.utils;

import java.util.HashMap;
import java.util.Map;

public enum DirectionType {
    direction1,
    direction2,
    direction3,
    direction4,
    direction5,
    direction6;

    public static Map<Integer, String> directionType = new HashMap() {{
        put(DirectionType.direction1, "惠城、博罗至珠海同乡会包车");
        put(DirectionType.direction2, "珠海至惠城、博罗同乡会包车");
        put(DirectionType.direction3, "珠海至惠东、淡水客运站班次");
        put(DirectionType.direction4, "惠东、淡水至珠海客运站班次");
        put(DirectionType.direction5, "惠东、淡水至珠海同乡会包车");
        put(DirectionType.direction6, "珠海至惠东、淡水同乡会包车");
    }};

    public static String getDirectionType(DirectionType type) {
        return directionType.get(type);
    }
}
