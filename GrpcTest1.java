package com.example.quiz;
import GRPC.*;
import java.util.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
public class GrpcTest1 {
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCVT175";
        String qCode = "NqiNgTVq";
        //CONNECT
        ManagedChannel channel =
                ManagedChannelBuilder
                        .forAddress("36.50.135.242",2240)
                        .usePlaintext()
                        .build();

        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub =
                TypedJudgeServiceGrpc.newBlockingStub(channel);

        // REQUEST
        TypedJudgeResponse res =
                stub.requestTyped(
                        TypedJudgeRequest.newBuilder()
                                .setStudentCode(studentCode)
                                .setQuestionAlias(qCode)
                                .build()
                );

        String requestId = res.getRequestId();
        // LOGIC
        TransactionRiskBatchData data =
                res.getTransactionRiskBatch();

        ArrayList<String> ids = new ArrayList<>();
        double total = 0;

        for(TransactionRecord t : data.getTransactionsList()){

            boolean risk =
                    t.getAmount() >= 5000
                            || t.getChargebackCount() >= 2
                            || (
                            t.getNewDevice()
                                    && !t.getCountry().equals("VN")
                    );

            if(risk){
                ids.add(t.getTransactionId());
                total += t.getAmount();
            }
        }
        // SUBMIT
        TypedSubmitResponse submit =
                stub.submitTyped(
                        TypedSubmitRequest.newBuilder()
                                .setStudentCode(studentCode)
                                .setQuestionAlias(qCode)
                                .setRequestId(requestId)
                                .setTransactionRiskAnswer(
                                        TransactionRiskAnswer
                                                .newBuilder()
                                                .addAllHighRiskTransactionIds(ids)
                                                .setReviewCount(ids.size())
                                                .setTotalHighRiskAmount(
                                                        Math.round(total*100.0)/100.0
                                                )
                                                .build()
                                )
                                .build()
                );

        System.out.println(submit.getStatus());
        System.out.println(submit.getMessage());
        channel.shutdown();
    }
}
