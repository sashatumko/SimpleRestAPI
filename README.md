### Simple Rest API

a very simple Product REST API storing products in an in-memory SQL database (H2).
This project uses Java 8, Maven, and the Spring boot 2.5.3 framework.

### Building and Running 

To build and run a project artifact, run the following commands in the root directory of the repository:
```console
$ mvn package
$ java -jar target/restapi-0.0.1-SNAPSHOT.jar
```

In order for this to work you must have Apache Maven and H2 installed.
I've included the executable in the target directory which can be ran directly.
The server runs on `localhost:8080`. The H2 console can be accessed at `localhost:8080/h2` with JDBC URL `jdbc:h2:mem:products`, username `centric` and no password.

### API

All endpoints will accept JSON content type ("Content-Type: application/json"), and will respond in
JSON format and with the appropriate HTTP status code.

| Endpoint URI              | accepted request types |
| ------------------------- | ---------------------- |
| `/v1/product`             | GET, POST              |
| `/v1/product/{category}`  | GET                    |

- Insert a product by sending a POST request to endpoint: `/v1/products`.
You must include the JSON representation of the product to be inserted in the body of the request.
The response will be the result of the insertion (the product entity data itself, or an error).  
Example request to insert :
```console
$ curl --request POST                            
       --header "Content-Type: application/json" 
       --write-out "%{http_code}\n"              
       --data '{JSON OBJECT}'
        http://localhost:8080/v1/products
```

- Search products by category by sending a GET request to endpoint: `/v1/products/{category}`. 
A list of products will be returned based on exact match on the 
text of the ‘category’ field, listed from newest to oldest based on the 'created_at' field.
This endpoint also supports pagination via the 'page' and 'max' parameters.
Setting the 'max' sets a limit for the maximum number of products to return per page.  
Example request searching database by category 'apparel' on page 1 with page size 25:
```console
$ curl --request GET                            
       --header "Content-Type: application/json" 
       --write-out "%{http_code}\n"              
        http://localhost:8080/v1/products/apparel?page=1&max=25
```

Retrieve all products (GET request to endpoint: `/v1/products`)
```console
$ curl http://localhost:8080/v1/products
```

### Testing
I have included a Python test script in the testing directory which inserts and retrieves a few example products to the database.
I also used Postman and the H2 console as a convenient way to perform simple API tests.
