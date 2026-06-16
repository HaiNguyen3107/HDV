from itertools import combinations

import requests

studentCode = "B22DCVT175"
qCode = "1EALsgYZ"


res = requests.get("http://36.50.135.242:2230/api/rest/object",
                   params={
                       "studentCode":studentCode,
                       "qCode":qCode
                   }).json()

requestId = res["requestId"]
data = res["data"]

capacity = data["capacity"]
items = data["items"]
print(capacity)
print(items)

best=(0,[])
for r in range(len(items)+1):
    for c in combinations(items,r):
        w=sum(i["weight"] for i in c)
        v=sum(i["value"] for i in c)
        if w<=capacity and v>best[0]:
            best=(v,[i["id"] for i in c])

ans=",".join(best[1])+"|"+str(best[0])

body = {
    "studentCode":studentCode,
    "qCode":qCode,
    "requestId":requestId,
    "answer":ans
}
r = requests.post("http://36.50.135.242:2230/api/rest/object/submit", json=body)
