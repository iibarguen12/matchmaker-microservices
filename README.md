# Matchmaker microservices
**Matchmaker** is a system that helps a company find the best products at the best prices, by matching the products offered by a salesman with the company's set of rules. The system scores each product based on how well it matches the rules, filters the products to only those that pass a certain threshold, and calculates the total and average prices of the matching products. It is designed to be scalable, durable and consistent, able to handle high volumes of data and millions of users.

## Description
This system is split into microservices, allowing for better scalability, fault isolation, and ease of development. Each microservice can be deployed and scaled independently and can quickly add new features and fix bugs without affecting the entire system.

The services are as follows:

* ###### Auth Service: 
    This service is a centralized authentication and authorization server that will be used for creating and validating the JWT (JSON Web Token) tokens.

* ###### API Gateway: 
    This service act as the point of entry for the system. This will route the requests to the corresponding microservices.

* ###### Service Discovery Service: 
    This is the Client-side service discovery allowing the other services to find and communicate with each other without hard-coding the hostname and port.
  
* ###### Product Service: 
  This service is responsible for managing products and their attributes, it is responsible for CRUD operations of products, and it also provides an endpoint that can filter products based on the rules.

* ###### Rule Service: 
    This service is responsible for managing the rules, and it provides an endpoint to add, update, delete and retrieve rules.

* ###### Scoring Service: 
    This service is responsible for calculating the scores of the products based on the rules, it provides an endpoint to calculate the scores and filter the products based on a threshold.

* ###### Pricing Service: 
    This service is responsible for calculating the total and average prices of the products, it provides an endpoint to calculate the prices of the products that have been filtered by the scoring service.

## Communication
The communication between the services is achieved using **Spring Cloud OpenFeign**, a declarative HTTP client that allows for easy communication between microservices using RESTful APIs.

## Storage
For the sake of simplicity, this project uses H2 as a database embedded in the Products service and the Rules service. However, in a real-life scenario where a high volume of data needs to be managed, with more reads than writes and the need for easy scalability, I would recommend using a NoSQL database such as MongoDB or Cassandra. Additionally, it would be beneficial to partition the Products database into categories and the Rules database by scores, using a **Shared Database Pattern**.