package com.example.svmc_habit_tracker.fragment.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    // chỉ hiển thị số. Chưa biết habit nào??? → cần chuyền vào tên habit để lấy dữ liệu từ database

    private Context context;
    private ArrayList<String> daysOfMonth = new ArrayList<>();
    private String nameHabit, monthAndYear;
    private ArrayList<Habit> specificHabitList = new ArrayList<>();
    private Database database;


    private OnItemCalendarMonthlyListener onItemCalendarMonthlyListener;

    public void setOnItemCalendarMonthlyListener(OnItemCalendarMonthlyListener onItemCalendarMonthlyListener) {
        this.onItemCalendarMonthlyListener = onItemCalendarMonthlyListener;
    }

    public CalendarAdapter(Context context, ArrayList<String> daysOfMonth, String nameHabit, String monthAndYear) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.nameHabit = nameHabit;
        this.monthAndYear = monthAndYear;
        this.specificHabitList = new ArrayList<>();
        this.database = new Database(context);
    }

    public CalendarAdapter(Context context) {
        this.context = context;
        this.daysOfMonth = new ArrayList<>();
        this.specificHabitList = new ArrayList<>();
        this.nameHabit = "";
        this.monthAndYear = "";
    }

    public CalendarAdapter(Context context, ArrayList<String> daysOfMonth) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.database = new Database(context);
        this.nameHabit = "";
        this.monthAndYear = "";
    }

    public void setDaysOfMonth(ArrayList<String> list) {
        daysOfMonth.clear();
        daysOfMonth.addAll(list);
        notifyDataSetChanged();
    }

    ///// ******** nho phai setSpecificHabitList de co data

    public void setSpecificHabitList(ArrayList<Habit> líst){
        specificHabitList.clear();
        specificHabitList.addAll(líst);
        notifyDataSetChanged();
    }

    public void setMonthAndYear(String monthYear){
        monthAndYear = monthYear;
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1);
        return new CalendarViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        String dayofmonth = daysOfMonth.get(position);
        String day = getDayofddMyyyy(dayofmonth, "day");
        int monthVar = Integer.parseInt(getDayofddMyyyy(dayofmonth, "month"));
        String year = getDayofddMyyyy(dayofmonth, "year");

        String s[] = monthAndYear.split("/");
        int monthMain = Integer.parseInt(s[0]);

        holder.dayOfMonth.setText(day);

//        Toast.makeText(context, "day 1: "+  daysOfMonth.get(0).toString(), Toast.LENGTH_SHORT).show();
//        holder.dayOfMonth.setText("30"); // list chỉ co ngay 20

        holder.progressBar.setProgress(0);
        holder.progressBar.setMax(100);

//        Toast.makeText(context, specificHabitList.size()+"", Toast.LENGTH_SHORT).show();

        if (monthVar != monthMain){
            holder.dayOfMonth.setVisibility(View.GONE);
//            holder.dayOfMonth.setBackground();
            holder.progressBar.setVisibility(View.GONE);
        }else{
            if (specificHabitList != null && specificHabitList.size() != 0) {
                for (Habit i : specificHabitList) {
//                String dayinfor = getDayofMonthFromDate(i.getDate());
                    if (i.getDate().equalsIgnoreCase(dayofmonth)) {
//                    Toast.makeText(context, "in loop calendaradapter monthly: "
//                            +"\nDate: "+ i.getDate()
//                            +"\nNgay cua thang: "+dayofmonth
//                            +"\nDone: "+ i.getDone()
//                            +"\nGoal: "+i.getGoal(), Toast.LENGTH_SHORT).show();

                        holder.progressBar.setProgress((int) i.getDone());
                        holder.progressBar.setMax((int) i.getGoal());
//                        holder.dayOfMonth.setBackgroundColor(R.color.teal_200);
//                        holder.progressBar.setVisibility(View.GONE);
//                    holder.progressBar.setMax(100);
                        break;
                    }
                }
            }
        }


        holder.dayOfMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context.getApplicationContext(), dayofmonth
//                        +"\nsize spe"+specificHabitList.get(0).getDate(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getItem(int position){
        return daysOfMonth.get(position);
    }

    public String getDayofddMyyyy(String standardDate, String x){
        String[] array = standardDate.split("/");
        String day = array[0];
        String month = array[1];
        String year = array[2];
        if (x.equals("day"))
            return day;
        else if (x.equals("month"))
            return month;
        else return year;
    }



    @Override
    public int getItemCount()
    {
        if (daysOfMonth != null) //////
            return daysOfMonth.size();
        else return 0;
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayOfMonth;
        ProgressBar progressBar;
        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            progressBar = itemView.findViewById(R.id.ir_progressbar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemCalendarMonthlyListener != null){
                onItemCalendarMonthlyListener.onItemClick(getAdapterPosition(), getItem(getAdapterPosition()));
            }
        }
    }

    public String getDayofMonthFromDate(String date){
        String s[] = date.split("/");
        return s[0];
    }

    public interface  OnItemCalendarMonthlyListener
    {
        void onItemClick(int position, String date);
    }
}

