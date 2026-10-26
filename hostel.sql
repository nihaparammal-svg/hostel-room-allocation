CREATE DATABASE hostel_management;

USE hostel_management;

-- Student details
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(50),
    phone VARCHAR(15),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Admin details
CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Room details
CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    block_name VARCHAR(50),
    room_type VARCHAR(30),
    capacity INT NOT NULL,
    occupied INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'Available'
);

-- Hostel applications
CREATE TABLE applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    preferred_hostel VARCHAR(50),
    preferred_room VARCHAR(20),
    room_type VARCHAR(30),
    reason VARCHAR(255),
    application_date DATE,
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

-- Room allocations
CREATE TABLE allocations (
    allocation_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    room_id INT NOT NULL,
    allocation_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Vacancy records
CREATE TABLE vacancies (
    vacancy_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    vacant_beds INT DEFAULT 0,
    updated_date DATE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Sample admin account
INSERT INTO admins (username, password)
VALUES ('admin', 'admin123');

-- Sample rooms
INSERT INTO rooms
(room_number, block_name, room_type, capacity, occupied, status)
VALUES
('A-101', 'A Block', 'Single', 1, 0, 'Available'),
('A-102', 'A Block', 'Double', 2, 1, 'Available'),
('B-201', 'B Block', 'Triple', 3, 3, 'Full'),
('B-202', 'B Block', 'Double', 2, 0, 'Available'),
('C-301', 'C Block', 'Single', 1, 0, 'Available');