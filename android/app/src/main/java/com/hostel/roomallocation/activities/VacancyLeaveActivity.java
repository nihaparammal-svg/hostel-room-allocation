package com.hostel.roomallocation.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hostel.roomallocation.R;
import com.hostel.roomallocation.adapters.LeaveHistoryAdapter;
import com.hostel.roomallocation.models.LeaveRequest;
import com.hostel.roomallocation.network.ApiClient;
import com.hostel.roomallocation.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VacancyLeaveActivity extends AppCompatActivity {

    private ImageView btnLeaveBack;
    private Spinner spLeaveType;
    private EditText etFromDate, etToDate, etLeaveReason;
    private CheckBox cbParentConsent;
    private Button btnSubmitLeave;
    private RecyclerView rvLeaveHistory;

    private LeaveHistoryAdapter leaveAdapter;
    private List<LeaveRequest> leaveList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_leave);

        sessionManager = new SessionManager(this);

        initViews();
        setupSpinner();
        setupDatePickers();
        setupHistoryRecycler();
        setupListeners();
    }

    private void initViews() {
        btnLeaveBack = findViewById(R.id.btnLeaveBack);
        spLeaveType = findViewById(R.id.spLeaveType);
        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        etLeaveReason = findViewById(R.id.etLeaveReason);
        cbParentConsent = findViewById(R.id.cbParentConsent);
        btnSubmitLeave = findViewById(R.id.btnSubmitLeave);
        rvLeaveHistory = findViewById(R.id.rvLeaveHistory);
    }

    private void setupSpinner() {
        String[] types = {
                "Weekend Home Visit",
                "Vacation / Semester Break",
                "Medical / Emergency Leave",
                "Academic / Conference Visit"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spLeaveType.setAdapter(adapter);
    }

    private void setupDatePickers() {
        Calendar calendar = Calendar.getInstance();

        etFromDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format("%04d-%02d-%02d", year, (month + 1), dayOfMonth);
                etFromDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        etToDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format("%04d-%02d-%02d", year, (month + 1), dayOfMonth);
                etToDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void setupHistoryRecycler() {
        leaveList.add(new LeaveRequest(1, sessionManager.getStudentId(), "Weekend Home Visit", "2026-10-10", "2026-10-12", "Family festival", "Approved", "2026-10-09"));
        leaveList.add(new LeaveRequest(2, sessionManager.getStudentId(), "Medical Leave", "2026-09-18", "2026-09-20", "Dental consultation", "Approved", "2026-09-17"));

        rvLeaveHistory.setLayoutManager(new LinearLayoutManager(this));
        leaveAdapter = new LeaveHistoryAdapter(this, leaveList);
        rvLeaveHistory.setAdapter(leaveAdapter);
    }

    private void setupListeners() {
        btnLeaveBack.setOnClickListener(v -> finish());

        btnSubmitLeave.setOnClickListener(v -> {
            String from = etFromDate.getText().toString().trim();
            String to = etToDate.getText().toString().trim();
            String reason = etLeaveReason.getText().toString().trim();
            String type = spLeaveType.getSelectedItem().toString();

            if (from.isEmpty()) {
                Toast.makeText(this, "Please select departure date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (to.isEmpty()) {
                Toast.makeText(this, "Please select return date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (reason.isEmpty()) {
                etLeaveReason.setError("Please provide reason for leave");
                etLeaveReason.requestFocus();
                return;
            }

            if (!cbParentConsent.isChecked()) {
                Toast.makeText(this, "Parent/guardian consent is required", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSubmitLeave.setEnabled(false);
            btnSubmitLeave.setText("Submitting Leave...");

            LeaveRequest req = new LeaveRequest(
                    leaveList.size() + 1,
                    sessionManager.getStudentId(),
                    type,
                    from,
                    to,
                    reason,
                    "Pending",
                    "Today"
            );

            ApiClient.getInstance().submitLeave(req, new ApiClient.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    btnSubmitLeave.setEnabled(true);
                    btnSubmitLeave.setText("Submit Leave Request");

                    leaveAdapter.addLeave(req);
                    etLeaveReason.setText("");

                    new AlertDialog.Builder(VacancyLeaveActivity.this)
                            .setTitle("Leave Request Submitted")
                            .setMessage("Your leave application has been submitted to the hostel warden for approval.")
                            .setPositiveButton("OK", null)
                            .show();
                }

                @Override
                public void onError(String errorMessage) {
                    btnSubmitLeave.setEnabled(true);
                    btnSubmitLeave.setText("Submit Leave Request");
                    leaveAdapter.addLeave(req);
                    Toast.makeText(VacancyLeaveActivity.this, "Leave application submitted", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
