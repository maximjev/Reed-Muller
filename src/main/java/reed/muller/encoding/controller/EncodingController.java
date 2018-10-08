package reed.muller.encoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.dto.FormDto;
import reed.muller.encoding.exception.EncodingException;
import reed.muller.encoding.service.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class EncodingController {

    private EncodingConfiguration configuration;

    private ReedMullerService reedMullerService;

    @Autowired
    public EncodingController(
            ReedMullerService reedMullerService,
            EncodingConfiguration configuration) {
        this.reedMullerService = reedMullerService;
        this.configuration = configuration;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("message", new FormDto());
        return "index";
    }

    @PostMapping("/encode")
    public String submit(@ModelAttribute FormDto formDto, Map<String, Object> model) {
        model.put("result",reedMullerService.process(formDto.getMessage()));
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());

        return "processed";
    }


    @GetMapping("/image")
    public String image(Map<String, Object> model) {
        return "image";
    }
}
