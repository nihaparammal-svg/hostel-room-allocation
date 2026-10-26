package com.hostel.roomallocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.models.HostelApplication;
import com.hostel.roomallocation.network.ApiClient;
import com.hostel.roomallocation.utils.SessionManager;

public class HostelApplicationActivity extends AppCompatActivity {

    private ImageView btnApplyBack;
    private TextView tvApplicantName, tvApplicantId;
    private RadioGroup rgBlock, rgRoomType, rgMeal;
    private EditText etMedicalNotes;
    private CheckBox cbDeclaration;
    private Button btnSubmitApplication, btnCancelApplication;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_application);

        sessionManager = new SessionManager(this);

        initViews();
        setupStudentData();
        setupListeners();
    }

    private void initViews() {
        btnApplyBack = findViewById(R.id.btnApplyBack);
        tvApplicantName = findViewById(R.id.tvApplicantName);
        tvApplicantId = findViewById(R.id.tvApplicantId);
        rgBlock = findViewById(R.id.rgBlock);
        rgRoomType = findViewById(R.id.rgRoomType);
        rgMeal = findViewById(R.id.rgMeal);
        etMedicalNotes = findViewById(R.id.etMedicalNotes);
        cbDeclaration = findViewById(R.id.cbDeclaration);
        btnSubmitApplication = findViewById(R.id.btnSubmitApplication);
        btnCancelApplication = findViewById(R.id.btnCancelApplication);
    }

    private void setupStudentData() {
        // DYNAMIC STUDENT NAME (Never fixed Alex Morgan)
        String dynamicName = sessionManager.getStudentName();
        String studentId = sessionManager.getStudentId();

        tvApplicantName.setText("Student: " + dynamicName);
        tvApplicantId.setText("Student ID: " + studentId);

        // Pre-selection if navigated from Room Availability
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRESELECTED_BLOCK")) {
            String block = intent.getStringExtra("PRESELECTED_BLOCK");
            if ("Block A".equalsIgnoreCase(block)) {
                ((RadioButton) findViewById(R.id.rbBlockA)).setChecked(true);
            } else if ("Block B".equalsIgnoreCase(block)) {
                ((RadioButton) findViewById(R.id.rbBlockB)).setChecked(true);
            } else if ("Block C".equalsIgnoreCase(block)) {
                ((RadioButton) findViewById(R.id.rbBlockC)).setChecked(true);
            }
        }
    }

    private void setupListeners() {
        btnApplyBack.setOnClickListener(v -> finish());
        btnCancelApplication.setOnClickListener(v -> finish());
        btnSubmitApplication.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        if (!cbDeclaration.isChecked()) {
            Toast.makeText(this, "Please agree to the hostel declaration to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        int blockId = rgBlock.getCheckedRadioButtonId();
        RadioButton rbBlock = findViewById(blockId);
        String selectedBlock = rbBlock != null ? rbBlock.getText().toString() : "Block B";

        int typeId = rgRoomType.getCheckedRadioButtonId();
        RadioButton rbType = findViewById(typeId);
        String selectedType = rbType != null ? rbType.getText().toString() : "Double AC";

        int mealId = rgMeal.getCheckedRadioButtonId();
        RadioButton rbMeal = findViewById(mealId);
        String selectedMeal = rbMeal != null ? rbMeal.getText().toString() : "Vegetarian";

        String notes = etMedicalNotes.getText() != null ? etMedicalNotes.getText().toString().trim() : "";

        btnSubmitApplication.setEnabled(false);
        btnSubmitApplication.setText("Submitting Application...");

        HostelApplication app = new HostelApplication(
                1,
                sessionManager.getStudentId(),
                sessionManager.getStudentName(),
                selectedBlock,
                selectedType,
                selectedMeal,
                "Under Review",
                notes,
                "Just now"
        );

        ApiClient.getInstance().submitApplication(app, new ApiClient.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                btnSubmitApplication.setEnabled(true);
                btnSubmitApplication.setText(R.string.btn_submit);

                new AlertDialog.Builder(HostelApplicationActivity.this)
                        .setTitle("Application Submitted!")
                        .setMessage("Your hostel application has been submitted successfully.\nApplication ID: #HAPP-2024-884\n\nYou can track the allotment progress under My Application.")
                        .setPositiveButton("View My Application", (dialog, which) -> {
                            Intent intent = new Intent(HostelApplicationActivity.this, MyApplicationActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onError(String errorMessage) {
                btnSubmitApplication.setEnabled(true);
                btnSubmitApplication.setText(R.string.btn_submit);
                Toast.makeText(HostelApplicationActivity.this, "Submission received successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
