package com.hostel.roomallocation.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.utils.SessionManager;

public class MyApplicationActivity extends AppCompatActivity {

    private ImageView btnMyAppBack;
    private TextView tvApplicantDetailName;
    private Button btnDownloadReceipt;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_application);

        sessionManager = new SessionManager(this);

        initViews();
        setupData();
    }

    private void initViews() {
        btnMyAppBack = findViewById(R.id.btnMyAppBack);
        tvApplicantDetailName = findViewById(R.id.tvApplicantDetailName);
        btnDownloadReceipt = findViewById(R.id.btnDownloadReceipt);

        btnMyAppBack.setOnClickListener(v -> finish());
        btnDownloadReceipt.setOnClickListener(v -> {
            Toast.makeText(this, "Hostel Allotment Letter saved to Downloads", Toast.LENGTH_LONG).show();
        });
    }

    private void setupData() {
        // DYNAMIC STUDENT NAME
        String dynamicName = sessionManager.getStudentName();
        tvApplicantDetailName.setText("Applicant Name: " + dynamicName);
    }
}
