import unittest
import subprocess
import requests
import json

localhost = "localhost" 
port = 8080

class Client():

	def insertProduct(self, product):
		result = requests.post('http://%s:%s/v1/products'%(localhost, str(port)),
							  json = {
								  'name':product['name'], 
								  'description': product['description'], 
								  'brand': product['brand'], 
								  'tags': product['tags'], 
								  'category': product['category']},
							  headers = {"Content-Type": "application/json"})
		print("\nINSERT PRODUCT\n%s\n"%str(json.dumps(product, indent=2)))
		return result

	def getAllProducts(self):
		result = requests.get('http://%s:%s/v1/products'%(localhost, str(port)),
							  json = {},
							  headers = {"Content-Type": "application/json"})
		#print("GET all products result %s"%str(result.content))
		return result

    # def getProduct(self, category, pageNumber, maxPages):
	# 	result = requests.get('http://%s:%s/v1/products/%s?page=%s&max=%s'%(localhost, str(port), category, str(pageNumber), str(maxPages)),
	# 						  json = {},
	# 						  headers = {"Content-Type": "application/json"})
	# 	print("GET all products result %s"%str(result.content))
	# 	return result

client = Client()


Product1 = {
	'name': 'Red Shirt', 
	'description': 'Red Hugo Boss shirt', 
	'brand': 'hugo boss',
	'tags': ['red', 'shirt', 'hugo'],
	'category': 'apparel'
}

class TestAPI(unittest.TestCase):

	def checkResponseBody(self, result, product):
		resJson = result.json()
		print("RESPONSE:\n", json.dumps(resJson, indent=2))
		self.assertEqual(resJson.get("name"), product['name'])
		self.assertEqual(resJson.get("description"), product['description'])
		self.assertEqual(resJson.get("brand"), product['brand'])
		self.assertEqual(resJson.get("tags"), product['tags'])
		self.assertEqual(resJson.get("category"), product['category'])

	def checkStatusCode(self, result, code):
		print("\nSTATUS CODE: %s EXPECTED: %s\n"%(str(result.status_code), code))

	def test1(self):
		result = client.insertProduct(Product1)
		self.checkResponseBody(result, Product1)
		self.checkStatusCode(result, 201)



if __name__ == '__main__':
	unittest.main()