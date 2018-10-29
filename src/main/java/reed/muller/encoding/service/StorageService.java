package reed.muller.encoding.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.exception.StorageException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private Path rootLocation;

    public StorageService(EncodingConfiguration configuration) {
        this.rootLocation = Paths.get(configuration.getStoragePath());
    }

    /*
    * atlieka: išsaugo failą
    */
    public void store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(),
                    this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new StorageException("Failed to store " + file.getOriginalFilename());
        }
    }

    /*
    * atlieka: išsaugo failą
    * ima: baitų seka, failo pavadinimas
    */
    public void store(byte[] bytes, String filename) {
        try {
            Files.write(this.rootLocation.resolve(filename).toAbsolutePath(), bytes);
        } catch (IOException ex) {
            throw new StorageException("Failed to store processed file " + filename);
        }
    }

    /*
    * atlieka: užkrauna failą atvaizdavimui
    */
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Resource already exists");
            }
        } catch (MalformedURLException e) {
            throw new StorageException("resource string could not be parsed");
        }
    }

    /*
    * atlieka: ištrina failus iš direktorijos
    */
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /*
    * atlieka: sukuria direktoriją failų laikymui
    */
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }

}
