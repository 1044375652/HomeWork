package com.example.administrator.Tong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.administrator.Tong.model.BusMessageInfo;
import com.example.administrator.Tong.model.DirectionMessageInfo;
import com.example.administrator.Tong.model.OrderMessageInfo;
import com.example.administrator.Tong.model.PointMessagesInfo;
import com.example.administrator.Tong.model.RunningUserStatusInfo;
import com.example.administrator.Tong.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tongxianghui.db";
    private static int DATABASE_VERSION = 1;
    public static DataBaseHelper dataBaseHelper = null;
    private Context context;
    private static ContentValues contentValues = null;

    public DataBaseHelper(@Nullable Context context2) {
        super(context2, DATABASE_NAME, null, DATABASE_VERSION);
        context = context2;
    }

    private static class UserTable {
        private static final String TABLE_NAME = "user";
        private static final String ID = "_id";
        private static final String PHONE = "phone";
        private static final String ROLE = "role";
        private static final String SQL = "create table if not exists " + UserTable.TABLE_NAME + "(" + UserTable.ID + " INTEGER PRIMARY KEY," + UserTable.PHONE + " varchar(11)," + UserTable.ROLE + " tinyint(1))";
    }

    private static class BusMessageTable {
        private static final String TABLE_NAME = "bus_message";
        private static final String ID = "_id";
        private static final String Up_Date = "up_date";
        private static final String Up_Point = "point";
        private static final String Direction_Type = "direction_type";
        private static final String SQL = "create table if not exists " + BusMessageTable.TABLE_NAME + "(" + BusMessageTable.ID + " INTEGER PRIMARY KEY," + BusMessageTable.Up_Date + " bigint(13)," + BusMessageTable.Up_Point + " tinyint(1)," + BusMessageTable.Direction_Type + " tinyint(1))";
    }

    private static class DirectionMessagesTable {
        private static final String TABLE_NAME = "direction_messages";
        private static final String ID = "_id";
        private static final String DirectionType = "direction_type";
        private static final String SQL = "create table if not exists " + DirectionMessagesTable.TABLE_NAME + "(" + DirectionMessagesTable.ID + " integer primary key," + DirectionMessagesTable.DirectionType + " tinyint(1))";
    }

    private static class PointMessageTable {
        private static final String TABLE_NAME = "point_messages";
        private static final String ID = "_id";
        private static final String PointName = "point_name";
        private static final String PointType = "point_type";
        private static final String DirectionType = "direction_type";
        private static final String STATUS = "point_status";
        private static final String SQL = "create table if not exists " + PointMessageTable.TABLE_NAME + "(" + PointMessageTable.ID + " INTEGER PRIMARY KEY," + PointMessageTable.PointName + " tinyint(1)," + PointMessageTable.PointType + " tinyint(1)," + PointMessageTable.DirectionType + " tinyint(1)," + PointMessageTable.STATUS + " tinyint(1))";
    }

    private static class OrderMessageTable {
        private static final String TABLE_NAME = "order_messages";
        private static final String ID = "_id";
        private static final String UpPoint = "up_point";
        private static final String DownPoint = "down_point";
        private static final String DirectionType = "direction_type";
        private static final String TicketNumber = "ticket_number";
        private static final String Phone = "phone";
        private static final String UpDate = "up_date";
        private static final String PlateNumber = "plate_number";
        private static final String WithCarPhone = "with_car_phone";
        private static final String SQL = "create table if not exists " + OrderMessageTable.TABLE_NAME + "(" + OrderMessageTable.ID + " INTEGER PRIMARY KEY," + OrderMessageTable.UpPoint + " tinyint(1)," + OrderMessageTable.DownPoint + " tinyint(1)," + OrderMessageTable.DirectionType + " tinyint(1)," + OrderMessageTable.TicketNumber + " tinyint(1)," + OrderMessageTable.UpDate + " int(13)," + OrderMessageTable.Phone + " varchar(11)," + OrderMessageTable.PlateNumber + " varchar(10)," + OrderMessageTable.WithCarPhone + " varchar(10))";
    }

    private static class RunningUserStatusTable {
        private static final String TABLE_NAME = "running_user_status";
        private static final String ID = "_id";
        private static final String UserStatus = "user_status";
        private static final String Phone = "phone";
        private static final String PlateNumber = "plate_number";
        private static final String SQL = "create table if not exists " + RunningUserStatusTable.TABLE_NAME + "(" + RunningUserStatusTable.ID + " integer primary key not null," + RunningUserStatusTable.Phone + " varchar(11)," + RunningUserStatusTable.PlateNumber + " varchar(6)," + RunningUserStatusTable.UserStatus + " tinyint(1))";
    }

    public static synchronized DataBaseHelper getDataBaseHelper(Context context) {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper(context);
        }
        return dataBaseHelper;
    }

    public static synchronized ContentValues getContentValues() {
        if (contentValues == null) {
            contentValues = new ContentValues();
        }
        return contentValues;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserTable.SQL);
        sqLiteDatabase.execSQL(BusMessageTable.SQL);
        sqLiteDatabase.execSQL(DirectionMessagesTable.SQL);
        sqLiteDatabase.execSQL(PointMessageTable.SQL);
        sqLiteDatabase.execSQL(OrderMessageTable.SQL);
        sqLiteDatabase.execSQL(RunningUserStatusTable.SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private SQLiteDatabase getMyWritableDatabase() {
        return getDataBaseHelper(context).getWritableDatabase();
    }

    private SQLiteDatabase getMyReadableDatabase() {
        return getDataBaseHelper(context).getReadableDatabase();
    }

    //删除数据库
    public boolean deleteDataBase() {
        context.deleteDatabase(DataBaseHelper.DATABASE_NAME);
        return true;
    }

    //增加数据User
    public boolean addDataToUserTable(List<User> users) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (User user : users) {
            contentValues.put(UserTable.ID, user.getId());
            contentValues.put(UserTable.ROLE, user.getRole());
            contentValues.put(UserTable.PHONE, user.getPhone());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(UserTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }

        return true;
    }

    //删除数据

    //查询数据User
    public List<User> selectDataFromUserTable(String[] columns, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(UserTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        List<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            users.add(new User()
                    .setPhone(cursor.getString(cursor.getColumnIndex(UserTable.PHONE)))
                    .setId(cursor.getInt(cursor.getColumnIndex(UserTable.ID)))
                    .setRole(cursor.getInt(cursor.getColumnIndex(UserTable.ROLE))));
        }
        return users;
    }

    //修改数据


    //增加数据BusMessageTable
    public boolean addDataToBusMessageTable(List<BusMessageInfo> busMessageInfos) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = getContentValues();
        for (BusMessageInfo busMessageInfo : busMessageInfos) {
            contentValues.clear();
            contentValues.put(BusMessageTable.Up_Point, busMessageInfo.getPoint());
            contentValues.put(BusMessageTable.Direction_Type, busMessageInfo.getDirectionType());
            contentValues.put(BusMessageTable.Up_Date, busMessageInfo.getUpDate());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(BusMessageTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    //选择数据BusMessageTable
    public List<BusMessageInfo> selectDataFromBusMessageTable(String[] columns, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<BusMessageInfo> busMessageInfos = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(BusMessageTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        while (cursor.moveToNext()) {
            busMessageInfos.add(new BusMessageInfo().setId(cursor.getInt(cursor.getColumnIndex(BusMessageTable.ID)))
                    .setPoint(cursor.getInt(cursor.getColumnIndex(BusMessageTable.Up_Point)))
                    .setUpDate(cursor.getLong(cursor.getColumnIndex(BusMessageTable.Up_Date))));
        }
        return busMessageInfos;
    }

    //删除数据BusMessageTable
    public void deleteDataFromBusMessageTable(String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        sqLiteDatabase.delete(BusMessageTable.TABLE_NAME, whereClause, whereArgs);
    }

    //选择数据DirectionMessagesTable
    public List<DirectionMessageInfo> selectDataFromDirectionMessagesTable(String[] columns, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyReadableDatabase();
        List<DirectionMessageInfo> directionMessageInfoList = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.query(DirectionMessagesTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        sqLiteDatabase.endTransaction();
        while (cursor.moveToNext()) {
            directionMessageInfoList.add(new DirectionMessageInfo()
                    .setId(cursor.getInt(cursor.getColumnIndex(DirectionMessagesTable.ID)))
                    .setDirectionType(cursor.getInt(cursor.getColumnIndex(DirectionMessagesTable.DirectionType)))
            );
        }
        return directionMessageInfoList;
    }


    //删除数据DirectionMessagesTable
    public boolean deleteDataFromDirectionMessagesTable(String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete(DirectionMessagesTable.TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return true;
    }

    //增加数据DirectionMessagesTable
    public boolean addDataToDirectionMessagesTable(List<DirectionMessageInfo> directionMessageInfoList) {
        ContentValues contentValues = getContentValues();
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
            contentValues.clear();
            contentValues.put(DirectionMessagesTable.ID, directionMessageInfo.getId());
            contentValues.put(DirectionMessagesTable.DirectionType, directionMessageInfo.getDirectionType());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(DirectionMessagesTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    //修改数据DirectionMessagesTable
    public boolean modifyDataToDirectionMessagesTable(List<DirectionMessageInfo> directionMessageInfoList, String whereClause, String[] whereArgs) {
        ContentValues contentValues = getContentValues();
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();

        for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
            contentValues.clear();
            contentValues.put(DirectionMessagesTable.DirectionType, directionMessageInfo.getDirectionType());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.update(DirectionMessagesTable.TABLE_NAME, contentValues, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    //选择数据PointMessageTable
    public List<PointMessagesInfo> selectDataFromPointMessageTable(String[] columns, String whereClause, String[] wherArgs) {
        SQLiteDatabase sqLiteDatabase = getMyReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(PointMessageTable.TABLE_NAME, columns, whereClause, wherArgs, null, null, null);
        List<PointMessagesInfo> pointMessagesInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            pointMessagesInfoList.add(new PointMessagesInfo()
                    .setId(cursor.getInt(cursor.getColumnIndex(PointMessageTable.ID)))
                    .setPointName(cursor.getInt(cursor.getColumnIndex(PointMessageTable.PointName)))
                    .setDirectionType(cursor.getInt(cursor.getColumnIndex(PointMessageTable.DirectionType)))
                    .setPointType(cursor.getInt(cursor.getColumnIndex(PointMessageTable.PointType)))
                    .setPointStatus(cursor.getInt(cursor.getColumnIndex(PointMessageTable.STATUS)))
            );
        }
        return pointMessagesInfoList;
    }

    //添加数据PointMessageTable
    public void addDataToPointMessageTable(List<PointMessagesInfo> pointMessagesInfoList) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        ContentValues contentValues = getContentValues();
        for (PointMessagesInfo pointMessagesInfo : pointMessagesInfoList) {
            contentValues.clear();
            contentValues.put(PointMessageTable.ID, pointMessagesInfo.getId());
            contentValues.put(PointMessageTable.DirectionType, pointMessagesInfo.getDirectionType());
            contentValues.put(PointMessageTable.PointName, pointMessagesInfo.getPointName());
            contentValues.put(PointMessageTable.PointType, pointMessagesInfo.getPointType());
            contentValues.put(PointMessageTable.STATUS, 0);
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(PointMessageTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    //修改数据PointMessageTable
    public boolean modifyDataToPointMessageTable(List<PointMessagesInfo> pointMessagesInfoList, String whereClause, String[] whereArgs) {
        ContentValues contentValues = getContentValues();
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();

        for (PointMessagesInfo pointMessagesInfo : pointMessagesInfoList) {
            contentValues.clear();
            contentValues.put(PointMessageTable.STATUS, pointMessagesInfo.getPointStatus());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.update(PointMessageTable.TABLE_NAME, contentValues, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    //删除数据PointMessageTable
    public boolean deleteDataToPointMessageTable(String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        sqLiteDatabase.delete(PointMessageTable.TABLE_NAME, whereClause, whereArgs);
        return true;
    }


    //选择数据OrderMessageTable
    public List<OrderMessageInfo> selectDataFromOrderMessageTable(String[] columns, String whereClause, String[] wherArgs) {
        SQLiteDatabase sqLiteDatabase = getMyReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(OrderMessageTable.TABLE_NAME, columns, whereClause, wherArgs, null, null, null);
        List<OrderMessageInfo> orderMessageInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            orderMessageInfoList.add(new OrderMessageInfo()
                    .setId(cursor.getInt(cursor.getColumnIndex(OrderMessageTable.ID)))
                    .setDirectionType(cursor.getInt(cursor.getColumnIndex(OrderMessageTable.DirectionType)))
                    .setUpDate(cursor.getLong(cursor.getColumnIndex(OrderMessageTable.UpDate)))
                    .setDownPoint(cursor.getInt(cursor.getColumnIndex(OrderMessageTable.DownPoint)))
                    .setTicketNumber(cursor.getInt(cursor.getColumnIndex(OrderMessageTable.TicketNumber)))
                    .setUpPoint(cursor.getInt(cursor.getColumnIndex(OrderMessageTable.UpPoint)))
                    .setPhone(cursor.getString(cursor.getColumnIndex(OrderMessageTable.Phone)))
                    .setPlateNumber(cursor.getString(cursor.getColumnIndex(OrderMessageTable.PlateNumber)))
                    .setWithCarPhone(cursor.getString(cursor.getColumnIndex(OrderMessageTable.WithCarPhone)))
            );
        }
        return orderMessageInfoList;
    }

    //添加数据OrderMessageTable
    public void addDataToOrderMessageTable(List<OrderMessageInfo> orderMessageInfoList) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        ContentValues contentValues = getContentValues();
        for (OrderMessageInfo orderMessageInfo : orderMessageInfoList) {
            contentValues.clear();
            contentValues.put(OrderMessageTable.ID, orderMessageInfo.getId());
            contentValues.put(OrderMessageTable.DirectionType, orderMessageInfo.getDirectionType());
            contentValues.put(OrderMessageTable.DownPoint, orderMessageInfo.getDownPoint());
            contentValues.put(OrderMessageTable.UpPoint, orderMessageInfo.getUpPoint());
            contentValues.put(OrderMessageTable.UpDate, orderMessageInfo.getUpDate());
            contentValues.put(OrderMessageTable.TicketNumber, orderMessageInfo.getTicketNumber());
            contentValues.put(OrderMessageTable.Phone, orderMessageInfo.getPhone());
            contentValues.put(OrderMessageTable.PlateNumber, orderMessageInfo.getPlateNumber());
            contentValues.put(OrderMessageTable.WithCarPhone, orderMessageInfo.getWithCarPhone());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(OrderMessageTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    //修改数据OrderMessageTable


    //删除数据OrderMessageTable
    public boolean deleteDataToOrderMessageTable(String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        sqLiteDatabase.delete(OrderMessageTable.TABLE_NAME, whereClause, whereArgs);
        return true;
    }

    //选择数据RunningUserStatusTable
    public List<RunningUserStatusInfo> selectDataFromRunningUserStatusTable(String[] columns, String whereClause, String[] wherArgs) {
        SQLiteDatabase sqLiteDatabase = getMyReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(OrderMessageTable.TABLE_NAME, columns, whereClause, wherArgs, null, null, null);
        List<RunningUserStatusInfo> runningUserStatusInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            runningUserStatusInfoList.add(new RunningUserStatusInfo()
                    .setId(cursor.getInt(cursor.getColumnIndex(RunningUserStatusTable.ID)))
                    .setPhone(cursor.getString(cursor.getColumnIndex(RunningUserStatusTable.Phone)))
                    .setPlateNumber(cursor.getString(cursor.getColumnIndex(RunningUserStatusTable.PlateNumber)))
                    .setUserStatus(cursor.getInt(cursor.getColumnIndex(RunningUserStatusTable.PlateNumber)))
            );
        }
        return runningUserStatusInfoList;
    }

    //添加数据RunningUserStatusTable
    public void addDataToRunningUserStatusTable(List<RunningUserStatusInfo> runningUserStatusInfoList) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        ContentValues contentValues = getContentValues();
        for (RunningUserStatusInfo runningUserStatusInfo : runningUserStatusInfoList) {
            contentValues.clear();
            contentValues.put(RunningUserStatusTable.UserStatus, runningUserStatusInfo.getUserStatus());
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(RunningUserStatusTable.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    //修改数据RunningUserStatusTable


    //删除数据RunningUserStatusTable
    public boolean deleteDataToRunningUserStatusTable(String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getMyWritableDatabase();
        sqLiteDatabase.delete(RunningUserStatusTable.TABLE_NAME, whereClause, whereArgs);
        return true;
    }

}
