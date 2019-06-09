package com.example.administrator.Tong.utils;

public class ChangeType {
    public static class DirectionType {
        public static int MsgToCode(String msg) {
            switch (msg) {
                case "惠城、博罗至珠海同乡会包车":
                    return 0;
                case "珠海至惠城、博罗同乡会包车":
                    return 1;
                case "珠海至惠东、淡水客运站班次":
                    return 2;
                case "惠东、淡水至珠海客运站班次":
                    return 3;
                case "惠东、淡水至珠海同乡会包车":
                    return 4;
                case "珠海至惠东、淡水同乡会包车":
                    return 5;
                case "全部":
                    return 11;
            }
            return 0;
        }


        public static String CodeToMsg(int code) {
            switch (code) {
                case 0:
                    return "惠城、博罗至珠海同乡会包车";
                case 1:
                    return "珠海至惠城、博罗同乡会包车";
                case 2:
                    return "珠海至惠东、淡水客运站班次";
                case 3:
                    return "惠东、淡水至珠海客运站班次";
                case 4:
                    return "惠东、淡水至珠海同乡会包车";
                case 5:
                    return "珠海至惠东、淡水同乡会包车";
                case 11:
                    return "全部";
            }
            return "惠城、博罗至珠海同乡会包车";
        }
    }

    public static class PointType {

        public static int MsgToCode(String msg) {
            switch (msg) {
                case "花边岭广场":
                    return 0;
                case "惠大":
                    return 1;
                case "江北华贸":
                    return 2;
                case "博罗华侨中学":
                    return 3;
                case "香洲梅华中":
                    return 4;
                case "北理工":
                    return 5;
                case "北师":
                    return 6;
                case "UIC":
                    return 7;
                case "官塘公交站":
                    return 8;
                case "惠东客运站":
                    return 9;
                case "淡水客运站":
                    return 10;
            }
            return 0;
        }

        public static String CodeToMsg(int code) {
            switch (code) {
                case 0:
                    return "花边岭广场";
                case 1:
                    return "惠大";
                case 2:
                    return "江北华贸";
                case 3:
                    return "博罗华侨中学";
                case 4:
                    return "香洲梅华中";
                case 5:
                    return "北理工";
                case 6:
                    return "北师";
                case 7:
                    return "UIC";
                case 8:
                    return "官塘公交站";
                case 9:
                    return "惠东客运站";
                case 10:
                    return "淡水客运站";
            }
            return "花边岭广场";
        }
    }

    public static class Change {
        public static String codeToMsg(int directionType) {
            String msg = "当前乘车方向：";
            switch (directionType) {
                case 0:
                    msg += "惠城、博罗至珠海同乡会包车";
                    break;
                case 1:
                    msg += "珠海至惠城、博罗同乡会包车";
                    break;
                case 2:
                    msg += "珠海至惠东、淡水客运站班次";
                    break;
                case 3:
                    msg += "惠东、淡水至珠海客运站班次";
                    break;
                case 4:
                    msg += "惠东、淡水至珠海同乡会包车";
                    break;
                case 5:
                    msg += "珠海至惠东、淡水同乡会包车";
                    break;
            }
            return msg;
        }
    }

    public static class UserStatusType {
        //0：没看到车，1：看到车，2：服务区上厕所，3：上完厕所回来
        public static String CodeToMsg(int code) {
            switch (code) {
                case 0:
                    return "没看到车";
                case 1:
                    return "我已上车";
                case 2:
                    return "服务区上厕所";
                case 3:
                    return "上完厕所回来";
            }
            return "没看到车";
        }

        public static int MsgToCode(String msg) {
            switch (msg) {
                case "没看到车":
                    return 0;
                case "我已上车":
                    return 1;
                case "服务区上厕所":
                    return 2;
                case "上完厕所回来":
                    return 3;
            }
            return 0;
        }
    }

    public static class BusStatusType {
        public static String CodeToMsg(int code) {
            String msg = "";
            switch (code) {
                case 0:
                    msg = "未发车";
                    break;
                case 1:
                    msg = "正在前往花边岭广场";
                    break;
                case 2:
                    msg = "已到花边岭广场";
                    break;
                case 3:
                    msg = "正在前往惠大";
                    break;
                case 4:
                    msg = "已到惠大";
                    break;
                case 5:
                    msg = "正在前往江北华贸";
                    break;
                case 6:
                    msg = "已到江北华贸";
                    break;
                case 7:
                    msg = "正在前往博罗华侨中学";
                    break;
                case 8:
                    msg = "已到博罗华侨中学";
                    break;
                case 9:
                    msg = "正在前往香洲梅华中";
                    break;
                case 10:
                    msg = "已到香洲梅华中";
                    break;
                case 11:
                    msg = "正在前往北理工";
                    break;
                case 12:
                    msg = "已到北理工";
                    break;
                case 13:
                    msg = "正在前往北师";
                    break;
                case 14:
                    msg = "已到北师";
                    break;
                case 15:
                    msg = "正在前往UIC";
                    break;
                case 16:
                    msg = "已到UIC";
                    break;
            }
            return msg;
        }

        public static int MsgToCode(String msg) {
            int code = 0;
            switch (msg) {
                case "未发车":
                    code = 0;
                    break;
                case "正在前往花边岭广场":
                    code = 1;
                    break;
                case "已到花边岭广场":
                    code = 2;
                    break;
                case "正在前往惠大":
                    code = 3;
                    break;
                case "已到惠大":
                    code = 4;
                    break;
                case "正在前往江北华贸":
                    code = 5;
                    break;
                case "已到江北华贸":
                    code = 6;
                    break;
                case "正在前往博罗华侨中学":
                    code = 7;
                    break;
                case "已到博罗华侨中学":
                    code = 8;
                    break;
                case "正在前往香洲梅华中":
                    code = 9;
                    break;
                case "已到香洲梅华中":
                    code = 10;
                    break;
                case "正在前往北理工":
                    code = 11;
                    break;
                case "已到北理工":
                    code = 12;
                    break;
                case "正在前往北师":
                    code = 13;
                    break;
                case "已到北师":
                    code = 14;
                    break;
                case "正在前往UIC":
                    code = 15;
                    break;
                case "已到UIC":
                    code = 16;
                    break;
            }
            return code;
        }
    }

}
