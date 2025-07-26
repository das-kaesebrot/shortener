package eu.kaesebrot.dev.shortener.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
@Tag(name = "ui", description = "The UI controller")
public class UiController {
    @GetMapping("login")
    String getLoginPage() {
        return "login";
    }
}
