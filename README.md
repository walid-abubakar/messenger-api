# Simple Messenger API Service

The intent of this service is to build a simplified messenger API that would enable a web app to build a simple messaging application.

## Installation and Setup

### Tools you'll need:
* [Docker](https://docs.docker.com/desktop/)
* Insomnia
  * Curl commands will be listed below

### Frameworks/Language:
* Spring Boot MVC
* Kotlin: JVM Language
* JUnit Mockk: for unit tests
* Gradle: As the build automation tool

We will spin up two dockerized containers. One for our postgres database and the other to spin up the actual application.
This will be achieved using gradle wrapper and docker-compose.

### To spin up the project on your computer:

* Clone this repo
* Open it in your favorite IDE (OR run it directly from the command line using gradle with the provided script below)
  * The combined command line script is as follows, make sure to cd into the project root directory first since we are using gradle wrapper to build the project.
    * ```./gradlew clean && ./gradlew build && docker build --build-arg JAR_FILE=build/libs/\*.jar -t spring-boot-docker:latest . && docker-compose build --no-cache && docker-compose up --force-recreate```
    * You can run these commands separately of course but this is more for your convenience.
* Refer to docker-compose.yml and application.properties if you care to see how the app and db containers are configured.
* Once both containers are successfully up, try interacting with the application via the following curl commands.

**Note**: Though it wasn't required, I added a simple user registration endpoint to have better design around API design.
So before you hit other endpoints, create at least two user accounts with the endpoint /users/register

## APIs

<details>
  <summary>Register User</summary>

----
Returns json data about a single user.

* **URL**

  0.0.0.0:8080/users/register

* **Method:**

  `POST`

* **Request Body**

  **Content:** `{ "username" : String }`

* **Success Response:**

  * **Code:** 201 <br />
    **Content:** `{
    "id": Long,
    "username": String,
    "createdDate": String
    }`

* **Error Response:**

  * **Code:** 409 Conflict <br />
    **Content:** `{ error : "User already exist" }`

* **Sample Call:**

  ```curl
    curl --request POST \
  --url http://0.0.0.0:8080/users/register \
  --header 'content-type: application/json' \
  --data '{username": "example User"}'
  ```
  
</details>

<details>
  <summary>Send Message</summary>

----
Sends and returns json data of the message sent.

* **URL**

  0.0.0.0:8080/messages

* **Method:**

  `POST`

* **Request Body**

  **Content:** `{ 
  "senderUserName": String,
  "recipientId": Long,
  "message": String }`

* **Success Response:**

  * **Code:** 201 <br />
    **Content:** `{
    "id": Long,
    "username": String,
    "createdDate": String
    }`

* **Sample Call:**

  ```curl
    curl --request POST \
  --url http://localhost:8080/messages \
  --header 'content-type: application/json' \
  --data '{
	 "senderUserName": "exampleUser",
	 "recipientId": 1,
	 "message": "Hi!"}'
  ```

</details>

<details>
  <summary>List Chat for User by username</summary>

----
Returns chat formatted as json data for this user.

* **URL**

  0.0.0.0:8080/chat-manager/users/{username}

* **Method:**

  `GET`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `[
     {
      "chatId": Long,
      "otherUser": String,
      "messages": [
        {
          "id": Long,
          "senderId": Long,
          "recipientId": Long,
          "chatId": Long,
          "body": String,
          "createdDate": String
        }
      ]
     }
    ]`

* **Sample Call:**

  ```curl
  curl --request GET \ --url http://localhost:8080/chat-manager/users/exampleUser
  ```

</details>

<details>
  <summary>List Chat for Recipient by Sender</summary>

----
Returns chat formatted as json data for this recipient.

* **URL**

  0.0.0.0:8080/chat-manager/users/{recipient-username}/sender/{sender-username}

* **Method:**

  `GET`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `[
    {
    "chatId": Long,
    "otherUser": String,
    "messages": [
    {
    "id": Long,
    "senderId": Long,
    "recipientId": Long,
    "chatId": Long,
    "body": String,
    "createdDate": String
    }
    ]
    }
    ]`

* **Sample Call:**

  ```curl
  curl --request GET \
  --url http://localhost:8080/chat-manager/users/currentUsername/sender/senderUsername
  ```
</details>

## Tests
Service layer unit tests are written but controllers need more love (time didn't allow)

## Design
* I decided to go with postgres here but one can argue choosing horizontally scalable databases like Cassandra and MangoDB might be more optimal due to the heavy writes when scaled.
* The current implementation to limit the size of the message to 100 and less than 30 days isn't what I would proudly take to prod.
  * We can leverage build in functions of our database to limit the size of messages
  * The same can be done with expiring data based on how old it is
  * Pagination might be an option too, maybe populate each page with 100 messages
* For an ideal messaging application, we might consider storing already delivered messages on the client's phone/device
  * This might be optimal for security concerns as well
* Metrics are critical for observability and therefore any production application should have it
  * I would include it given I had more time
* Some areas where I populated logs may need a bit more reconsideration for security and redundancy reasoning
* Polling of some kind is necessary for the current implementation
  * We'd need to update the data on the client side
    * one solution, which may be naive, is pushing updates of all changed resources to the clients. 
* Rate limiting and circuit breaking needs to be considered as well to ensure the stability of this kind of service
  * Maybe consider libraries that implement ingress/egress rate limiting (not necessarily the same library)
  * Resilience4J is a good option, Bucket4j is another

## Database Tables
There are a total of 3 tables
* user: 
* chat
* message

The chat and the message tables, respectively, have a (parent -> child) bidirectional relationship
This was to make reads/writes easier to implement via jpa/hibernate given the constraint on time

## Additional Notes
I left TODO comments in the parts of the code I would improve on