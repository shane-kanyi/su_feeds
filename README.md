# SU Feeds

A desktop application for students to track class topics and share feedback. Built with Java Swing and PostgreSQL. Features secure password storage using bcrypt.

## Features

- Secure user registration and login (bcrypt password hashing)
- Add/view enrolled courses
- Track weekly topics for each course
- Share/view feedback, comments, jokes, and summaries
- Robust error handling

## Technology Stack

- **Language:** Java 21
- **GUI:** Java Swing
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Security:** jBCrypt

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- PostgreSQL
- Apache Maven
- Git

## Setup & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/su_feeds.git
cd su_feeds
```

### 2. Database Setup
Log in to PostgreSQL as a superuser:
```bash
sudo -u postgres psql
```
Run the following SQL commands (replace `<your_strong_password>`):

```sql
-- Create a new user
CREATE USER su_feeds_user WITH PASSWORD '<your_strong_password>';
-- Create the database
CREATE DATABASE su_feeds;
-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE su_feeds TO su_feeds_user;
```
Exit psql (`\q`).

Connect as the new user:
```bash
psql -U su_feeds_user -d su_feeds
```
Grant permissions on the public schema:
```sql
GRANT ALL ON SCHEMA public TO su_feeds_user;
```

Set up tables by running the script in `db/schema.sql`.

### 3. Application Configuration
Create `src/main/resources/application.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/su_feeds
db.user=su_feeds_user
db.password=<your_strong_password>
```
Replace `<your_strong_password>` with your password.

### 4. Build the Application
```bash
mvn clean install
```

### 5. Run the Application
```bash
java -cp target/su-feeds-1.0-SNAPSHOT.jar com.project.sufeeds.Main
```
This launches the login window. Register a new user and start using the app.

## Folder Structure
```text
su_feeds/
├── db/
│   └── schema.sql                # Database table creation script
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/project/sufeeds/   # Java source code
│   └── resources/
│       └── application.properties    # App config (you create this)
├── .gitignore
├── pom.xml                        # Maven config
└── README.md
```