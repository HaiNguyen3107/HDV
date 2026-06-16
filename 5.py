import grpc
import re
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "5wtnJgqr"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

counts = {}

for entry in res.text_batch.entries:

    severity = entry.split()[0]

    counts[severity] = counts.get(severity, 0) + 1

first_code = ""

for entry in res.text_batch.entries:

    m = re.search(r'code=([A-Za-z0-9_-]+)', entry)

    if m:
        first_code = m.group(1)
        break

values = []

if first_code:
    values.append(first_code)

submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        text_batch_answer=judge_pb2.TextBatchAnswer(
            counts=counts,
            values=values
        )
    )
)

print(submit.status)
print(submit.message)