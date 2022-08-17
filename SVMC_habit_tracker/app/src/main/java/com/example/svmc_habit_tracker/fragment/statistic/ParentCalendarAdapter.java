package com.example.svmc_habit_tracker.fragment.statistic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.util.ArrayList;
import java.util.Calendar;

public class ParentCalendarAdapter extends RecyclerView.Adapter<ParentCalendarAdapter.ParentViewHolder> {

    private CalendarAdapter subCalendarAdapter;
    private ArrayList<MonthlyStatistic> monthlyStatistics = new ArrayList<>();
    private ArrayList<String> subDaysOfmonth = new ArrayList<>();
    private Context context;
    private Database database; //

    public ParentCalendarAdapter(Context context, CalendarAdapter subCalendarAdapter) {
        this.subCalendarAdapter = subCalendarAdapter;
        this.context = context;
        this.monthlyStatistics = new ArrayList<>();
        this.subDaysOfmonth = new ArrayList<>();
        this.database = new Database(context);
    }

    public ParentCalendarAdapter(Context context) {
        this.monthlyStatistics = new ArrayList<>();
        this.subDaysOfmonth = new ArrayList<>();
        this.context = context;
        subCalendarAdapter = new CalendarAdapter(context);
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_fragmonthly, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        MonthlyStatistic monthlyStatistic = monthlyStatistics.get(position);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 7);

        holder.subRecyclerView.setLayoutManager(gridLayoutManager);
        subCalendarAdapter = new CalendarAdapter(holder.subRecyclerView.getContext());
        holder.subRecyclerView.setAdapter(subCalendarAdapter);
        subCalendarAdapter.setDaysOfMonth(subDaysOfmonth);
//        subCalendarAdapter.notifyDataSetChanged();

        // set specifict habit
        String nameHabit = monthlyStatistic.getNameHabit();
        String monthYear = monthlyStatistic.getMonthYear();
        ArrayList<Habit> specificHabit = database.getListSpecificHabitbyMonthAndYear(monthYear, nameHabit);
        subCalendarAdapter.setSpecificHabitList(specificHabit);
        subCalendarAdapter.setMonthAndYear(monthYear);

        // tinh tong so ngay hoan thanh
        int total = 0;
        if (specificHabit != null){
            for (Habit i:specificHabit)
                if (i.getDone()>=i.getGoal())
                    total++;
        }
        int daysInMonth = getMonthFromMonthYear(monthYear);
        double percent = Math.ceil(((double) total/ (double) daysInMonth) * 1000)/10;
        holder.tvPercentage.setText(percent+"%");
        holder.tvTotal.setText(total+"");
        holder.tvTitleDashboard.setText(monthlyStatistic.getNameHabit());
    }

    public int getMonthFromMonthYear(String monthYear){
        String[] sArray = monthYear.split("/");
        int month = Integer.parseInt(sArray[0]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int dayofmonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return  dayofmonth;
    }

    @Override
    public int getItemCount() {
        return monthlyStatistics.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setParentAndSubList(ArrayList<MonthlyStatistic> mainList, ArrayList<String> subList) {
        if (subList!= null)
            try {
                subDaysOfmonth.clear();
                subDaysOfmonth.addAll(subList);
                subCalendarAdapter.setDaysOfMonth(subList);
                notifyDataSetChanged();
                subCalendarAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        if (mainList != null){
            monthlyStatistics.clear();
            monthlyStatistics.addAll(mainList);
            notifyDataSetChanged();
        }


    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        RecyclerView subRecyclerView;
        TextView tvTitleDashboard, tvTotal, tvPercentage;
        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.irv_fragmonthly_trecycle_view);
            tvTitleDashboard = itemView.findViewById(R.id.irv_fragmonthly_title_dashboard);
            tvTotal = itemView.findViewById(R.id.irv_fragmonthly_total_day);
            tvPercentage = itemView.findViewById(R.id.irv_fragmonthly_percent_done);
        }
    }

    public interface OnItemParentCalendarClickListener{
        void OnItemClick(int position);
    }
}
