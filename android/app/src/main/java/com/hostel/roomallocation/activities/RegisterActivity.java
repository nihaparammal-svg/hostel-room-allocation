package com.hostel.roomallocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hostel.roomallocation.R;
import com.hostel.roomallocation.models.Student;
import com.hostel.roomallocation.network.ApiClient;
import com.hostel.roomallocation.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etRegStudentId, etEmail, etRegPassword, etConfirmPassword;
    private Button btnRegisterSubmit;
    private TextView tvGoToLogin;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etRegStudentId = findViewById(R.id.etRegStudentId);
        etEmail = findViewById(R.id.etEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
    }

    private void setupListeners() {
        btnRegisterSubmit.setOnClickListener(v -> performRegistration());
        tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String studentId = etRegStudentId.getText() != null ? etRegStudentId.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }

        if (studentId.isEmpty()) {
            etRegStudentId.setError("Please enter your student ID");
            etRegStudentId.requestFocus();
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid college email address");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 4) {
            etRegPassword.setError("Password must be at least 4 characters");
            etRegPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        btnRegisterSubmit.setEnabled(false);
        btnRegisterSubmit.setText("Registering...");

        // Call backend API /register
        ApiClient.getInstance().register(fullName, studentId, email, password, new ApiClient.ApiCallback<Student>() {
            @Override
            public void onSuccess(Student student) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText(R.string.btn_register);

                // DYNAMIC NAME STORAGE:
                // Store the registered full name dynamically in SessionManager
                sessionManager.createLoginSession(
                        studentId,
                        fullName,
                        email,
                        "Room 304",
                        "Block B"
                );

                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Registration Successful!")
                        .setMessage("Welcome, " + fullName + "!\nYour student account has been created. Proceeding to Student Dashboard.")
                        .setPositiveButton("Continue to Dashboard", (dialog, which) -> {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onError(String errorMessage) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText(R.string.btn_register);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
