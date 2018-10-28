package reed.muller.encoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.dto.FormDto;
import reed.muller.encoding.service.NoEncodingService;
import reed.muller.encoding.service.ReedMullerService;

import java.util.Map;

@Controller
public class EncodingController {

    private EncodingConfiguration configuration;

    private ReedMullerService reedMullerService;

    private NoEncodingService noEncodingService;

    @Autowired
    public EncodingController(
            ReedMullerService reedMullerService,
            NoEncodingService noEncodingService,
            EncodingConfiguration configuration) {
        this.reedMullerService = reedMullerService;
        this.configuration = configuration;
        this.noEncodingService = noEncodingService;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("message", new FormDto());
        return "index";
    }

    @PostMapping("/encode")
    public String submit(@ModelAttribute FormDto formDto, Map<String, Object> model) {
        model.put("result",reedMullerService.process(formDto.getMessage()));
        model.put("withoutEncoding", noEncodingService.processMessage(formDto.getMessage()));
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());

        return "processed";
    }

    @GetMapping("/reload")
    public void reload() {

    }
}
