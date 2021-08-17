maven project

Spring boot 2.5.3

Java 8



### GOAL

a very simple Product REST API storing data in a SQL database.


### API
- Insertion of a product. 
  The response must return the result of the insertion (the product entity data itself)
  
- Search of products by category, using an exact match on the text of the ‘category’ field,
  supporting pagination by page number and max entries per page. This will return the list of
  products matching the criteria sorted by created_at from newest to oldest.

### API
get all products
```console
curl http://localhost:8080/v1/products
```

insert
```console
curl -i -X POST -H "Content-Type: application/json" -d "@product_example1.json" http://localhost:8080/v1/products
```