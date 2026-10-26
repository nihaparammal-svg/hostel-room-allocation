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
import com.hostel.roomallocation.models.LeaveRequest;

import java.util.List;

public class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.LeaveViewHolder> {

    private final Context context;
    private final List<LeaveRequest> leaveList;

    public LeaveHistoryAdapter(Context context, List<LeaveRequest> leaveList) {
        this.context = context;
        this.leaveList = leaveList;
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leave_history, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder holder, int position) {
        LeaveRequest req = leaveList.get(position);
        holder.tvType.setText(req.getLeaveType());
        holder.tvDates.setText(req.getFromDate() + " → " + req.getToDate());
        holder.tvReason.setText(req.getReason());

        String status = req.getStatus();
        holder.tvStatus.setText(status);
        if ("Approved".equalsIgnoreCase(status)) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_available);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.success));
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_partially);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.warning));
        }
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public void addLeave(LeaveRequest request) {
        leaveList.add(0, request);
        notifyItemInserted(0);
    }

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvDates, tvReason, tvStatus;

        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvLeaveItemType);
            tvDates = itemView.findViewById(R.id.tvLeaveItemDates);
            tvReason = itemView.findViewById(R.id.tvLeaveItemReason);
            tvStatus = itemView.findViewById(R.id.tvLeaveItemStatus);
        }
    }
}
