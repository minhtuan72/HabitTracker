package com.example.svmc_habit_tracker.data.model;

import java.io.Serializable;

public class Habit implements Serializable {
    // id, ten, anh, timeStart, timeEnd, descriptopn, isDone, curStreak, longestStreak
    private int idHabit;
    private String nameHabit;
    private byte[] imageHabit;
    private String timeStart, timeEnd, description;
    private String unit;
    private long goal;
    private long done;
    private String reminder;
    private String date;
//    private int curStreak, longestStreak;


    public Habit(String nameHabit, byte[] imageHabit) {
        this.nameHabit = nameHabit;
        this.imageHabit = imageHabit;
    }

    public Habit(int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal, long done, String reminder, String date) {
        this.idHabit = idHabit;
        this.nameHabit = nameHabit;
        this.imageHabit = imageHabit;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.description = description;
        this.unit = unit;
        this.goal = goal;
        this.done = done;
        this.reminder = reminder;
        this.date = date;
    }

    public Habit(int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal, String reminder) {
        this.idHabit = idHabit;
        this.nameHabit = nameHabit;
        this.imageHabit = imageHabit;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.description = description;
        this.unit = unit;
        this.goal = goal;
        this.reminder = reminder;
    }

    public Habit(int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal, String reminder, String date) {
        this.idHabit = idHabit;
        this.nameHabit = nameHabit;
        this.imageHabit = imageHabit;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.description = description;
        this.unit = unit;
        this.goal = goal;
        this.reminder = reminder;
        this.date = date;
    }

    public Habit(int idHabit, String nameHabit, byte[] imageHabit) {
        this.idHabit = idHabit;
        this.nameHabit = nameHabit;
        this.imageHabit = imageHabit;
    }

    public Habit(String nameHabit, String timeStart) {
        this.nameHabit = nameHabit;
        this.timeStart = timeStart;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdHabit() {
        return idHabit;
    }

    public void setIdHabit(int idHabit) {
        this.idHabit = idHabit;
    }

    public String getNameHabit() {
        return nameHabit;
    }

    public void setNameHabit(String nameHabit) {
        this.nameHabit = nameHabit;
    }

    public byte[] getImageHabit() {
        return imageHabit;
    }

    public void setImageHabit(byte[] imageHabit) {
        this.imageHabit = imageHabit;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public long getDone() {
        return done;
    }

    public void setDone(long done) {
        this.done = done;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}