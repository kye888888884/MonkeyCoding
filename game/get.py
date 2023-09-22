import requests
  
payload = {'name': 'kyh'}
r = requests.get('http://127.0.0.1:8000', params=payload)
  
print(r.url)
print(r.text)
print(r.status_code)