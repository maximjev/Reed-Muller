package reed.muller.encoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.service.ReedMullerService;
import reed.muller.encoding.service.StorageService;

import java.util.Map;

@Controller
public class ImageEncodingController {

    @Autowired
    private ReedMullerService reedMullerService;

    @Autowired
    private EncodingConfiguration configuration;

    @Autowired
    private StorageService storageService;

    @GetMapping("/image")
    public String image(Map<String, Object> model) {
        return "image";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Map<String, Object> model) {
        model.put("filename", file.getOriginalFilename());
        model.put("processedFilename", reedMullerService.processImage(file));

        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());
        return "processedImage";
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
