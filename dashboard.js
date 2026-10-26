/**
 * Hostel Room Allocation and Vacancy Registration System
 * Shared Student Portal JavaScript
 */

document.addEventListener('DOMContentLoaded', () => {
  // 1. Session & Student Details Hydration
  const loggedStudentId = sessionStorage.getItem('current_hostel_student') || localStorage.getItem('current_hostel_student') || 'STU-2024-8842';
  const storedName = sessionStorage.getItem('current_student_name') || localStorage.getItem('current_student_name');
  
  // Dynamic student name from registration/login - DO NOT USE A FIXED DEMO NAME LIKE ALEX MORGAN
  const studentDisplayName = storedName || loggedStudentId;

  // Calculate dynamic initials from student name
  function getInitials(name) {
    if (!name) return 'ST';
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) {
      return (parts[0][0] + parts[1][0]).toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  }
  const initials = getInitials(studentDisplayName);

  // Update dynamic avatar initials
  document.querySelectorAll('.user-avatar-mini, .header-avatar, .id-avatar-lg').forEach(el => {
    el.textContent = initials;
  });

  // Update DOM elements if present
  document.querySelectorAll('.student-id-display').forEach(el => {
    el.textContent = loggedStudentId;
    if (el.tagName === 'INPUT') el.value = loggedStudentId;
  });

  document.querySelectorAll('.student-name-display').forEach(el => {
    el.textContent = studentDisplayName;
    if (el.tagName === 'INPUT') el.value = studentDisplayName;
  });

  const bannerWelcomeHeading = document.getElementById('bannerWelcomeHeading');
  if (bannerWelcomeHeading) {
    const hours = new Date().getHours();
    let timeGreeting = 'Good morning';
    if (hours >= 12 && hours < 17) timeGreeting = 'Good afternoon';
    else if (hours >= 17) timeGreeting = 'Good evening';
    bannerWelcomeHeading.textContent = `${timeGreeting}, ${studentDisplayName}! 👋`;
  }

  // 2. Mobile Sidebar Navigation Toggle
  const sidebarToggleBtn = document.getElementById('mobileSidebarToggle');
  const sidebar = document.getElementById('appSidebar');
  const backdrop = document.getElementById('sidebarBackdrop');

  if (sidebarToggleBtn && sidebar) {
    sidebarToggleBtn.addEventListener('click', () => {
      sidebar.classList.toggle('mobile-open');
      if (backdrop) backdrop.classList.toggle('active');
    });
  }

  if (backdrop && sidebar) {
    backdrop.addEventListener('click', () => {
      sidebar.classList.remove('mobile-open');
      backdrop.classList.remove('active');
    });
  }

  // 3. Logout Handling
  document.querySelectorAll('.btn-logout-trigger').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      sessionStorage.removeItem('current_hostel_student');
      showToast('Signing out of Student Portal... See you soon!', 'info');
      setTimeout(() => {
        window.location.href = 'index.html';
      }, 700);
    });
  });

  // 4. Notification Bell Quick Pop
  const notifBtn = document.getElementById('headerNotifBtn');
  if (notifBtn) {
    notifBtn.addEventListener('click', () => {
      showToast('You have 2 unread notices: Room verification approved & Mess menu updated.', 'info');
    });
  }

  // 5. Toast Notification System with Success, Info, & Error Icons
  window.showToast = function(message, type = 'info') {
    let toast = document.getElementById('dashboardToast');
    if (!toast) {
      toast = document.createElement('div');
      toast.id = 'dashboardToast';
      toast.className = 'toast-box';
      document.body.appendChild(toast);
    }

    let strokeColor = '#2563eb';
    let iconSvg = `
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="12" y1="16" x2="12" y2="12"></line>
        <line x1="12" y1="8" x2="12.01" y2="8"></line>
      </svg>
    `;

    if (type === 'success') {
      strokeColor = '#10b981';
      iconSvg = `
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#10b981" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
          <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
      `;
    } else if (type === 'error') {
      strokeColor = '#ef4444';
      iconSvg = `
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="10"></circle>
          <line x1="15" y1="9" x2="9" y2="15"></line>
          <line x1="9" y1="9" x2="15" y2="15"></line>
        </svg>
      `;
    }

    toast.style.borderLeftColor = strokeColor;
    toast.innerHTML = `${iconSvg}<span>${message}</span>`;
    toast.classList.add('show');

    clearTimeout(window.toastTimer);
    window.toastTimer = setTimeout(() => {
      toast.classList.remove('show');
    }, 4500);
  };
});
