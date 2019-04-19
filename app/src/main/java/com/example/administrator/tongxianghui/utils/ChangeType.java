package com.example.administrator.tongxianghui.utils;

public class ChangeType {
    public static class DirectionType {
        public static int MsgToCode(String msg) {
            switch (msg) {
                case "惠城、博罗至珠海同乡会包车":
                    return 1;
                case "珠海至惠城、博罗同乡会包车":
                    return 2;
                case "珠海至惠东、淡水客运站班次":
                    return 3;
                case "惠东、淡水至珠海客运站班次":
                    return 4;
                case "惠东、淡水至珠海同乡会包车":
                    return 5;
                case "珠海至惠东、淡水同乡会包车":
                    return 6;
            }
            return 1;
        }


        public static String CodeToMsg(int code) {
            switch (code) {
                case 1:
                    return "惠城、博罗至珠海同乡会包车";
                case 2:
                    return "珠海至惠城、博罗同乡会包车";
                case 3:
                    return "珠海至惠东、淡水客运站班次";
                case 4:
                    return "惠东、淡水至珠海客运站班次";
                case 5:
                    return "惠东、淡水至珠海同乡会包车";
                case 6:
                    return "珠海至惠东、淡水同乡会包车";
            }
            return "惠城、博罗至珠海同乡会包车";
        }
    }


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
