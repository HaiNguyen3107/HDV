import requests


studentCode = "B22DCVT175"
qCode = "ESPslZbk"
host = "http://36.50.135.242:2230"

# Phase 1: GET dữ liệu
res = requests.get(
    f"{host}/api/rest/method",
    params={
        "studentCode": studentCode,
        "qCode": qCode
    }
).json()

requestId = res["requestId"]
data = res["data"]

tasks = data["tasks"]
deps = data["deps"]

# Topological Sort (Kahn)
ans = []
while len(ans) < len(tasks):
    for t in tasks:
        if t not in ans and all(d["before"] in ans for d in deps if d["after"] == t):
            ans.append(t)

answer = ",".join(ans)

# Phase 2: PUT kết quả
body = {
    "studentCode": studentCode,
    "qCode": qCode,
    "answer": answer
}

r = requests.put(
    f"{host}/api/rest/method/{requestId}",
    json=body
)

print("Status:", r.status_code)
print(r.text)