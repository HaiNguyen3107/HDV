import grpc
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "MkDg85d0"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

data = res.enrollment

missing_courses = []

for course in data.required_courses:
    if course not in data.completed_courses:
        missing_courses.append(course)

missing_courses.sort()

gpa_gap = max(
    0,
    data.min_gpa - data.gpa
)

gpa_gap = round(gpa_gap, 2)

eligible = (
    len(missing_courses) == 0
    and gpa_gap == 0
)

submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        enrollment_answer=judge_pb2.EnrollmentAnswer(
            eligible=eligible,
            missing_courses=missing_courses,
            gpa_gap=gpa_gap
        )
    )
)

print(submit.status)
print(submit.message)