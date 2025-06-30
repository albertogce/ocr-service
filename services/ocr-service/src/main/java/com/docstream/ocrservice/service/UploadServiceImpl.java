package com.docstream.ocrservice.service;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.grpc.FileRequest;
import com.docstream.grpc.UploadResponse;
import com.docstream.grpc.UploadServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadServiceImpl extends UploadServiceGrpc.UploadServiceImplBase {

    @Autowired
    private KafkaTemplate<String, DocumentProcessingEvent> kafkaTemplate;

    @Value("${docstream.topics.output}")
    private String outputQueue;


    @Override
    public void uploadFile(FileRequest request, StreamObserver<UploadResponse> responseObserver) {
        try {
            byte[] content = request.getContent().toByteArray();

            // Construimos el evento de procesamiento del documento
            DocumentProcessingEvent documentProcessingEvent = new DocumentProcessingEvent();
            documentProcessingEvent.setBase64Data(content);
            documentProcessingEvent.setDocumentId(UUID.randomUUID());

            kafkaTemplate.send(outputQueue, documentProcessingEvent);

            UploadResponse response = UploadResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Image uploaded and sent to Kafka successfully!")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("Error processing image upload: " + e.getMessage());
            UploadResponse response = UploadResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to upload image: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
