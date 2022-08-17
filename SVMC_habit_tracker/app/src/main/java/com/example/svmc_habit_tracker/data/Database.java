package com.example.svmc_habit_tracker.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.data.model.Habit;
import com.example.svmc_habit_tracker.fragment.statistic.MonthlyStatistic;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Database extends SQLiteOpenHelper {
    // Ten DB
    private static final String DBNAME = "SVMCMiniProject1.db";
    public Database(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    // insert, update, delete
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    // select
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //imageview to byte
    public byte[] ImageView_to_Byte(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // create TABLE
    public void createTable(){
        QueryData("CREATE TABLE IF NOT EXISTS Habit(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name VARCHAR, " +
                "Icon BLOG, " +
                "TimeStart VARCHAR, " +
                "TimeEnd VARCHAR, " +
                "Description VARCHAR, " +
                "Unit VARCHAR, " +
                "Goal INTEGER, " +
                "Done INTEGER, " +
                "Reminder VARCHAR," +
                "Date VARCHAR)");
    }

    // delete TABLE
    public void deleteDB(){
        QueryData("DROP DATABASE SVMCMiniProject.db");
    }
    public void deleteTable(){
        QueryData("DROP TABLE Habit");
    }

    // insert habit
    public void insertHabit(String nameHabit, byte[] icon, String timeStart, String timeEnd, String description, String unit, long goal, String reminder, String date) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Habit (Id, Name, Icon, TimeStart, TimeEnd, Description, Unit, Goal, Reminder, Date) VALUES(null, ?, ? ,?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql); // vi luu hinh anh nen phai dung statement de biet
        statement.bindString(1, nameHabit);
        statement.bindBlob(2, icon);
        statement.bindString(3, timeStart);
        statement.bindString(4, timeEnd);
        statement.bindString(5, description);
        statement.bindString(6, unit);
        statement.bindLong(7, goal);
        statement.bindString(8, reminder);
        statement.bindString(9, date);
        statement.executeInsert();
    }

    // insert list habit from startdate to endDate
    public void insertHabitStartToEnd(String nameHabit, byte[] icon, String timeStart, String timeEnd, String description, String unit, long goal, String reminder, String date) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Habit (Id, Name, Icon, TimeStart, TimeEnd, Description, Unit, Goal, Reminder, Date) VALUES(null, ?, ? ,?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql); // vi luu hinh anh nen phai dung statement de biet



        String timeStartInput = timeStart;
        String timeEndInput = timeEnd;
        int[] timeStartArray = getDateMonthYearfromInput(timeStartInput);
        int[] timeEndArray = getDateMonthYearfromInput(timeEndInput);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(timeEndArray[2], timeEndArray[1], timeEndArray[0]);

        long daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());

        while (daysBetween >= 0){
//            timeStartInput = setStandardDateFormat(calendarStart.getTime());

            timeStartInput = setStandardDateFormat(calendarStart);
            /// hàm setStandartDateFormat có vấn đề




            statement.bindString(1, nameHabit);
            statement.bindBlob(2, icon);
            statement.bindString(3, timeStart);
            statement.bindString(4, timeEnd);
            statement.bindString(5, description);
            statement.bindString(6, unit);
            statement.bindLong(7, goal);
            statement.bindString(8, reminder);
            statement.bindString(9, timeStartInput);
            statement.executeInsert();

            timeStartArray = getDateMonthYearfromInput(timeStartInput);
            calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);
            calendarStart.add(Calendar.DAY_OF_MONTH, +1);
//            calendarEnd.add(Calendar.DAY_OF_MONTH, +1);
            daysBetween--;

        }

    }

    /*
    *
    * // BACK UP insert list habit from startdate to endDate
    public void insertHabitStartToEnd(String nameHabit, byte[] icon, String timeStart, String timeEnd, String description, String unit, long goal, String reminder, String date) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Habit (Id, Name, Icon, TimeStart, TimeEnd, Description, Unit, Goal, Reminder, Date) VALUES(null, ?, ? ,?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql); // vi luu hinh anh nen phai dung statement de biet



        String timeStartInput = timeStart;
        String timeEndInput = timeEnd;
        int[] timeStartArray = getDateMonthYearfromInput(timeStartInput);
        int[] timeEndArray = getDateMonthYearfromInput(timeEndInput);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(timeEndArray[2], timeEndArray[1], timeEndArray[0]);

        long daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());

        while (daysBetween >= 0){
            timeStartInput = setStandardDateFormat(calendarStart.getTime());



            statement.bindString(1, nameHabit);
            statement.bindBlob(2, icon);
            statement.bindString(3, timeStart);
            statement.bindString(4, timeEnd);
            statement.bindString(5, description);
            statement.bindString(6, unit);
            statement.bindLong(7, goal);
            statement.bindString(8, reminder);
            statement.bindString(9, timeStartInput);
            statement.executeInsert();

            timeStartArray = getDateMonthYearfromInput(timeStartInput);
            calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);
            calendarStart.add(Calendar.DAY_OF_MONTH, +1);
//            calendarEnd.add(Calendar.DAY_OF_MONTH, +1);
            daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());

        }

    }
    * */
    // return a arrayList Habit from StartDate to EndDate
    public ArrayList<Habit> insertHabitStartToEndReturnArrayList(String nameHabit, byte[] icon, String timeStart, String timeEnd, String description, String unit, long goal, String reminder, String date) {
        ArrayList<Habit> arrayList = new ArrayList<>();

        String timeStartInput = timeStart;
        String timeEndInput = timeEnd;
        int[] timeStartArray = getDateMonthYearfromInput(timeStartInput);
        int[] timeEndArray = getDateMonthYearfromInput(timeEndInput);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(timeEndArray[2], timeEndArray[1], timeEndArray[0]);

        long daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());

        int curId = 0;

        Cursor cursor = GetData("SELECT rowid from Habit order by ROWID DESC limit 1");
        if (cursor.moveToNext()){
            curId = cursor.getInt(0);
        }

        while (daysBetween >= 0) {
//                timeStartInput = setStandardDateFormat(calendarStart.getTime());
            timeStartInput = setStandardDateFormat(calendarStart);
//                Toast.makeText( timeStartInput, Toast.LENGTH_SHORT).show();
            curId++;
            arrayList.add(new Habit(curId, nameHabit, icon, timeStart, timeEnd, description, unit, goal, reminder, timeStartInput));

            calendarStart.add(Calendar.DAY_OF_MONTH, +1);
//            calendarEnd.add(Calendar.DAY_OF_MONTH, +1);
            daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());
        }

        return arrayList;
    }


    // get date, month, year
    public int[] getDateMonthYearfromInput(String inputTime){

        String[] start = inputTime.split("/");
        int day = Integer.parseInt(start[0]);
        int month = Integer.parseInt(start[1]);
        int year = Integer.parseInt(start[2]);
        int[] array = {day, month, year};
        return array;
    }
//    // insert habit Name & Image
//    public void insertHabit(String nameHabit, byte[] icon) {
//        SQLiteDatabase database = getWritableDatabase();
//        String sql = "INSERT INTO Habit (Id, Name, Icon) VALUES(null, ?, ?)";
//        SQLiteStatement statement = database.compileStatement(sql); // vi luu hinh anh nen phai dung statement de biet
//        statement.bindString(1, nameHabit);
//        statement.bindBlob(2, icon);
//        statement.executeInsert();
//    }

    // delete a habit
    public void deleteHabit(int id){
        String nameHabit = getHabitNameById(id);
        QueryData("DELETE FROM Habit WHERE Name = '"+nameHabit+"'");
    }

    // get name by id
    public String getHabitNameById(int id){
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Id = '"+id+"'");
        if (cursor.moveToNext()){
            return cursor.getString(1);
        }
        return "";
    }

    // update a habit
    public void updateHabit(Habit habit){
        int id = habit.getIdHabit();
        String nameHabit = habit.getNameHabit();
        byte[] icon = habit.getImageHabit();
        String timeStart = habit.getTimeStart();
        String timeEnd = habit.getTimeEnd();
        String description = habit.getDescription();
        String unit = habit.getUnit();
        long goal = habit.getGoal();
        long done = habit.getDone();
        String reminder = habit.getReminder();

        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE Habit SET Name = ?, Icon = ?, TimeStart = ?, TimeEnd = ?, Description = ?, Unit = ?, Goal = ?, Reminder = ? WHERE Id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();

        statement.bindString(1, nameHabit);
        statement.bindBlob(2, icon);
        statement.bindString(3, timeStart);
        statement.bindString(4, timeEnd);
        statement.bindString(5, description);
        statement.bindString(6, unit);
        statement.bindLong(7, goal);
        statement.bindString(8, reminder);
        statement.bindLong(9, (long) id);

        statement.executeUpdateDelete();
    }

    // update a habit
    public void updateHabitTestDone(Habit habit){
        int id = habit.getIdHabit();
        String nameHabit = habit.getNameHabit();
        byte[] icon = habit.getImageHabit();
        String timeStart = habit.getTimeStart();
        String timeEnd = habit.getTimeEnd();
        String description = habit.getDescription();
        String unit = habit.getUnit();
        long goal = habit.getGoal();
        long done = habit.getDone();
        String reminder = habit.getReminder();

        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE Habit SET Name = ?, Icon = ?, TimeStart = ?, TimeEnd = ?, Description = ?, Unit = ?, Goal = ?, Reminder = ?, Done = ? WHERE Id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();

        statement.bindString(1, nameHabit);
        statement.bindBlob(2, icon);
        statement.bindString(3, timeStart);
        statement.bindString(4, timeEnd);
        statement.bindString(5, description);
        statement.bindString(6, unit);
        statement.bindLong(7, goal);
        statement.bindString(8, reminder);
        statement.bindLong(9, done);
        statement.bindLong(10, (long) id);


        statement.executeUpdateDelete();
    }


//    // insert list habit from startdate to endDate
//    public void updateHabitAll(Habit habit, String oldName, String oldDate) {
//        SQLiteDatabase database = getWritableDatabase();
//
//        String sql = "UPDATE Habit SET Name = ?, Icon = ?, TimeStart = ?, TimeEnd = ?, Description = ?, Unit = ?, Goal = ?, Reminder = ?, Date = ?, Done = ? WHERE Name = ? AND Date = ?";
//
////        String sql = "INSERT INTO Habit (Id, Name, Icon, TimeStart, TimeEnd, Description, Unit, Goal, Reminder, Date) VALUES(null, ?, ? ,?, ?, ?, ?, ?, ?, ?)";
//        SQLiteStatement statement = database.compileStatement(sql); // vi luu hinh anh nen phai dung statement de biet
//
//
//
//        String timeStartInput = habit.getTimeStart();
//        String timeEndInput = habit.getTimeEnd();
//        int[] timeStartArray = getDateMonthYearfromInput(timeStartInput);
//        int[] timeEndArray = getDateMonthYearfromInput(timeEndInput);
//
//        Calendar calendarStart = Calendar.getInstance();
//        calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);
//
//        Calendar calendarEnd = Calendar.getInstance();
//        calendarEnd.set(timeEndArray[2], timeEndArray[1], timeEndArray[0]);
//
//        long daysBetween = TimeUnit.MILLISECONDS.toDays(calendarEnd.getTime().getTime() - calendarStart.getTime().getTime());
//
//        while (daysBetween >= 0){
////            timeStartInput = setStandardDateFormat(calendarStart.getTime());
//
//            timeStartInput = setStandardDateFormat(calendarStart);
//            /// hàm setStandartDateFormat có vấn đề
//
//
//
//
//            statement.bindString(1, habit.getNameHabit());
//            statement.bindBlob(2, habit.getImageHabit());
//            statement.bindString(3, habit.getTimeStart());
//            statement.bindString(4, habit.getTimeEnd());
//            statement.bindString(5, habit.getDescription());
//            statement.bindString(6, habit.getUnit());
//            statement.bindLong(7, habit.getGoal());
//            statement.bindString(8, habit.getReminder());
//            statement.bindLong(9, habit.getDone());
//            statement.bindString(10, habit.getDate());
//            statement.bindLong(11, (long) habit.getIdHabit());
//            statement.bindString(12, getHabitNameById(habit.getIdHabit()));
//            statement.executeInsert();
//
//            timeStartArray = getDateMonthYearfromInput(timeStartInput);
//            calendarStart.set(timeStartArray[2], timeStartArray[1], timeStartArray[0]);
//            calendarStart.add(Calendar.DAY_OF_MONTH, +1);
////            calendarEnd.add(Calendar.DAY_OF_MONTH, +1);
//            daysBetween--;
//
//
//        }
//
//    }

    // select list habit
    public ArrayList<Habit> getListHabitNameandIcon(){
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT Name, Icon FROM Habit GROUP BY Name");
        while (cursor!=null && cursor.moveToNext()){
            String nameHabit = cursor.getString(0);
            byte[] icon = cursor.getBlob(1);
            habitArrayList.add(new Habit(nameHabit, icon));
        }
        return habitArrayList;
    }

//    // select habit test
//    public Habit getHabit(){
//        Cursor cursor = GetData("SELECT * FROM Habit");
//
//        if (cursor!=null && cursor.moveToNext()){
//            int idHabit = cursor.getInt(0);
//            String nameHabit = cursor.getString(1);
//            byte[] icon = cursor.getBlob(2);
////            String timeStart = cursor.getString(3);
////            String timeEnd= cursor.getString(4);
////            String description= cursor.getString(5);
////            int isDone = cursor.getInt(6);
////            String unit= cursor.getString(7);
////            int goal = cursor.getInt(8);
////            int done = cursor.getInt(9);
////            String reminder = cursor.getString(10);
//            return new Habit(idHabit, nameHabit, icon);
//        }
//        return null;
//    }

    // get Habit by Name & DATE
    public Habit getHabitByNameAndDate(String nameInput, String dateInput){
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Name = '"+nameInput+"' AND Date = '"+dateInput+"'");
        if (cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);
            return new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date);
        }
        return null;
    }

    // get a Habit by Name & TimeStart
    public Habit getHabitByNameAndTimeStart(String nameInput, String startTimeInput){
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Name = '"+nameInput+"' AND TimeStart = '"+startTimeInput+"'");
        if (cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);
            return new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date);
        }
        return null;
    }


    //select list habit today
    public ArrayList<Habit> getListHabitbyDate(String specificDate){
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Date = '"+specificDate+"'");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }

    //select list habit today
    public ArrayList<Habit> getListAllHabit(){
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }

    public ArrayList<Habit> getListHabitToday(){
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Date = '"+today+"'");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }


    public ArrayList<Habit> getListAllHabitInRangeOfDate(String dateStart, String dateEnd){
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Date BETWEEN '"+dateStart+"' AND '"+dateEnd+"'");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }

    public ArrayList<Habit> getlistOneHabitByNameInRangeOfDate(String nameHabitInput, String dateStart, String dateEnd){
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Name = '"+nameHabitInput+"' AND Date BETWEEN '"+dateStart+"' AND '"+dateEnd+"'");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }


    public ArrayList<String> getListGroupByHabitInRangeOfDate(String dateStart, String dateEnd){
        ArrayList<String> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT Name FROM Habit WHERE Date BETWEEN '"+dateStart+"' AND '"+dateEnd+"' GROUP BY Name");
        while (cursor!=null && cursor.moveToNext()){
            String nameHabit = cursor.getString(0);
            habitArrayList.add(nameHabit);
        }
        return habitArrayList;
    }


    public ArrayList<Habit> getListSpecificHabitbyMonthAndYear(String monthYear, String nameHabitInput){ // monthYear 6/2022
        String monthYearPattern = "%/"+monthYear;
        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Date LIKE '"+monthYearPattern+"' AND Name = '"+nameHabitInput+"'");
        while (cursor!=null && cursor.moveToNext()){
            int idHabit = cursor.getInt(0);
            String nameHabit = cursor.getString(1);
            byte[] icon = cursor.getBlob(2);
            String timeStart = cursor.getString(3);
            String timeEnd= cursor.getString(4);
            String description= cursor.getString(5);
            String unit= cursor.getString(6);
            int goal = cursor.getInt(7);
            int done = cursor.getInt(8);
            String reminder = cursor.getString(9);
            String date = cursor.getString(10);

            habitArrayList.add(new Habit(idHabit, nameHabit, icon, timeStart, timeEnd, description, unit, goal, done, reminder, date));

        }
        return habitArrayList;
    }

    public ArrayList<MonthlyStatistic> getListMonthlyStaticByMonthAndYear(String monthYear){
        ArrayList<MonthlyStatistic> monthlyStatisticArrayList = new ArrayList<>();
        String monthYearPattern = "%/"+monthYear;
        Cursor cursor = GetData("SELECT Name FROM Habit WHERE Date LIKE '"+monthYearPattern+"' GROUP BY Name");
        while (cursor!=null && cursor.moveToNext()){
            String nameHabit = cursor.getString(0);
            monthlyStatisticArrayList.add(new MonthlyStatistic(nameHabit, monthYear));
        }
        return monthlyStatisticArrayList;

    }

    //----------------- get danhsach habit con han
    public void insertListNameAndStartTimeGroupByNameHabitOnGoing(){
        Calendar calendar = Calendar.getInstance();
//        String today = setStandardDateFormat(calendar.getTime());
        String today = setStandardDateFormat(calendar);

        ArrayList<Habit> habitArrayList = new ArrayList<>();
        Cursor cursor = GetData("SELECT Name, TimeStart FROM Habit WHERE TimeEnd >= '"+today+"' GROUP BY Name");
        while (cursor!=null && cursor.moveToNext()){
            String nameHabit = cursor.getString(0);
            String timeStart = cursor.getString(1);
            habitArrayList.add(new Habit(nameHabit, timeStart));

            insertHabitDateNotExistInTable( nameHabit, timeStart);
        }
    }

    // insert nhung ngay habit ma con thieu trong table cho den hom nay
    public void insertHabitDateNotExistInTable(String nameHabitInput, String timeStartInput){
        Habit habit = getHabitByNameAndTimeStart(nameHabitInput, timeStartInput);

        String start[] = timeStartInput.split("/");
        int day = Integer.parseInt(start[0]);
        int month = Integer.parseInt(start[1]);
        int year = Integer.parseInt(start[2]);
        Calendar startDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        startDate.set(year, month, day);

        long distance = TimeUnit.MILLISECONDS.toDays(today.getTime().getTime() - startDate.getTime().getTime());

        while(distance >=0){
//            String date = setStandardDateFormat(startDate.getTime());
            String date = setStandardDateFormat(startDate);
            if (!isHabitDayExisted(nameHabitInput, timeStartInput)){

                    insertHabit(habit.getNameHabit(), habit.getImageHabit(), habit.getTimeStart(),
                            habit.getTimeEnd(), habit.getDescription(), habit.getUnit(), habit.getGoal(), habit.getReminder(), date);

            }
            distance--;
            startDate.add(Calendar.DAY_OF_MONTH, +1);
        }

    }

    public boolean isHabitDayExisted(String nameHabitInput, String date){
        Cursor cursor = GetData("SELECT * FROM Habit WHERE Name = '"+nameHabitInput+"' AND Date = '"+date+"'");
        if (cursor != null && cursor.moveToNext()){
            return true;
        }
        return false;
    }

    private String setStandardDateFormat1(Date date){
        // Date: tính 7 là tháng 8
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
        return simpleDateFormat.format(date);
    }

    private String setStandardDateFormat(Calendar calendar){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
        return calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
    }


    //----------------- get danhsach habit con han -----------------






}