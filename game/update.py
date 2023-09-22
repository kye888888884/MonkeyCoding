import requests
import random

file_path = "./sample.jpg"
file = {'file': open(file_path, 'rb')}

my_number = random.randint(1, 100)
payload = {'name': 'kyh', 'answer': my_number}
r = requests.post('http://127.0.0.1:8000/update', params=payload, files=file)

# print(r.url)
print(r.text)
print(r.status_code)