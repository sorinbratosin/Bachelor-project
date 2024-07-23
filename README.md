This project is an IoT-based web application designed for automating and monitoring garden irrigation. The system is designed to measure soil moisture, control water pumps, and provide real-time data to users through a web interface. The system comprises both hardware and software components, including a React frontend, a Spring Boot backend, IoT devices for data collection and a MySQL database. 

**Technologies Used**

Frontend: React, TypeScript, Axios, Material-UI
Backend: Spring Boot, Java, Spring Security, JWT
Database: MySQL
Hardware: C++ for ESP32 in Arduino

**Architecture**

The application architecture consists of three main layers:
Hardware: The IoT device collects and transmits sensor data and the irrigation status to the backend
Backend: Implemented using Spring Boot, handling API requests, business logic, and database interactions
Frontend: Built with React, providing a user-friendly interface

**Hardware Setup**

ESP32 Microcontroller: Collects data from sensors, controls the water pump, and communicates with the backend application via Wi-Fi
Humidity Sensor: Measures soil moisture and sends the data to the microcontroller
5V Relay: Controls the operation of the water pump based on signals received from the microcontroller
Water Pump: Activated or deactivated by the relay to maintain optimal soil moisture levels
0.96 SSD1306 OLED Display: Displays sensor data and system status
Power Supply: Provides electrical power for the relay and water pump
Breadboard: Facilitates connections and mounting of electronic components in an organized and easily modifiable manner
![image](https://github.com/user-attachments/assets/77420b31-554c-4512-b8e1-41d30daf62f1)

**API Endpoints**
_Authentication_
POST /api/login: User login
POST /api/register: User registration

_Sensor Data_
POST /api/postHumidity: Receives humidity data and irrigation status from the IoT device. Requires a "deviceIOT" header and key for authentication

_User Data_
GET /api/date-home/{userId}: Retrieves user-specific data, the latest irrigation time and humidity levels. Requires an "Authorization" header and JWT token starting with "Bearer"
