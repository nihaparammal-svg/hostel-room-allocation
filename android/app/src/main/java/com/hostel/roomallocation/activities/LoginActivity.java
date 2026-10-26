package com.hostel.roomallocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hostel.roomallocation.R;
import com.hostel.roomallocation.models.Student;
import com.hostel.roomallocation.network.ApiClient;
import com.hostel.roomallocation.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etStudentId, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvGoToRegister, tvForgotPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // If session is already active, navigate straight to Dashboard
        if (sessionManager.isLoggedIn() && sessionManager.isRememberMe()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Pre-fill previously remembered Student ID
        String savedId = sessionManager.getStudentId();
        if (savedId != null && !savedId.isEmpty()) {
            etStudentId.setText(savedId);
        }
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Password Assistance")
                    .setMessage("To reset your hostel student password, please contact your hostel administration office or college IT helpdesk at support@college.edu.")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void performLogin() {
        String studentId = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (studentId.isEmpty()) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Signing In...");

        ApiClient.getInstance().login(studentId, password, new ApiClient.ApiCallback<Student>() {
            @Override
            public void onSuccess(Student student) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.btn_login);

                // DYNAMIC NAME STORAGE:
                // Store student name dynamically from API/user registration.
                String studentName = student.getName();
                if (studentName == null || studentName.trim().isEmpty() || studentName.equalsIgnoreCase("Student")) {
                    studentName = studentId;
                }

                sessionManager.createLoginSession(
                        student.getStudentId(),
                        studentName,
                        student.getEmail(),
                        student.getRoomNumber(),
                        student.getBlock()
                );
                sessionManager.setRememberMe(cbRememberMe.isChecked());

                Toast.makeText(LoginActivity.this, "Welcome, " + studentName + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.btn_login);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
