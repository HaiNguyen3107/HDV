import grpc
import math
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "vIhWEBE1"

channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)

res = stub.RequestTyped(
    judge_pb2.TypedJudgeRequest(
        student_code=studentCode,
        question_alias=qCode
    )
)

values = [r.value for r in res.sensor_telemetry.readings]

average = round(sum(values) / len(values), 2)

p95 = round(
    sorted(values)[math.ceil(len(values) * 0.95) - 1],
    2
)

anomaly_count = sum(
    1 for v in values
    if v > res.sensor_telemetry.threshold
)

# submit
submit = stub.SubmitTyped(
    judge_pb2.TypedSubmitRequest(
        student_code=studentCode,
        question_alias=qCode,
        request_id=res.request_id,
        sensor_telemetry_answer=judge_pb2.SensorTelemetryAnswer(
            average=average,
            p95=p95,
            anomaly_count=anomaly_count
        )
    )
)

print(submit.status)
print(submit.message)