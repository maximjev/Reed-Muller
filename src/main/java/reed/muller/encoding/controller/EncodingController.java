package reed.muller.encoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reed.muller.encoding.config.EncodingConfiguration;
import reed.muller.encoding.dto.FormDto;
import reed.muller.encoding.exception.EncodingException;
import reed.muller.encoding.service.BasicScenarioService;
import reed.muller.encoding.service.NoEncodingService;
import reed.muller.encoding.service.ReedMullerService;

import java.util.Map;

import static reed.muller.encoding.utils.CustomStringUtils.calculateErrors;

@Controller
public class EncodingController {

    private EncodingConfiguration configuration;

    private ReedMullerService reedMullerService;

    private NoEncodingService noEncodingService;

    private BasicScenarioService basicScenarioService;

    @Autowired
    public EncodingController(
            ReedMullerService reedMullerService,
            NoEncodingService noEncodingService,
            BasicScenarioService basicScenarioService,
            EncodingConfiguration configuration) {
        this.reedMullerService = reedMullerService;
        this.configuration = configuration;
        this.noEncodingService = noEncodingService;
        this.basicScenarioService = basicScenarioService;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("message", new FormDto());
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());
        return "index";
    }

    @PostMapping("/")
    public String submit(@ModelAttribute FormDto formDto, Map<String, Object> model) {
        model.put("result", reedMullerService.process(formDto.getMessage()));
        model.put("withoutEncoding", noEncodingService.processMessage(formDto.getMessage()));
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());

        return "processed";
    }


    @GetMapping("/vector")
    public String vector(Map<String, Object> model) {
        model.put("message", new FormDto());
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());
        return "vector";
    }

    @PostMapping("/vector")
    public String encodeVector(@ModelAttribute FormDto formDto, Map<String, Object> model) {
        String vector = formDto.getMessage();
        if (vector.length() != (configuration.getM() + 1)) {
            throw new EncodingException("Vector length does not match M parameter");
        }

        String encodedVector = basicScenarioService.encode(vector);
        String sentEncodedVector = basicScenarioService.sendThroughChannel(vector);
        model.put("encodedVector", encodedVector);
        model.put("sentEncodedVector", sentEncodedVector);

        FormDto newForm = new FormDto();
        newForm.setMessage(sentEncodedVector);
        model.put("message", newForm);
        model.put("errors", calculateErrors(encodedVector, sentEncodedVector));
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());

        return "vectorEncoded";
    }

    @PostMapping("/vector/decode")
    public String decodeVector(@ModelAttribute FormDto formDto, Map<String, Object> model) {
        String vector = formDto.getMessage();
        if(vector.length() != Math.pow(2, configuration.getM())) {
            throw new EncodingException("Vector length does not match generator matrix length");
        }

        model.put("decodedVector", basicScenarioService.decode(vector));
        model.put("m", configuration.getM());
        model.put("p", configuration.getChannelProbability());
        return "vectorDecoded";
    }
}
