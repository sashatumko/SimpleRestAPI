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
Due to this I've also included the executable in the target directory.

### API

All endpoints will accept JSON content type ("Content-Type: application/json"), and will respond in
JSON format and with the appropriate HTTP status code.

| Endpoint URI              | accepted request types |
| ------------------------- | ---------------------- |
| `/v1/product`             | GET, POST              |
| `/v1/product/{category}`  | GET                    |

Insertion of a product (POST request to endpoint: `/v1/products`)  
Include the JSON representation of the product to be inserted in the body of the request 
The response will return the result of the insertion (the product entity data itself)
Example request:
```console
$ curl --request POST                            
       --header "Content-Type: application/json" 
       --write-out "%{http_code}\n"              
       --data '{product in JSON}'                
        http://localhost:8080/v1/products
```

Search of products by category (GET request to endpoint: `/v1/products/{category}`)  
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
I have included a Python test script in the testing directory.
Postman and the H2 console are also a convenient way to perform simple tests.
