1. Tạo thư mục
grpc/
│
├── judge.proto
├── a.py

2. Cài thư viện
pip install grpcio grpcio-tools
hoặc
python -m pip install grpcio grpcio-tools

3. Generate file từ proto
python -m grpc_tools.protoc --proto_path=. --python_out=. --grpc_python_out=. judge.proto

4. Sau khi generate sẽ có
grpc/
│
├── judge.proto
├── a.py
├── judge_pb2.py
├── judge_pb2_grpc.py
