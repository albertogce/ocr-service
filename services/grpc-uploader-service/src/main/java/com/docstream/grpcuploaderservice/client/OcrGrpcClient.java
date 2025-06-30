package com.docstream.grpcuploaderservice.client;

import com.docstream.grpc.FileRequest;
import com.docstream.grpc.UploadResponse;
import com.docstream.grpc.UploadServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OcrGrpcClient {

    @Value("${ocr.service.host}")
    private String ocrServiceHost;

    private UploadServiceGrpc.UploadServiceBlockingStub stub;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(ocrServiceHost, 50052)
                .usePlaintext()
                .build();

        this.stub = UploadServiceGrpc.newBlockingStub(channel);
    }

    public UploadResponse sendFile(String filename, byte[] content) {
        FileRequest request = FileRequest.newBuilder()
                .setFilename(filename)
                .setContent(com.google.protobuf.ByteString.copyFrom(content))
                .build();

        return stub.uploadFile(request);
    }
}
