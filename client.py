import grpc
import math
import judge_pb2
import judge_pb2_grpc

studentCode = "B22DCVT175"
qCode = "vIhWEBE1"

# get data
channel = grpc.insecure_channel("36.50.135.242:2240")
stub = judge_pb2_grpc.TypedJudgeServiceStub(channel)



# logic
telemetry = res.sensor_telemetry
threshold = telemetry.threshold
readings = telemetry.readings
values = []
for r in readings:
    values.append(r.value)
# average
average = sum(values) / len(values)
# p95
values_sorted = sorted(values)
n = len(values)
index = math.ceil(n * 0.95) - 1
p95 = values_sorted[index]
# anomaly_count
anomaly_count = 0
for v in values:
    if v > threshold:
        anomaly_count += 1
average = round(average, 2)
p95 = round(p95, 2)
####
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