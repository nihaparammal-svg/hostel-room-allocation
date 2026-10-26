package com.hostel.roomallocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.adapters.RoomAdapter;
import com.hostel.roomallocation.models.Room;
import com.hostel.roomallocation.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class RoomAvailabilityActivity extends AppCompatActivity {

    private ImageView btnRoomsBack;
    private EditText etSearchRooms;
    private Button chipAll, chipBlockA, chipBlockB, chipAvailable;
    private TextView tvRoomsSummary;
    private RecyclerView rvRooms;

    private RoomAdapter roomAdapter;
    private List<Room> roomList = new ArrayList<>();

    private String currentBlockFilter = "All";
    private boolean isAvailableOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_availability);

        initViews();
        setupRecyclerView();
        setupFilters();
        fetchRooms();
    }

    private void initViews() {
        btnRoomsBack = findViewById(R.id.btnRoomsBack);
        etSearchRooms = findViewById(R.id.etSearchRooms);
        chipAll = findViewById(R.id.chipAll);
        chipBlockA = findViewById(R.id.chipBlockA);
        chipBlockB = findViewById(R.id.chipBlockB);
        chipAvailable = findViewById(R.id.chipAvailable);
        tvRoomsSummary = findViewById(R.id.tvRoomsSummary);
        rvRooms = findViewById(R.id.rvRooms);

        btnRoomsBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(this, roomList, room -> {
            // Apply Now clicked
            Intent intent = new Intent(RoomAvailabilityActivity.this, HostelApplicationActivity.class);
            intent.putExtra("PRESELECTED_ROOM", room.getRoomNumber());
            intent.putExtra("PRESELECTED_BLOCK", room.getBlock());
            intent.putExtra("PRESELECTED_TYPE", room.getType());
            startActivity(intent);
        });
        rvRooms.setAdapter(roomAdapter);
    }

    private void setupFilters() {
        etSearchRooms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyCurrentFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        chipAll.setOnClickListener(v -> {
            currentBlockFilter = "All";
            isAvailableOnly = false;
            updateChipStyles(chipAll);
            applyCurrentFilters();
        });

        chipBlockA.setOnClickListener(v -> {
            currentBlockFilter = "Block A";
            updateChipStyles(chipBlockA);
            applyCurrentFilters();
        });

        chipBlockB.setOnClickListener(v -> {
            currentBlockFilter = "Block B";
            updateChipStyles(chipBlockB);
            applyCurrentFilters();
        });

        chipAvailable.setOnClickListener(v -> {
            isAvailableOnly = !isAvailableOnly;
            if (isAvailableOnly) {
                chipAvailable.setBackgroundResource(R.drawable.bg_button_primary);
                chipAvailable.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                chipAvailable.setBackgroundResource(R.drawable.bg_button_outline);
                chipAvailable.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            }
            applyCurrentFilters();
        });
    }

    private void updateChipStyles(Button activeChip) {
        // Reset block chips
        Button[] blockChips = {chipAll, chipBlockA, chipBlockB};
        for (Button btn : blockChips) {
            btn.setBackgroundResource(R.drawable.bg_button_outline);
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }

        activeChip.setBackgroundResource(R.drawable.bg_button_primary);
        activeChip.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void fetchRooms() {
        ApiClient.getInstance().getRooms(new ApiClient.ApiCallback<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
                roomList = rooms;
                roomAdapter.updateData(rooms);
                updateSummary(rooms.size(), calculateTotalAvailableBeds(rooms));
            }

            @Override
            public void onError(String errorMessage) {
                List<Room> fallbackRooms = ApiClient.getInstance().getDefaultRooms();
                roomList = fallbackRooms;
                roomAdapter.updateData(fallbackRooms);
                updateSummary(fallbackRooms.size(), calculateTotalAvailableBeds(fallbackRooms));
            }
        });
    }

    private void applyCurrentFilters() {
        String query = etSearchRooms.getText() != null ? etSearchRooms.getText().toString() : "";
        roomAdapter.filter(query, currentBlockFilter, isAvailableOnly);
        int count = roomAdapter.getItemCount();
        tvRoomsSummary.setText("Showing " + count + " Rooms");
    }

    private int calculateTotalAvailableBeds(List<Room> rooms) {
        int beds = 0;
        for (Room r : rooms) {
            beds += r.getAvailableBeds();
        }
        return beds;
    }

    private void updateSummary(int roomCount, int bedCount) {
        tvRoomsSummary.setText("Showing " + roomCount + " Rooms • " + bedCount + " Beds Available");
    }
}
