import requests
studentCode = "B22DCVT175"
qCode = "6SgtZW8b"

res = requests.get("http://36.50.135.242:2230/api/rest/method",
                   params={"studentCode":studentCode, "qCode":qCode}).json()
requestId = res["requestId"]
data = res["data"]

ans=[]
while len(ans)<len(data["modules"]):
    ans.append(max(
        (m["id"] for m in data["modules"]
         if m["id"] not in ans
         and all(d["before"] in ans for d in data["deps"] if d["after"]==m["id"])),
        key=lambda x: next(m["version"] for m in data["modules"] if m["id"]==x)
    ))

ans=",".join(ans)

body={
    "studentCode":studentCode,
    "qCode":qCode,
    "answer":ans
}
r = requests.put(f"http://36.50.135.242:2230/api/rest/method/{requestId}", json=body)