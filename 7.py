import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "yzypRqZY"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

data = res.shipping_quote

best = None
best_fee = 0

for q in data.quotes:
    if q.eta_days <= data.max_eta_days:

        total_fee = q.base_fee + data.weight_kg * q.per_kg_fee
        total_fee = round(total_fee, 2)

        if best is None:
            best = q
            best_fee = total_fee
        elif total_fee < best_fee:
            best = q
            best_fee = total_fee
        elif total_fee == best_fee and q.reliability > best.reliability:
            best = q
            best_fee = total_fee

submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        shipping_quote_answer=judge_pb2.ShippingQuoteAnswer(
            carrier=best.carrier,
            total_fee=best_fee,
            eta_days=best.eta_days
        )
    )
)

print(submit.status)
print(submit.message)