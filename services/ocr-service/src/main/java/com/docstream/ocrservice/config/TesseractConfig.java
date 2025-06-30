package com.docstream.ocrservice.config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class TesseractConfig {

    @Value("${tesseract.data.path}")
    private String tesseractDataPath; // La ruta del sistema de archivos donde se extraerán los datos

    @Autowired
    private ResourceLoader resourceLoader; // Para acceder a los recursos del classpath

    private static final String TESSDATA_CLASSPATH_DIR = "classpath:tessdata/"; // Directorio de origen en el classpath

    @PostConstruct
    public void setupTesseractData() throws IOException {
        File dataDir = new File(tesseractDataPath);

        // Crear el directorio si no existe
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("Created Tesseract data directory: " + dataDir.getAbsolutePath());
        }

        // Listar los recursos en el directorio tessdata del classpath
        // Nota: Listar recursos de classpath directamente a veces es complicado con JARs.
        // Un enfoque más robusto es listar los recursos conocidos si solo hay unos pocos.
        // Para spa.traineddata, podemos apuntar directamente a él.

        Resource resource = resourceLoader.getResource(TESSDATA_CLASSPATH_DIR + "spa.traineddata");

        if (resource.exists()) {
            File destFile = new File(dataDir, "spa.traineddata");
            if (!destFile.exists() || Files.size(destFile.toPath()) != resource.contentLength()) { // Evita copiar si ya existe y tiene el mismo tamaño
                System.out.println("Extracting spa.traineddata to: " + destFile.getAbsolutePath());
                try (InputStream is = resource.getInputStream();
                     FileOutputStream fos = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                System.out.println("spa.traineddata already exists at " + destFile.getAbsolutePath() + ". Skipping extraction.");
            }
        } else {
            System.err.println("Error: spa.traineddata not found in classpath at " + TESSDATA_CLASSPATH_DIR);
        }

    }
    /**
     * Configuración de Tesseract para usar el idioma español.
     */
     @Bean
     public ITesseract tesseract() {
         Tesseract tesseract = new Tesseract();
         tesseract.setDatapath(tesseractDataPath);
         tesseract.setLanguage("spa");
         return tesseract;
     }
}