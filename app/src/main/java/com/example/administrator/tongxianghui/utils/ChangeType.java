package com.example.administrator.tongxianghui.utils;

public class ChangeType {
    public static class PointType {
        public static int MsgToCode(String msg) {
            switch (msg) {
                case "花边岭广场":
                    return 1;
                case "惠大":
                    return 2;
                case "江北华贸":
                    return 3;
                case "博罗华侨中学":
                    return 4;
            }
            return 1;
        }

        public static String CodeToMsg(int code) {
            switch (code) {
                case 1:
                    return "花边岭广场";
                case 2:
                    return "惠大";
                case 3:
                    return "江北华贸";
                case 4:
                    return "博罗华侨中学";
            }
            return "花边岭广场";
        }
    }
}
