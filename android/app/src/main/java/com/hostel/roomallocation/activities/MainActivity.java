package com.hostel.roomallocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.hostel.roomallocation.R;
import com.hostel.roomallocation.adapters.ActivityAdapter;
import com.hostel.roomallocation.models.ActivityItem;
import com.hostel.roomallocation.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ImageView btnMenu, btnNotification;
    private TextView tvWelcomeGreeting, tvStudentSubtitle, tvDashboardAvatarInitials;

    // Stat cards
    private CardView cardAvailableRooms, cardApplicationStatus, cardAllocatedRoom, cardAvailableBeds;
    private TextView tvAvailableRoomsCount, tvAppStatus, tvAllocatedRoomNum, tvAvailableBedsCount;

    // Quick Actions
    private Button btnQuickRooms, btnQuickApply, btnQuickMyApplication, btnQuickMyRoom;

    // Recent Activity
    private RecyclerView rvRecentActivity;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        initViews();
        setupNavigationDrawer();
        setupDashboardContent();
        setupQuickActions();
        setupRecentActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Dynamically refresh student greeting if profile name was updated
        updateDynamicStudentGreeting();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        btnMenu = findViewById(R.id.btnMenu);
        btnNotification = findViewById(R.id.btnNotification);

        tvWelcomeGreeting = findViewById(R.id.tvWelcomeGreeting);
        tvStudentSubtitle = findViewById(R.id.tvStudentSubtitle);
        tvDashboardAvatarInitials = findViewById(R.id.tvDashboardAvatarInitials);

        cardAvailableRooms = findViewById(R.id.cardAvailableRooms);
        cardApplicationStatus = findViewById(R.id.cardApplicationStatus);
        cardAllocatedRoom = findViewById(R.id.cardAllocatedRoom);
        cardAvailableBeds = findViewById(R.id.cardAvailableBeds);

        tvAvailableRoomsCount = findViewById(R.id.tvAvailableRoomsCount);
        tvAppStatus = findViewById(R.id.tvAppStatus);
        tvAllocatedRoomNum = findViewById(R.id.tvAllocatedRoomNum);
        tvAvailableBedsCount = findViewById(R.id.tvAvailableBedsCount);

        btnQuickRooms = findViewById(R.id.btnQuickRooms);
        btnQuickApply = findViewById(R.id.btnQuickApply);
        btnQuickMyApplication = findViewById(R.id.btnQuickMyApplication);
        btnQuickMyRoom = findViewById(R.id.btnQuickMyRoom);

        rvRecentActivity = findViewById(R.id.rvRecentActivity);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnNotification.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hostel Announcements")
                    .setMessage("1. Maintenance check scheduled for Block B on Saturday 10:00 AM.\n\n2. Mess menu updated for upcoming semester.\n\n3. Gate curfew is 10:00 PM on weekdays.")
                    .setPositiveButton("Close", null)
                    .show();
        });
    }

    /**
     * Updates greeting with the dynamic student name stored in SessionManager
     * Eliminates any hardcoded 'Alex Morgan'.
     */
    private void updateDynamicStudentGreeting() {
        String dynamicName = sessionManager.getStudentName();
        String studentId = sessionManager.getStudentId();
        String initials = sessionManager.getStudentInitials();

        tvWelcomeGreeting.setText("Welcome back, " + dynamicName + "! 👋");
        tvStudentSubtitle.setText("Student ID: " + studentId + " • " + sessionManager.getBlock());
        tvDashboardAvatarInitials.setText(initials);

        // Update Navigation Drawer Header
        View headerView = navView.getHeaderView(0);
        if (headerView != null) {
            TextView tvNavName = headerView.findViewById(R.id.tvNavStudentName);
            TextView tvNavId = headerView.findViewById(R.id.tvNavStudentId);
            TextView tvNavInitials = headerView.findViewById(R.id.tvNavInitials);

            if (tvNavName != null) tvNavName.setText(dynamicName);
            if (tvNavId != null) tvNavId.setText("ID: " + studentId);
            if (tvNavInitials != null) tvNavInitials.setText(initials);
        }
    }

    private void setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener(this);
    }

    private void setupDashboardContent() {
        updateDynamicStudentGreeting();

        // Stat cards click actions
        cardAvailableRooms.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RoomAvailabilityActivity.class)));
        cardApplicationStatus.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyApplicationActivity.class)));
        cardAllocatedRoom.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyRoomActivity.class)));
        cardAvailableBeds.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RoomAvailabilityActivity.class)));
    }

    private void setupQuickActions() {
        btnQuickRooms.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RoomAvailabilityActivity.class)));
        btnQuickApply.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HostelApplicationActivity.class)));
        btnQuickMyApplication.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyApplicationActivity.class)));
        btnQuickMyRoom.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyRoomActivity.class)));
    }

    private void setupRecentActivity() {
        List<ActivityItem> list = new ArrayList<>();
        list.add(new ActivityItem(1, "Room 304 Allotted", "Room 304 (Block B) has been allocated to you.", "Just now", R.color.success));
        list.add(new ActivityItem(2, "Hostel Application Approved", "Warden approved your accommodation request.", "Yesterday", R.color.primary));
        list.add(new ActivityItem(3, "Document Verification Completed", "Identity and college enrollment verified.", "2 days ago", R.color.purple_primary));
        list.add(new ActivityItem(4, "Application Submitted", "Submitted application for Fall Semester.", "5 days ago", R.color.warning));

        rvRecentActivity.setLayoutManager(new LinearLayoutManager(this));
        ActivityAdapter adapter = new ActivityAdapter(this, list);
        rvRecentActivity.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Already on dashboard
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_rooms) {
            startActivity(new Intent(this, RoomAvailabilityActivity.class));
        } else if (id == R.id.nav_apply) {
            startActivity(new Intent(this, HostelApplicationActivity.class));
        } else if (id == R.id.nav_my_application) {
            startActivity(new Intent(this, MyApplicationActivity.class));
        } else if (id == R.id.nav_my_room) {
            startActivity(new Intent(this, MyRoomActivity.class));
        } else if (id == R.id.nav_vacancy_leave) {
            startActivity(new Intent(this, VacancyLeaveActivity.class));
        } else if (id == R.id.nav_logout) {
            performLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to sign out from your hostel account?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    sessionManager.logoutUser();
                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
