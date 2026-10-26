package com.hostel.roomallocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    public interface OnRoomApplyClickListener {
        void onApply(Room room);
    }

    private final Context context;
    private final List<Room> allRooms;
    private final List<Room> displayedRooms;
    private final OnRoomApplyClickListener listener;

    public RoomAdapter(Context context, List<Room> rooms, OnRoomApplyClickListener listener) {
        this.context = context;
        this.allRooms = new ArrayList<>(rooms);
        this.displayedRooms = new ArrayList<>(rooms);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = displayedRooms.get(position);

        holder.tvRoomNumber.setText(room.getRoomNumber());
        holder.tvRoomBlockFloor.setText(room.getBlock() + " • " + room.getFloor());
        holder.tvRoomType.setText(room.getType());
        holder.tvRoomAvailableBeds.setText(room.getAvailableBeds() + " of " + room.getCapacity() + " Beds");
        holder.tvRoomPrice.setText(room.getPrice());

        // Status badge styling
        String status = room.getStatus();
        holder.tvRoomStatusBadge.setText(status);

        if ("Available".equalsIgnoreCase(status)) {
            holder.tvRoomStatusBadge.setBackgroundResource(R.drawable.bg_badge_available);
            holder.tvRoomStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.success));
            holder.btnApplyRoom.setEnabled(true);
            holder.btnApplyRoom.setText("Apply Now");
        } else if ("Partially Occupied".equalsIgnoreCase(status)) {
            holder.tvRoomStatusBadge.setBackgroundResource(R.drawable.bg_badge_partially);
            holder.tvRoomStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.warning));
            holder.btnApplyRoom.setEnabled(true);
            holder.btnApplyRoom.setText("Apply Now");
        } else {
            holder.tvRoomStatusBadge.setBackgroundResource(R.drawable.bg_badge_full);
            holder.tvRoomStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.danger));
            holder.btnApplyRoom.setEnabled(false);
            holder.btnApplyRoom.setText("Room Full");
        }

        holder.btnApplyRoom.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApply(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedRooms.size();
    }

    public void updateData(List<Room> newRooms) {
        allRooms.clear();
        allRooms.addAll(newRooms);
        displayedRooms.clear();
        displayedRooms.addAll(newRooms);
        notifyDataSetChanged();
    }

    public void filter(String query, String blockFilter, boolean availableOnly) {
        displayedRooms.clear();
        String q = query.toLowerCase().trim();

        for (Room r : allRooms) {
            boolean matchesSearch = q.isEmpty() ||
                    r.getRoomNumber().toLowerCase().contains(q) ||
                    r.getBlock().toLowerCase().contains(q) ||
                    r.getType().toLowerCase().contains(q);

            boolean matchesBlock = blockFilter == null || blockFilter.isEmpty() ||
                    "All".equalsIgnoreCase(blockFilter) ||
                    r.getBlock().equalsIgnoreCase(blockFilter);

            boolean matchesAvail = !availableOnly || !"Full".equalsIgnoreCase(r.getStatus());

            if (matchesSearch && matchesBlock && matchesAvail) {
                displayedRooms.add(r);
            }
        }
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvRoomBlockFloor, tvRoomStatusBadge, tvRoomType, tvRoomAvailableBeds, tvRoomPrice;
        Button btnApplyRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvRoomBlockFloor = itemView.findViewById(R.id.tvRoomBlockFloor);
            tvRoomStatusBadge = itemView.findViewById(R.id.tvRoomStatusBadge);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomAvailableBeds = itemView.findViewById(R.id.tvRoomAvailableBeds);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            btnApplyRoom = itemView.findViewById(R.id.btnApplyRoom);
        }
    }
}
