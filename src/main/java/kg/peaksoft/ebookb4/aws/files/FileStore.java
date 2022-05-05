package kg.peaksoft.ebookb4.aws.files;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileStore {

    void save(String path,
              String fileName,
              Optional<Map<String, String>> optionalMetadata,
              InputStream inputStream
    );

    byte[] download(String path, String key);

    void delete(String filePath, String fileName);
}
