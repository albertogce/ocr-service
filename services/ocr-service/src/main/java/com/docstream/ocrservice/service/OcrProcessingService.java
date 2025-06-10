package com.docstream.ocrservice.service;

import com.docstream.ocrservice.dto.DocumentProcessingEvent;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
public class OcrProcessingService {

    private final MeterRegistry meterRegistry;

    public OcrProcessingService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public DocumentProcessingEvent processOcr(DocumentProcessingEvent event) {
        long startTime = System.nanoTime();

        try {
            String extractedText = runTesseract(event.getRawFilePath());
            event.setExtractedText(extractedText);

            meterRegistry.counter("ocr.documents.processed").increment();

        } catch (Exception e) {
            meterRegistry.counter("ocr.errors").increment();
            throw new RuntimeException("OCR processing failed", e);
        } finally {
            long endTime = System.nanoTime();
            meterRegistry.timer("ocr.processing.time").record(endTime - startTime, TimeUnit.NANOSECONDS);
        }

        return event;
    }

    private String runTesseract(String imagePath) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "tesseract",
                imagePath,
                "stdout",
                "-l", "spa"
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Error al ejecutar Tesseract. Código: " + exitCode);
            }

            return output.toString().trim();
        }
    }

}