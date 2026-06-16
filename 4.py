import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "kuzpwj8V"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

# ===== LOGIC =====

tags = ["account", "payment", "refund", "shipping"]

counts = {tag: 0 for tag in tags}

for entry in res.text_batch.entries:

    text = entry.lower()

    for tag in tags:
        if tag in text:
            counts[tag] += 1

values = []

for tag in sorted(tags):
    if counts[tag] > 0:
        values.append(tag)

# ===== SUBMIT =====

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