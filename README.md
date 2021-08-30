### Simple Rest API

A REST API for storing products in an in-memory SQL database (H2).
This project uses Java with Gradle and the Spring boot framework.

### Building and Running 

To build and run a project artifact, run the following commands in the root directory of the repository:
```console
$ gradle clean build
$ java -jar .\build\libs\restapi-0.0.1-SNAPSHOT.jar --server.port=8080
```

In order for this to work you must have Gradle installed, but I've included the executable in the target directory which can be ran directly assuming H2 is installed.
The server runs on port 8080 by default. The H2 console can be accessed at `localhost:8080/h2` with JDBC URL `jdbc:h2:mem:products`, username `centric` and no password.

### API

All endpoints will accept JSON content type ("Content-Type: application/json"), and will respond in
JSON format and with the appropriate HTTP status code.

| Endpoint URI              | accepted request types |
| ------------------------- | ---------------------- |
| `/v1/product`             | GET, POST              |

- Insert a product by sending a POST request to endpoint: `/v1/products`.
You must include the JSON representation of the product to be inserted in the body of the request.
The response will be the result of the insertion (the product entity data itself, or an error).  
Example request to insert :
```console
$ curl --request POST                            
       --header "Content-Type: application/json"              
       --data '{JSON OBJECT}'
        http://localhost:8080/v1/products
```

- Search products by category by sending a GET request to endpoint: `/v1/products`. 
There are 4 optional parameters: category, page, max, and sort.
Products are searched for by exact match to the category parameter. Pages are numbered starting at 1, 
and the `max` parameter specifies a limit on products listed per page. The `sort` parameter 
is used to sort products in either descending (-) or ascending (+) order via the specified field.
Example request searching database by category 'apparel' on page 1 
with page size 25, sorted by descending creation time:
```console
$ curl --request GET                            
       --header "Content-Type: application/json"             
        http://localhost:8080/v1/products?category=apparel&page=1&max=25&sort=-createdAt
```

### Testing
I have included a Python test script in the testing directory which inserts and retrieves a few example products to the database.
