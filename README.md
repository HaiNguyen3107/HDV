
pip install grpcio grpcio-tools
(python -m pip install grpcio grpcio-tools)

python -m grpc_tools.protoc --proto_path=. --python_out=. --grpc_python_out=. judge.proto

