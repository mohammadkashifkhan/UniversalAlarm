package com.mdkashif.alarm.time.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mdkashif.alarm.R;

import java.util.List;

/**
 * Created by Kashif on 24-Apr-18.
 */
public class DaysInWeekAdapter extends RecyclerView.Adapter<DaysInWeekAdapter.DaysListViewHolder>{
    List<String> days;
    private int lastPosition = 0;
    private Context context;

    public DaysInWeekAdapter(List<String> days) {
        this.days=days;
    }

    public class DaysListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView day;

        public DaysListViewHolder(View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.time_slot);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            notifyItemChanged(lastPosition);
            lastPosition = getAdapterPosition();
            notifyItemChanged(lastPosition);
        }
    }

    @NonNull
    @Override
    public DaysListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_of_week_item, parent, false);
        context=parent.getContext();
        return new DaysListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysListViewHolder holder, int position) {
        holder.day.setText(days.get(position));

        // set day slot background as blue if selected else least blue
        holder.day.setBackgroundResource(lastPosition == position ? R.drawable.day_selected_item :
                R.drawable.day_unselected_item);
        holder.day.setTextColor(lastPosition == position ? context.getResources().getColor(R.color.dark_blue):
                context.getResources().getColor(R.color.lesser_dark_blue));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
