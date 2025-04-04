# 🐦 Twitter Clone API

A backend-only clone of Twitter built using **Spring Boot**. Users can create tweets, upload media (image/video to AWS S3), comment, and fetch user feeds. Redis is optionally used for feed caching.

## 📚 Features

- Create tweets with optional media (image/video)
- Upload and retrieve media via AWS S3
- Comment on tweets
- Fetch tweets by user
- Lazy fetching of related entities (User, Comments)
- Swagger documentation for all APIs
- Authentication and Authorization (JWT)


---

## 🛠️ Tech Stack

| Technology         | Description                  |
|--------------------|------------------------------|
| Java 17+           | Programming language         |
| Spring Boot        | Application framework        |
| Spring Data JPA    | ORM and database access      |
| MySQL              | Relational database          |
| AWS S3             | Media storage                |
| Swagger/OpenAPI    | API documentation            |
| Lombok             | Boilerplate code reduction   |

---

## 🔧 Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/michaelmounir12/twitter_clone.git
cd twitter_clone
2. Configure environment
Create application.properties or application.yml and set the following:

properties
Copy
Edit
# AWS S3
aws.accessKeyId=YOUR_ACCESS_KEY
aws.secretAccessKey=YOUR_SECRET_KEY
aws.region=us-east-1
aws.s3.bucket=your-bucket-name

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/twitter_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


3. Run the app
bash
Copy
Edit
./mvnw spring-boot:run
🧪 API Documentation (Swagger)
Once the server is running, visit:

bash
Copy
Edit
http://localhost:8080/swagger-ui/index.html
or

bash
Copy
Edit
http://localhost:8080/api-docs
🧵 Sample Endpoints
POST /api/tweets — Create a tweet with optional media (multipart)

GET /api/tweets/user/{userId} — Get all tweets for a user

GET /api/tweets/{tweetId}/comments — Get all comments on a tweet

POST /api/tweets/{tweetId}/comments — Add comment to a tweet

📂 Project Structure
arduino
Copy
Edit
src/
├── config/
├── controller/
├── dto/
├── entity/
├── dao/
├── service/
└── TwitterApplication.java
🧠 Future Features

Like/Retweet features

Feed caching with Redis


