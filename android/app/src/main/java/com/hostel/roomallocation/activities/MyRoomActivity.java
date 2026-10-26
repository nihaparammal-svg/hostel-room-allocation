package com.hostel.roomallocation.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hostel.roomallocation.R;

public class MyRoomActivity extends AppCompatActivity {

    private ImageView btnMyRoomBack;
    private Button btnReportIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        btnMyRoomBack = findViewById(R.id.btnMyRoomBack);
        btnReportIssue = findViewById(R.id.btnReportIssue);

        btnMyRoomBack.setOnClickListener(v -> finish());

        btnReportIssue.setOnClickListener(v -> showMaintenanceDialog());
    }

    private void showMaintenanceDialog() {
        EditText etIssue = new EditText(this);
        etIssue.setHint("Describe the maintenance or repair issue...");
        etIssue.setMinLines(3);
        etIssue.setPadding(32, 24, 32, 24);

        new AlertDialog.Builder(this)
                .setTitle("Report Room Maintenance")
                .setMessage("Submit a maintenance request for Room 304 (Block B):")
                .setView(etIssue)
                .setPositiveButton("Submit Ticket", (dialog, which) -> {
                    String issue = etIssue.getText().toString().trim();
                    if (!issue.isEmpty()) {
                        Toast.makeText(this, "Maintenance ticket #TKT-412 created. Maintenance staff will visit shortly.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Maintenance request logged.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
