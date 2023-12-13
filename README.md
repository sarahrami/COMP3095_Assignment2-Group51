GBC SpringSocial Project - COMP3095_Assignment2-Group51
Team Members

    Rauny Martinelli - Student ID: 101333371
    Lucas Furtado - Student ID: 101321576
    Sarah Moustafa - Student ID: 101376641

Overview

This project is part of the COMP 3095 course at George Brown College. It implements a microservices architecture for a social networking platform named GBC_SpringSocial. The system consists of several microservices including:

    api-gateway: The entry point for all service requests.
    post-service: Manages posts created by users.
    user-service: Handles user information and authentication.
    comment-service: Manages comments on posts.
    friendship-service: Handles user connections and friend requests.
    discovery-service: Service discovery to manage microservices instances.

Prerequisites

    Docker
    Java 17
    Maven / kotlin

Setup and Installation

    Clone the repository:

    bash

git clone https://github.com/[your-username]/COMP3095_Assignment2-Group51.git

Navigate to the project directory:

bash

cd COMP3095_Assignment2-Group51

Build the project using Maven:

mvn clean install

Start the services using Docker Compose:

    docker-compose up

Usage

Once the services are up and running, you can access them through the API Gateway at http://localhost:8080.
Services and Routes

    API Gateway: http://localhost:8080
    Post Service: http://localhost:8080/api/post
    User Service: http://localhost:8080/api/users
    Comment Service: http://localhost:8080/api/comments
    Friendship Service: http://localhost:8080/api/friendships

Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.
License

This project is licensed under the MIT License - see the LICENSE.md file for details.
Acknowledgments

    George Brown College
    COMP 3095 Instructors and TAs