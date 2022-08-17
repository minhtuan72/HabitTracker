package com.example.svmc_habit_tracker.fragment.statistic;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class WeeklyFragment extends Fragment {

    private String fisrtOfWeek, endOfWeek;

    private TextView dayStart;
    private TextView dayEnd;
    private TextView rangeDay;
    private LocalDate selectedDate;
    private TableLayout tableLayout;
    private final String[] daysOfWeek =  {"M", "T", "W", "T", "F", "S", "S"};
    private String[] nameHabits = {"Finance", "Journal", "100 days of Python"};
    private ArrayList<String> nameHabitListsOfWeek = new ArrayList<>();
    private ArrayList<Habit> specificHabitList = new ArrayList<>();
    private ArrayList<LocalDate> days = new ArrayList<>();
    private Database database;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weekly, container, false);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /// danh sach habits tu ngay startdate -> endDate
        database = new Database(getActivity());
        selectedDate = LocalDate.now();
//        dayStart = view.findViewById(R.id.dayStart);
//        dayEnd = view.findViewById(R.id.dayEnd);
        rangeDay = view.findViewById(R.id.range_day);

        tableLayout = view.findViewById(R.id.table_layout);

        // set dayStart and dayEnd
        daysInWeekArray(selectedDate);

        // Button change previous weeks
        Button prevWeek = view.findViewById(R.id.prev_week_button);
        prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusWeeks(1);
                daysInWeekArray(selectedDate);
                createTable();
            }
        });

        // Button change next weeks
        Button nextWeek = view.findViewById(R.id.next_week_button);
        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusWeeks(1);
                daysInWeekArray(selectedDate);
                createTable();
            }
        });

        createColumn();


        createTable();



    }

    private void createColumn() {
        // Add data into table
        TableRow nameCol = new TableRow(getContext());
        TextView col1 = new TextView(getContext());
        nameCol.addView(col1);
        // Hiện thứ M T W T F S S
        for (int i = 0; i < daysOfWeek.length; i++) {
            TextView dayWeek = new TextView(getContext());
            dayWeek.setText(daysOfWeek[i]);
            dayWeek.setGravity(Gravity.CENTER);
            nameCol.addView(dayWeek);
        }
        tableLayout.addView(nameCol);
    }

    private void cleanTable(TableLayout tableLayout){
        int child = tableLayout.getChildCount();
        if (child>1){
            tableLayout.removeViews(1, child-1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createTable() {

        cleanTable(tableLayout);



        // Hiện hang ngang - Tên habit
        for (int i = 0; i < nameHabitListsOfWeek.size(); i++) {
            String nameHabit = nameHabitListsOfWeek.get(i);
            specificHabitList.clear();
            specificHabitList.addAll(database.getlistOneHabitByNameInRangeOfDate(nameHabit, fisrtOfWeek, endOfWeek));
            TableRow rowValue = new TableRow(getContext());
            TextView name = new TextView(getContext());
            name.setText(nameHabit);
            name.setGravity(Gravity.CENTER);
            name.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_shadow_5));

            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
            name.setLayoutParams(params);
            if (nameHabit.length() > 10) {
                name.setMinLines(nameHabit.length() / 10 + 2);
            }

            rowValue.addView(name);
            for (LocalDate j:days) {
                // get habit by DATE and HABITNAME
                Habit habit = database.getHabitByNameAndDate(nameHabit, formatDate(j));

                CheckBox checkBox = new CheckBox(getContext());
//                checkBox.setScaleX((float) 0.7);
//                checkBox.setScaleY((float) 0.7);
                TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                checkBox.setLayoutParams(params1);
                checkBox.setGravity(Gravity.CENTER);

                // check habit done
                if (habit != null && habit.getDone() >= habit.getGoal()) {
                    checkBox.setChecked(true);
                }
                rowValue.addView(checkBox);
            }

//            for (int j = 0; j < 7; j++) {
//                CheckBox checkBox = new CheckBox(getContext());
//                checkBox.setScaleX((float) 0.7);
//                checkBox.setScaleY((float) 0.7);
//                if (j == 1) {
//                    checkBox.setChecked(true);
//                }
//                rowValue.addView(checkBox);
//            }
            tableLayout.addView(rowValue);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void daysInWeekArray(LocalDate date) {
        days = new ArrayList<>();
        LocalDate current = sundayForDate(date);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
//        dayStart.setText(dayMonthFromDate(days.get(0)));
//        dayEnd.setText(dayMonthFromDate(days.get(days.size() - 1)));
        rangeDay.setText(dayMonthFromDate(days.get(0)) + " ~ " + dayMonthFromDate(days.get(days.size() - 1)));

        fisrtOfWeek = formatDate(days.get(0));
        endOfWeek = formatDate(days.get(days.size()-1));

        // set  habit name list
        nameHabitListsOfWeek = database.getListGroupByHabitInRangeOfDate(fisrtOfWeek, endOfWeek);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.MONDAY) {
                return current;
            }
            current = current.minusDays(1);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String dayMonthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return date.format(formatter);
    }


}