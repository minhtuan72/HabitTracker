package com.example.svmc_habit_tracker.fragment.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthlyFragmentNew extends Fragment {
    private Button fBtnNext, fBtnPrevious;
    private TextView fTvMonthYear;

    private RecyclerView fRecyclerViewParent;
    private ParentCalendarAdapter rvParentCalendarAdapter;

    private CalendarAdapter rvSubcalendarAdapter;

    private ArrayList<MonthlyStatistic> monthlyStatisticsParent;
    private ArrayList<String> daysOfMonth;
    private Database database;



    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_rv, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // anh xa
        initView(view);

        setmonthView();


        fBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMonth();
            }
        });

        fBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMonth();
            }
        });

    }

    private void initView(View view) {

        monthlyStatisticsParent = new ArrayList<>();
        daysOfMonth = new ArrayList<>();
        database = new Database(getActivity());
        calendar = Calendar.getInstance();
        String dayofmonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        monthlyStatisticsParent.clear();
        monthlyStatisticsParent = database.getListMonthlyStaticByMonthAndYear(setMonthYearFormat(calendar));
//        ArrayList<MonthlyStatistic> monthlyStatistics = database.get

//        monthlyStatisticsParent.addAll(database.getListMonthlyStaticByMonthAndYear("7/2022"));
      //  Toast.makeText(getActivity(), "monthly sau tai : "+monthlyStatisticsParent.size()+"", Toast.LENGTH_SHORT).show();

        // khoi tao calendar


        fBtnNext = view.findViewById(R.id.frMonthBtn_next);
        fBtnPrevious = view.findViewById(R.id.frMonthBtn_prev);
        fTvMonthYear = view.findViewById(R.id.frMonthTv_monthYear);

        fRecyclerViewParent = view.findViewById(R.id.frMonthRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        fRecyclerViewParent.setLayoutManager(gridLayoutManager);

        daysOfMonth = daysInMonthArray(calendar);

        rvSubcalendarAdapter = new CalendarAdapter(getActivity());
        rvParentCalendarAdapter = new ParentCalendarAdapter(getContext(), rvSubcalendarAdapter);

        fRecyclerViewParent.setAdapter(rvParentCalendarAdapter);
        ////// Chua truyen du lieu vao Adapter
    }

    private ArrayList<String> daysInMonthArray(Calendar input){
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        int daysInMonth = input.getActualMaximum(Calendar.DAY_OF_MONTH); // tong so ngay trong thang

        Calendar calendar1 = input;
        calendar1.set(Calendar.DAY_OF_MONTH, 1); // ngay dau cua thang
        int testMonth = calendar1.get(Calendar.MONTH);
       // Toast.makeText(getActivity(), "testMonth = "+ testMonth, Toast.LENGTH_SHORT).show();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK); // ngayf 1/5 là ngày thứ mấy trong tuần.
        calendar1.add(Calendar.DAY_OF_MONTH, -dayOfWeek+1);

        for (int i=1; i<43;i++){
            if (i<= dayOfWeek || i > dayOfWeek + daysInMonth){
//                daysInMonthArray.add("");
                daysInMonthArray.add(setStandardDateFormat(calendar1));
                calendar1.add(Calendar.DAY_OF_MONTH, +1);
            }else{
//                daysInMonthArray.add(String.valueOf(i-dayOfWeek));
                daysInMonthArray.add(setStandardDateFormat(calendar1)); // 2022/1/2
//                daysInMonthArray.add(setStandardDateFormat(calendar1.getTime())); // Object Calendar
                calendar1.add(Calendar.DAY_OF_MONTH, +1); // vong lap cuoi se chuyen sang thang moi → previous month -2
            }
        }
        calendar1.add(Calendar.MONTH, -1);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);

        return daysInMonthArray; // item co dang: d/M/yyyy
    }

    private String setStandardDateFormat(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        // return d/M/yyyy
    }

    private String setMonthYearFormat(Calendar calendar){
        return (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        // return M/yyyy
    }

    private String setformatMonthYear(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
        return simpleDateFormat.format(date);
    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        setmonthView();
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, +1);
        setmonthView();
    }

    @SuppressLint("ResourceAsColor")
    private void setmonthView() {
        Calendar currentCal = Calendar.getInstance();

//        DateFormat dateFormat = new SimpleDateFormat("MM");
//        Date date = new Date();
//
//        Toast.makeText(getContext(), dateFormat.format(date) +"\n"+ currentCal.get(Calendar.MONTH) + "\n" + calendar.get(Calendar.MONTH), Toast.LENGTH_SHORT).show();

        fTvMonthYear.setText(setformatMonthYear(calendar.getTime()));
        fTvMonthYear.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_shadow));
//        if (currentCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
//            fTvMonthYear.setBackgroundResource(R.drawable.today_bg_red);
//        else fTvMonthYear.setBackground(null);

        daysOfMonth = daysInMonthArray(calendar);
        monthlyStatisticsParent.clear();

        ArrayList<MonthlyStatistic> list = database.getListMonthlyStaticByMonthAndYear(setMonthYearFormat(calendar));
        if (list != null && daysOfMonth != null){
            monthlyStatisticsParent.addAll(list);

          //  Toast.makeText(getActivity(), "monthlylist: "+ monthlyStatisticsParent.size(), Toast.LENGTH_SHORT).show();

            //set list to adapter
            rvParentCalendarAdapter.setParentAndSubList(monthlyStatisticsParent, daysOfMonth);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}
