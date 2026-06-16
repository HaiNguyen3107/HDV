import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "NqiNgTVq"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

# ===== LOGIC =====

ids = []
total = 0

for t in res.transaction_risk_batch.transactions:

    if (
        t.amount >= 5000
        or t.chargeback_count >= 2
        or (t.new_device and t.country != "VN")
    ):
        ids.append(t.transaction_id)
        total += t.amount

review_count = len(ids)
total = round(total, 2)

# ===== SUBMIT =====

submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        transaction_risk_answer=judge_pb2.TransactionRiskAnswer(
            high_risk_transaction_ids=ids,
            review_count=review_count,
            total_high_risk_amount=total
        )
    )
)

print(submit.status)
print(submit.message)