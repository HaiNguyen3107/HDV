import hashlib
import hmac
import json

import requests

studentCode = "B22DCVT175"
qCode = "Y3Fk7eRI"

res = requests.get("http://36.50.135.242:2230/api/rest/header",
                   params={"studentCode":studentCode, "qCode": qCode}).json()
requestId = res["requestId"]
data = res["data"]
#####
ans = hmac.new(
    data["signingKey"].encode(),
    f'{data["nonce"]}:{ "|".join(data["events"]) }:{studentCode.upper()}'.encode(),
    hashlib.sha256
).hexdigest()
#######
body = {
    "requestId": requestId,
"studentCode": studentCode,
"qCode": qCode,
"answer": ans}
r = requests.post("http://36.50.135.242:2230/api/rest/header/submit", json = body)