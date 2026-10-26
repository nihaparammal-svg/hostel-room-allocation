package com.hostel.roomallocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.models.ActivityItem;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final Context context;
    private final List<ActivityItem> activityList;

    public ActivityAdapter(Context context, List<ActivityItem> activityList) {
        this.context = context;
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem item = activityList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getDescription());
        holder.tvTime.setText(item.getTimestamp());

        int colorRes = item.getStatusColor() != 0 ? item.getStatusColor() : R.color.primary;
        holder.vStatusDot.setBackgroundColor(ContextCompat.getColor(context, colorRes));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        View vStatusDot;
        TextView tvTitle, tvDesc, tvTime;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            vStatusDot = itemView.findViewById(R.id.vStatusDot);
            tvTitle = itemView.findViewById(R.id.tvActivityTitle);
            tvDesc = itemView.findViewById(R.id.tvActivityDesc);
            tvTime = itemView.findViewById(R.id.tvActivityTime);
        }
    }
}
