import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "YOUR_QCODE"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

# ===== LOGIC =====

submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        ANSWER_FIELD=judge_pb2.ANSWER_TYPE(
            ...
        )
    )
)

print(submit.status)
print(submit.message)