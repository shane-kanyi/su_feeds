CREATE TABLE tbl_users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_courses (
    course_id SERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL
);

CREATE TABLE tbl_enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES tbl_users(user_id) ON DELETE CASCADE,
    course_id INT NOT NULL REFERENCES tbl_courses(course_id) ON DELETE CASCADE,
    UNIQUE(user_id, course_id) -- A student can only enroll in a course once
);

CREATE TABLE tbl_topics (
    topic_id SERIAL PRIMARY KEY,
    course_id INT NOT NULL REFERENCES tbl_courses(course_id) ON DELETE CASCADE,
    week_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE tbl_posts (
    post_id SERIAL PRIMARY KEY,
    topic_id INT NOT NULL REFERENCES tbl_topics(topic_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES tbl_users(user_id) ON DELETE CASCADE,
    post_type VARCHAR(50) NOT NULL, -- e.g., 'feedback', 'comment', 'joke', 'summary'
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
