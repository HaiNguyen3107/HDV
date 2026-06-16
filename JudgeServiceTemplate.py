import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "YOUR_QCODE"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.JudgeServiceStub(channel)

res = stub.Request(
    judge_pb2.JudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

# ===== LOGIC =====

answer = ...

# ===== SUBMIT =====

submit = stub.Submit(
    judge_pb2.SubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        answer=answer
    )
)

print(submit.status)
print(submit.message)