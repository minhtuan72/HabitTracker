package com.example.svmc_habit_tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;

import java.util.ArrayList;

public class DlogIconsRVAdapter extends RecyclerView.Adapter<DlogIconsRVAdapter.ViewHolder> {
    private Context context;
    ArrayList<Integer> iconList = new ArrayList<>();
    private OnItemDialogIconClickListener onItemDialogIconClickListener;


    public void setOnItemDialogIconClickListener(OnItemDialogIconClickListener onItemDialogIconClickListener) {
        this.onItemDialogIconClickListener = onItemDialogIconClickListener;
    }

    public DlogIconsRVAdapter(Context context) {
        this.context = context;
        this.iconList = new ArrayList<>();
        iconList.add(R.drawable.idea);
        iconList.add(R.drawable.learning);
        iconList.add(R.drawable.img);
        iconList.add(R.drawable.struggle);
        iconList.add(R.drawable.team);
        iconList.add(R.drawable.trust);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dialog_icons_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int img = iconList.get(position);
        holder.imageView.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public int getIcon(int position) {
        return iconList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.irv_dlogIBox_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemDialogIconClickListener != null){
                onItemDialogIconClickListener.OnItemClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemDialogIconClickListener{
        public void OnItemClick(int position);
    }
}
