/**
 * Hostel Room Allocation and Vacancy Registration System
 * Student Login Portal Interaction Logic
 */

document.addEventListener('DOMContentLoaded', () => {
  // DOM Elements
  const loginForm = document.getElementById('studentLoginForm');
  const studentIdInput = document.getElementById('studentId');
  const passwordInput = document.getElementById('password');
  const passwordToggleBtn = document.getElementById('passwordToggleBtn');
  const rememberMeCheckbox = document.getElementById('rememberMe');
  const submitButton = document.getElementById('loginSubmitBtn');
  const forgotPasswordLink = document.getElementById('forgotPasswordLink');
  const quickFillBtn = document.getElementById('quickFillBtn');
  const toastNotification = document.getElementById('toastNotification');
  const toastMessage = document.getElementById('toastMessage');

  // SVG Icons for password toggle
  const eyeOpenSvg = `
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
      <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
      <circle cx="12" cy="12" r="3"/>
    </svg>
  `;

  const eyeClosedSvg = `
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
      <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24"/>
      <path d="M10.73 5.08A10.43 10.43 0 0 1 12 5c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68"/>
      <path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61"/>
      <line x1="2" y1="2" x2="22" y2="22"/>
    </svg>
  `;

  // 1. Restore Remembered Student ID from LocalStorage
  const savedStudentId = localStorage.getItem('hostel_student_id');
  if (savedStudentId) {
    studentIdInput.value = savedStudentId;
    rememberMeCheckbox.checked = true;
    passwordInput.focus();
  }

  // 2. Show / Hide Password Toggle
  if (passwordToggleBtn && passwordInput) {
    passwordToggleBtn.addEventListener('click', () => {
      const isPassword = passwordInput.getAttribute('type') === 'password';
      if (isPassword) {
        passwordInput.setAttribute('type', 'text');
        passwordToggleBtn.innerHTML = eyeClosedSvg;
        passwordToggleBtn.setAttribute('aria-label', 'Hide password');
        passwordToggleBtn.setAttribute('title', 'Hide password');
      } else {
        passwordInput.setAttribute('type', 'password');
        passwordToggleBtn.innerHTML = eyeOpenSvg;
        passwordToggleBtn.setAttribute('aria-label', 'Show password');
        passwordToggleBtn.setAttribute('title', 'Show password');
      }
    });
  }

  // 3. Clear Validation Errors on User Input
  [studentIdInput, passwordInput].forEach(input => {
    input.addEventListener('input', () => {
      const parentGroup = input.closest('.form-group');
      if (parentGroup && parentGroup.classList.contains('has-error')) {
        parentGroup.classList.remove('has-error');
      }
    });
  });

  // 4. Quick Fill Demo Button (Helpful for project demo)
  if (quickFillBtn) {
    quickFillBtn.addEventListener('click', () => {
      studentIdInput.value = 'STU-2024-8842';
      passwordInput.value = 'HostelPass@2024';
      clearErrors();

      // Ensure demo user is recorded with demo student name
      let registeredUsers = {};
      try {
        registeredUsers = JSON.parse(localStorage.getItem('hostel_registered_students') || '{}');
      } catch (err) {}

      const demoName = registeredUsers['stu-2024-8842']?.name || localStorage.getItem('current_student_name') || 'Demo Student';
      sessionStorage.setItem('current_student_name', demoName);
      localStorage.setItem('current_student_name', demoName);

      showToast('Demo credentials populated for testing!', 'info');
      passwordInput.focus();
    });
  }

  // 5. Forgot Password Dialog / Notification
  if (forgotPasswordLink) {
    forgotPasswordLink.addEventListener('click', (e) => {
      e.preventDefault();
      const studentId = studentIdInput.value.trim();
      if (studentId) {
        showToast(`Password recovery link sent to registered email for ID: ${studentId}`, 'info');
      } else {
        showToast('Please enter your Student ID first, then click Forgot Password.', 'info');
        studentIdInput.focus();
      }
    });
  }

  // 6. Form Submission & Validation -> Redirect to Student Dashboard
  if (loginForm) {
    loginForm.addEventListener('submit', (e) => {
      e.preventDefault();
      clearErrors();

      const studentId = studentIdInput.value.trim();
      const password = passwordInput.value;
      let hasError = false;

      // Validate Student ID
      if (!studentId) {
        showInputError(studentIdInput, 'Please enter your Student ID or Full Name');
        hasError = true;
      } else if (studentId.length < 3) {
        showInputError(studentIdInput, 'Student ID must be at least 3 characters');
        hasError = true;
      }

      // Validate Password
      if (!password) {
        showInputError(passwordInput, 'Please enter your password');
        hasError = true;
      } else if (password.length < 4) {
        showInputError(passwordInput, 'Password must be at least 4 characters');
        hasError = true;
      }

      if (hasError) return;

      // Handle "Remember Me"
      if (rememberMeCheckbox.checked) {
        localStorage.setItem('hostel_student_id', studentId);
      } else {
        localStorage.removeItem('hostel_student_id');
      }

      // Resolve dynamic student name from registration or user input - NO HARDCODED ALEX MORGAN
      let registeredUsers = {};
      try {
        registeredUsers = JSON.parse(localStorage.getItem('hostel_registered_students') || '{}');
      } catch (err) {}

      let resolvedName = '';
      const matched = registeredUsers[studentId.toLowerCase()];
      if (matched && matched.name) {
        resolvedName = matched.name;
      } else {
        const isNameFormat = /^[A-Za-z\s.'-]+$/.test(studentId) && studentId.length > 2 && !studentId.toUpperCase().startsWith('STU');
        if (isNameFormat) {
          resolvedName = studentId;
        } else {
          resolvedName = localStorage.getItem('current_student_name') || studentId;
        }
      }

      // Store student session name for personalized dashboard greeting
      sessionStorage.setItem('current_hostel_student', studentId);
      sessionStorage.setItem('current_student_name', resolvedName);
      localStorage.setItem('current_student_name', resolvedName);
      localStorage.setItem('current_hostel_student', studentId);

      // Button Loading State
      submitButton.classList.add('is-loading');
      submitButton.disabled = true;

      showToast(`Welcome ${resolvedName}! Loading Student Dashboard...`, 'success');

      // Seamless redirect to Student Dashboard
      setTimeout(() => {
        window.location.href = 'student-dashboard.html';
      }, 1000);
    });
  }

  // Helper Functions
  function showInputError(inputElement, message) {
    const group = inputElement.closest('.form-group');
    if (group) {
      group.classList.add('has-error');
      const errorMsgEl = group.querySelector('.input-error-msg span');
      if (errorMsgEl) {
        errorMsgEl.textContent = message;
      }
    }
  }

  function clearErrors() {
    document.querySelectorAll('.form-group.has-error').forEach(group => {
      group.classList.remove('has-error');
    });
  }

  let toastTimeout;
  function showToast(message, type = 'info') {
    if (!toastNotification || !toastMessage) return;

    clearTimeout(toastTimeout);
    toastMessage.textContent = message;
    toastNotification.className = `toast-notification toast-${type} show`;

    toastTimeout = setTimeout(() => {
      toastNotification.classList.remove('show');
    }, 4000);
  }
});
