package reed.muller.encoding.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.exception.StorageException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import static reed.muller.encoding.utils.FileUtils.resolveExtension;

@Service
public class StorageService {

    private Path rootLocation;

    public StorageService(EncodingConfiguration configuration) {
        this.rootLocation = Paths.get(configuration.getStoragePath());
    }

    public void store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(),
                    this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new StorageException("Failed to store " + file.getOriginalFilename());
        }
    }

    public void store(ByteArrayInputStream imageStream, String filename) {
        try {

            BufferedImage image = ImageIO.read(imageStream);
            ImageIO.write(image, resolveExtension(filename),
                    new File(this.rootLocation.resolve(filename).toAbsolutePath().toString()));
        } catch (IOException ex) {
            throw new StorageException("Failed to store processed file " + filename);
        }
    }

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

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }

}
