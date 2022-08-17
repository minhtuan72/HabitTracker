package com.example.svmc_habit_tracker.fragment.statistic;

public class MonthlyStatistic {
    private String nameHabit, monthYear;

    public MonthlyStatistic(String nameHabit, String monthYear) {
        this.nameHabit = nameHabit;
        this.monthYear = monthYear;
    }

    public String getNameHabit() {
        return nameHabit;
    }

    public void setNameHabit(String nameHabit) {
        this.nameHabit = nameHabit;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}