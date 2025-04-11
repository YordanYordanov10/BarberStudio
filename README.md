# 💈 Barber Studio Web Application

A modern web application for managing a professional barber studio. Built with **Java Spring Boot (MVC + Thymeleaf)** for the backend and frontend, this system provides an intuitive way to handle appointments, barbers, services, and clients.

## ✂️ Features

- User registration and login  
- Public homepage with information about the studio and services  
- Book an appointment with a specific barber and time slot  
- Admin panel to manage:
  - Barbers
  - Clients
  - Services
  - Promotions
  - Appointments
- Barber dashboard:
  - View upcoming appointments
  - Track loyal customers and revenue statistics
  - Profile rating and service overview
- Custom calendar booking system  
- Responsive and modern UI using Thymeleaf and custom styling

## 📬 Email Notification Microservice

A separate microservice is responsible for:
- Sending email notifications when a user books an appointment  
- Logging the history of each booking

## 🧰 Technologies Used

- Java Spring Boot  
- Spring Security  
- Thymeleaf  
- JPA & Hibernate  
- MySQL  
- REST API (for microservice communication)  
- JavaMailSender (email service)  
- Bootstrap (optional for styling)

## 🔐 User Roles

- **Guest** – Can view homepage, team, and services  
- **Client** – Can register, log in, and book appointments  
- **Barber** – Can manage their schedule and see analytics  
- **Admin** – Full control over the app data and content

## 📦 Project Structure

### Main App - `BarberStudio/`
src/main/java/ └── bg.softuni.barberstudio ├── web # Controllers ├── service # Business logic ├── repository # JPA Repositories └── model # Entities and DTOs resources/ ├── templates/ # Thymeleaf HTML templates └── static/ # CSS, JS, Images

application.properties


### Email Microservice - `EmailNotification/`
src/main/java/ └── bg.softuni.emailnotification ├── web # REST Controller ├── service # Email sending logic └── model # DTOs for messages and history


## 🛠️ Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YordanYordanov10/barber-studio.git
Configure the database in application.properties

Run the application from the main Spring Boot class

Visit:
http://localhost:8080
