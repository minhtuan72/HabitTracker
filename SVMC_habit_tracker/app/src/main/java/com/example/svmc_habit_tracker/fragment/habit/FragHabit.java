package com.example.svmc_habit_tracker.fragment.habit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.adapter.DlogIconsRVAdapter;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragHabit extends Fragment  implements RvCalendarAdapter.OnItemCalendarClickListener, HabitRvAdapter.OnHabitIconClickListener {
    private ArrayList<Habit> habitArrayListIcon = new ArrayList<>();
    private RecyclerView recyclerViewIconHabit;
    private HabitRvAdapter recyclerViewIconHabitAdapter;

    private TextView monthYear, title;
    private Button btnPrevious, btnNext;
    private RvCalendarAdapter rvCalendarAdapter;
    private RecyclerView recyclerViewCalendar;

    private Database database;
    private Calendar calendar;
    
    private final String TAG ="FragHabit";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.frag_habit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = new Database(getActivity());
        habitArrayListIcon = database.getListHabitNameandIcon();




        initIconRvView(view);

        calendar = Calendar.getInstance();

        initView(view);

        setmonthView(view);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMonth();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMonth();
            }
        });
    }

    // IconImage recyclerView
    private void initIconRvView(View view) {
        recyclerViewIconHabit = view.findViewById(R.id.frhabit_rvIcon);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);

        recyclerViewIconHabit.setLayoutManager(layoutManager);

        recyclerViewIconHabitAdapter = new HabitRvAdapter(getActivity());
        recyclerViewIconHabit.setAdapter(recyclerViewIconHabitAdapter);

        if (habitArrayListIcon != null)
            recyclerViewIconHabitAdapter.setHabitList(habitArrayListIcon);
        recyclerViewIconHabitAdapter.setOnHabitIconClickListener(this);
    }

    // CALENDAR recyclerView
    private void initView(View itemview) {
        title = itemview.findViewById(R.id.frhabit_title);
        monthYear = itemview.findViewById(R.id.frhabit_tvMonthYear);
        btnNext = itemview.findViewById(R.id.frhabit_btnNext);
        btnPrevious = itemview.findViewById(R.id.frhabit_btnPrevious);

        recyclerViewCalendar = itemview.findViewById(R.id.frhabit_rv);

        GridLayoutManager layoutManager = new GridLayoutManager(itemview.getContext(), 7);
        recyclerViewCalendar.setLayoutManager(layoutManager);


        rvCalendarAdapter = new RvCalendarAdapter(itemview.getContext());
//        rvCalendarAdapter.setDaysOfMonthList(daysInMonthArray(calendar));
        rvCalendarAdapter.setOnItemCalendarClickListener(this);

        recyclerViewCalendar.setAdapter(rvCalendarAdapter);
    }

    @SuppressLint("ResourceAsColor")
    private void setmonthView(View view) {
        Calendar currentCal = Calendar.getInstance();

//        DateFormat dateFormat = new SimpleDateFormat("MM");
//        Date date = new Date();
//
//        Toast.makeText(getContext(), dateFormat.format(date) +"\n"+ currentCal.get(Calendar.MONTH) + "\n" + calendar.get(Calendar.MONTH), Toast.LENGTH_SHORT).show();

        monthYear.setText(setformatMonthYear(calendar.getTime()));
        monthYear.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_shadow));

//        if (currentCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
//            monthYear.setBackgroundResource(R.drawable.today_bg_red);
//        else monthYear.setBackground(null);

        rvCalendarAdapter.setDaysOfMonthList(daysInMonthArray(calendar));
    }

    private String setformatMonthYear(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
        return simpleDateFormat.format(date);
    }

    private String setStandardDateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        return simpleDateFormat.format(date);
    }

    private ArrayList<String> daysInMonthArray(Calendar input){
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        int daysInMonth = input.getActualMaximum(Calendar.DAY_OF_MONTH); // tong so ngay trong thang

        Calendar calendar1 = input;
        calendar1.set(Calendar.DAY_OF_MONTH, 1); // ngay dau cua thang

        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK); // ngayf 1/5 là ngày thứ mấy trong tuần.
        calendar1.add(Calendar.DAY_OF_MONTH, -dayOfWeek+1);

        for (int i=1; i<43;i++){
            if (i<= dayOfWeek || i > dayOfWeek + daysInMonth){
//                daysInMonthArray.add("");
                daysInMonthArray.add(setStandardDateFormat(calendar1.getTime()));
                calendar1.add(Calendar.DAY_OF_MONTH, +1);
            }else{
//                daysInMonthArray.add(String.valueOf(i-dayOfWeek));
                daysInMonthArray.add(setStandardDateFormat(calendar1.getTime())); // 2022/1/2
//                daysInMonthArray.add(setStandardDateFormat(calendar1.getTime())); // Object Calendar
                calendar1.add(Calendar.DAY_OF_MONTH, +1); // vong lap cuoi se chuyen sang thang moi → previous month -2
            }
        }
        calendar1.add(Calendar.MONTH, -1);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);

        return daysInMonthArray;
    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        setmonthView(getView());
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, +1);
        setmonthView(getView());
    }

    @Override
    public void onItemClick(int position, String daytext, String dayStandard) {

//        Toast.makeText(getContext(), "Selected Date " + daytext + " dayStandard: "+ dayStandard + " " + setStandardDateFormat(calendar.getTime()), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), SelectMealActivity.class);
//        intent.putExtra("date", dayStandard);
//        startActivity(intent);
    }

    @Override
    public void onItemHabitClick(int position) {
//        Toast.makeText(getContext(), recyclerViewIconHabitAdapter.getNameHabit(position), Toast.LENGTH_SHORT).show();
        String nameHabit = recyclerViewIconHabitAdapter.getNameHabit(position);

        title.setText(nameHabit);


//        rvCalendarAdapter.setSpecificArrayList(database.get);
        ArrayList<String> test = daysInMonthArray(calendar);
//        Toast.makeText(getContext(), test.get(0), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), test.get(test.size()-1), Toast.LENGTH_SHORT).show();

        setHabitListToCalendar(test, nameHabit);
    }

    

    public void setHabitListToCalendar(ArrayList<String> daysInMonthList, String nameHabit){
        String startDate = rvCalendarAdapter.standardDateYYYYMddToddMYYYY(daysInMonthList.get(0));
        String endDate = rvCalendarAdapter.standardDateYYYYMddToddMYYYY(daysInMonthList.get(daysInMonthList.size()-1));

//        Toast.makeText(getContext(), "startDate: "+ startDate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "endDate: "+ endDate, Toast.LENGTH_SHORT).show();

        ArrayList<Habit> monthlyHabit = database.getlistOneHabitByNameInRangeOfDate(nameHabit, startDate, endDate);
//        Toast.makeText(getContext(), "monthlyHabit: "+monthlyHabit.size()+
//                "\nDone: "+monthlyHabit.get(0).getDone()
//                + "\nDate: "+monthlyHabit.get(0).getDate(), Toast.LENGTH_SHORT).show();
        // format DATE to match with TABLE in DB
        rvCalendarAdapter.setSpecificArrayList(monthlyHabit);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        rvCalendarAdapter.setDaysOfMonthList(daysInMonthArray(calendar));
        habitArrayListIcon = database.getListHabitNameandIcon();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

}

