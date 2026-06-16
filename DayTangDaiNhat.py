from bisect import bisect_left

import requests

studentCode = "B22DCVT175"
qCode = "jAtXPoq1"

res = requests.get("http://36.50.135.242:2230/api/rest/data",
                   params= {
                       "studentCode": studentCode,
                       "qCode": qCode
                   }).json()
requestId = res["requestId"]
data = res["data"]
values = data["values"]
print(values)
###
x=[]
for v in data["values"]:
    i=bisect_left(x,v)
    x.append(v) if i==len(x) else x.__setitem__(i,v)

ans=str(len(x))
####

body = {
    "studentCode": studentCode,
    "qCode": qCode,
    "requestId": requestId,
    "answer" : ans
}
r = requests.post("http://36.50.135.242:2230/api/rest/data/submit", json=body)
