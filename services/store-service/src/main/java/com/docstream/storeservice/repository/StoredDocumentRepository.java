package com.docstream.storeservice.repository;

import com.docstream.storeservice.dto.StoredDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoredDocumentRepository extends JpaRepository<StoredDocument, UUID> {

}
