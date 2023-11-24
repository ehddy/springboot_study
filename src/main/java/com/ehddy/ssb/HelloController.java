package com.ehddy.ssb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "안녕하새요 sbb에 오신것을 환영합니다. ";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
