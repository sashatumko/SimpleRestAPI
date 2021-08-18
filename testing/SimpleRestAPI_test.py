import unittest
import subprocess
import requests
import json
import os

localhost = "localhost" 
port = 8080
products = []       # list of JSON objects loaded from /products subdir
productCount = 0

def incrementProductCount():
    global productCount
    productCount += 1

def loadExampleProducts():
    for filename in os.listdir("products"):
        f = open(os.path.join("products", filename))
        products.append(json.load(f))
        f.close()

class Client():

    def getAllProducts(self):
        result = requests.get('http://%s:%s/v1/products'%(localhost, str(port)),
            json = {},
            headers = {"Content-Type": "application/json"})
        print("\nGet all products:\n")
        return result

    def getProduct(self, category, pageNumber, maxPages):
        result = requests.get('http://%s:%s/v1/products/%s?page=%s&max=%s'%(localhost, str(port), category, str(pageNumber), str(maxPages)),
            json = {},
            headers = {"Content-Type": "application/json"})
        return result

    def insertProduct(self, product):
        result = requests.post('http://%s:%s/v1/products'%(localhost, str(port)),
            json = {
                'name':product['name'],
                'description': product['description'],
                'brand': product['brand'],
                'tags': product['tags'],
                'category': product['category']
            },
            headers = {"Content-Type": "application/json"})
        return result

client = Client()

class TestAPI(unittest.TestCase):

    # asserts that response object matches product
    def checkResponseBody(self, resJson, product):
        self.assertEqual(resJson.get("name"), product['name'])
        self.assertEqual(resJson.get("description"), product['description'])
        self.assertEqual(resJson.get("brand"), product['brand'])
        self.assertEqual(resJson.get("tags"), product['tags'])
        self.assertEqual(resJson.get("category"), product['category'])

    # checks for correct status code
    def checkStatusCode(self, result, code):
        self.assertEqual(result.status_code, code)

    # Test 1: insert one product
    def test1(self):
        result = client.insertProduct(products[0])
        self.checkResponseBody(result.json(), products[0])
        self.checkStatusCode(result, 201)
        incrementProductCount()

    # Test 2: insert one product and fetch via GET 
    def test2(self):
        result = client.insertProduct(products[1])
        self.checkResponseBody(result.json(), products[1])
        self.checkStatusCode(result, 201)
        incrementProductCount()

        result = client.getProduct('apparel', 0, 1)
        resJson = result.json()
        self.checkStatusCode(result, 200)
        # assert that only 1 object is returned (since page limit was 1)
        responseProductList = resJson['products']
        self.assertEqual(len(responseProductList), 1) 
        self.checkResponseBody(responseProductList[0], products[1])
        
    # Test 3: insert 5 products
    def test3(self):
        for i in range (2, 7):
            result = client.insertProduct(products[i])
            self.checkStatusCode(result, 201)
            incrementProductCount()
        result = client.getAllProducts()
        self.assertEqual(len(result.json()), productCount) 
        self.checkStatusCode(result, 200)

    # Test 4: get products by category 'apparel' (4 total)
    def test4(self):
        result = client.getProduct('apparel', 0, 4)
        resJson = result.json()
        self.checkStatusCode(result, 200)
        responseProductList = resJson['products']
        self.assertEqual(len(responseProductList), 4)  # since 4 items in apparel category
        
if __name__ == '__main__':
    loadExampleProducts()
    unittest.main()