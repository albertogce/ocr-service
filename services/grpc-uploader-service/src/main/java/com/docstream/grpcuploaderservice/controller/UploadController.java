package com.docstream.grpcuploaderservice.controller;

import com.docstream.grpc.UploadResponse;
import com.docstream.grpcuploaderservice.client.OcrGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private final OcrGrpcClient grpcClient;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        byte[] content = file.getBytes();

        UploadResponse response = grpcClient.sendFile(filename, content);

        if (response.getSuccess()) {
            return ResponseEntity.ok("Archivo enviado correctamente al OCR");
        } else {
            return ResponseEntity.status(500).body("Fallo al enviar al OCR");
        }
    }
}
