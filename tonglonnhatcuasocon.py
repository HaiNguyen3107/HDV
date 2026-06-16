import requests

studentCode = "B22DCVT175"
qCode = "MTDxG6Zt"

res= requests.get("http://36.50.135.242:2230/api/rest/data",
                  params= {
                      "studentCode": studentCode,
                      "qCode": qCode,
                  }).json()
requestId = res["requestId"]
data = res["data"]
values = data["values"]
k = data["k"]

ans=str(
    max(sum(values[i:j])
    for i in range(len(values))
    for j in range(i+1,min(len(values)+1,i+k+1))
))

body = {
    "requestId": requestId,
    "studentCode": studentCode,
    "qCode": qCode,
    "answer": ans
}
r = requests.post("http://36.50.135.242:2230/api/rest/data/submit", json = body)
