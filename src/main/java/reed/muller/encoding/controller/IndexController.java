package reed.muller.encoding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("message", "Hello");
        return "index";
    }
}
