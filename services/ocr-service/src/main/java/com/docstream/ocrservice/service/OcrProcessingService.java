package com.docstream.ocrservice.service;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.commondata.dto.ProcessingStage;
import io.micrometer.core.instrument.MeterRegistry;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class OcrProcessingService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private ITesseract tesseract;

    /**
     * Procesa una imagen para OCR directamente desde byte[].
     *
     * @param event El evento que contiene toda la información.
     * @return El evento actualizado con el texto extraído.
     */
    public DocumentProcessingEvent processOcr(DocumentProcessingEvent event) {
        long startTime = System.nanoTime();

        try {
            // Convertir byte[] a BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(event.getBase64Data());
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new IOException("Could not read image data for document " + event.getDocumentId() + ". Invalid format or corrupted bytes.");
            }

            // Preprocesamiento de la imagen para asegurar la resolución
            BufferedImage processedImage = ensureImageResolution(image, 300); // Asumimos 300 DPI como estándar para OCR

            // Realizar OCR directamente sobre el BufferedImage
            String extractedText = tesseract.doOCR(processedImage);
            event.setExtractedText(extractedText);
            event.setStage(ProcessingStage.OCR);

            meterRegistry.counter("ocr.documents.processed").increment();
        } catch (IOException e) {
            meterRegistry.counter("ocr.errors").increment();
            throw new RuntimeException("Error reading image data for OCR for document " + event.getDocumentId() + ": " + e.getMessage(), e);
        } catch (TesseractException e) {
            meterRegistry.counter("ocr.errors").increment();
            throw new RuntimeException("OCR processing failed for document " + event.getDocumentId() + ": " + e.getMessage(), e);
        } catch (Exception e) {
            meterRegistry.counter("ocr.errors").increment();
            throw new RuntimeException("An unexpected error occurred during OCR for document " + event.getDocumentId() + ": " + e.getMessage(), e);
        } finally {
            long endTime = System.nanoTime();
            meterRegistry.timer("ocr.processing.time").record(endTime - startTime, TimeUnit.NANOSECONDS);
        }

        return event;
    }

    /**
     * Asegura que la imagen tenga una resolución mínima para un OCR óptimo.
     * Si la resolución de la imagen es baja o no está definida, la redimensiona.
     */
    private BufferedImage ensureImageResolution(BufferedImage originalImage, int targetDpi) {
        double currentDpi = 70.0; // Asumimos 70 DPI si Tesseract da el warning
        double scaleFactor = (double) targetDpi / currentDpi;

        if (scaleFactor <= 1.0) { // No escalar si ya tiene una resolución decente o es más grande
            return originalImage;
        }

        int newWidth = (int) (originalImage.getWidth() * scaleFactor);
        int newHeight = (int) (originalImage.getHeight() * scaleFactor);

        if (newWidth == originalImage.getWidth() && newHeight == originalImage.getHeight()) {
            // No hay necesidad de redimensionar si las dimensiones son las mismas
            return originalImage;
        }

        System.out.println("Resizing image from " + originalImage.getWidth() + "x" + originalImage.getHeight() +
                " to " + newWidth + "x" + newHeight + " for better OCR quality.");

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();

        // Configura hints para una mejor calidad de escalado (antialias, etc.)
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

}