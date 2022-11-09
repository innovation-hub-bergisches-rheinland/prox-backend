package de.innovationhub.prox.infra.storage;

import java.io.IOException;

public interface StorageProvider {
  void storeFile(String fileId, byte[] file, String contentType) throws IOException;
  byte[] getFile(String fileId) throws IOException;
  void deleteFile(String fileId);
  String buildUrl(String fileId);
}
