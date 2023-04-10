package de.innovationhub.prox.infra.aws;

import java.io.IOException;

public interface StorageProvider {
  void storeFile(String fileId, byte[] file, String contentType) throws IOException;
  String buildUrl(String fileId);
}
