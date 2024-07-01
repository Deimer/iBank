# iBank

iBank is a mobile banking application developed to provide a comprehensive banking experience to users. The application includes functionalities such as registration, login, balance viewing, banking movements, transaction details, and additional features like balance recharges and transfers to friends.

## Project Overview
This project is a technical test for Stori, designed to showcase proficiency in developing a feature-rich mobile banking application. The application is built following the requirements outlined in the provided [technical test document](https://firebasestorage.googleapis.com/v0/b/ibank-fb6b4.appspot.com/o/Technical%20Test%20Document%20Stori%2FAndroid_Stori_Technical_Test.pdf?alt=media&token=678c5d09-7688-423e-82a5-36abd99a0e6e).

## Features

### Splash Screen
- **Logo Display:** Initial splash screen with the app logo.
- **Navigation:** Automatically navigates to the login or home screen as soon as there is an active session, after a short delay.

### Registration
- **Personal Data:** Screen to enter first name, last name, email, and password.
- **Identification Photo:** Functionality to take an identification photo.
- **Success Screen:** Confirmation message "Welcome!"

### Login
- **Email:** Input field for email.
- **Password:** Input field for password.
- **Login Button:** Allows users to log in.
- **Register Button:** Redirects users to the registration start process.

### Home
- **Balance Information:** Displays the user's current balance.
- **Transactions List:** List of completed transactions.
- **Transaction Details:** Clicking on a transaction shows its details.

### Balance Recharges
- **Enter Amount:** Screen to input the amount to recharge.
- **Confirm Recharge:** Button to confirm the recharge.
- **Success Message:** Confirmation message showing the updated balance.

### Transfers to Friends
- **Enter Details:** Screen to input the friend's account details and amount to transfer.
- **Confirm Transfer:** Button to confirm the transfer.
- **Success Message:** Confirmation message showing the transfer was successful.

### Profile
- **Account Information:** Displays user's account information.
- **Logout:** Option to log out of the account.

## Architecture and Principles

### Clean Architecture
The project follows Clean Architecture principles, ensuring a robust and scalable structure. MVVM (Model-View-ViewModel) is used as the pattern for the presentation layer.

### SOLID Principles
The project adheres to SOLID principles, especially the Single Responsibility Principle, to ensure clean and maintainable code.

## Project Structure

```bash
iBank/
├── app/
├── network/
├── database/
├── datasource/
├── repository/
├── usecase/
├── presentation/
└── build.gradle
```

### Modules
- **app:** Application setup and entry point.
- **network:** Network communication setup.
- **database:** Database configurations and entities.
- **datasource:** Data source implementations.
- **repository:** Repositories for data handling.
- **usecase:** Use cases for business logic.
- **presentation:** Set of images, texts, colors and other resources belonging to the style of the application.

### UI Development
- **Compose:** All views are built using Jetpack Compose, providing a modern and declarative UI framework.
- **Theming:** The app implements a custom theme with dark mode support.
- **Navigation:** Compose-based navigation, where each screen receives data classes for navigation without needing to know the NavHostController.

## Internationalization (i18n)
The application supports both English and Spanish languages, enhancing accessibility for a broader user base. Language resources are managed efficiently to provide seamless switching between languages based on the user's device settings.

## Unit Testing
The project includes extensive unit tests covering various modules to ensure robustness.

## CI/CD Integration
CI/CD pipelines are set up for automated testing and deployment.

## Dependency Injection
Hilt is used for dependency injection, ensuring modular and testable code.

## Installation and Configuration

### Prerequisites
- Android Studio installed
- Android SDK configured
- Firebase account for database setup

### Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/Deimer/iBank.git
   ```
2. Open the project in Android Studio.
3. Configure Firebase with your project data.
4. Build and run the application on a device or emulator.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
