package com.docstream.ocrservice.config;

import com.docstream.ocrservice.service.UploadServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    private final UploadServiceImpl uploadService;

    public GrpcServerConfig(UploadServiceImpl uploadService) {
        this.uploadService = uploadService;
    }

    private Server server;

    @PostConstruct
    public void start() throws Exception {
        server = ServerBuilder
                .forPort(50052)
                .addService(uploadService)  // uso del bean inyectado
                .build()
                .start();

        System.out.println("✅ gRPC Server started on port 50052");
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}