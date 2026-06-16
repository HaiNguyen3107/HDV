import requests



studentCode = "B22DCVT175"
qCode = "???"
host="http://36.50.135.242:2230"

#GET
res = requests.get("http://36.50.135.242:2230/api/rest/method",
                   params={"studentCode":studentCode,"qCode":qCode}).json()

requestId = res["requestId"]
data = res["data"]

#LOGIC
ans = ""

#POST | PUT
body = {
    "studentCode":studentCode,
    "qCode":qCode,
    "answer":ans,
    "requestId":requestId
}
r = requests.post("http://36.50.135.242:2230/api/rest/", json=body)

