package eu.kaesebrot.dev.shortener.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("${shortener.hosting.subdirectory:}/${shortener.hosting.redirect-alias:s}")
@Tag(name = "redirect", description = "Alias to the links redirect")
@RequiredArgsConstructor
public class AliasLinkController {
    private final LinkController linkController;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    RedirectView redirectShortUri(@PathVariable String id) throws IOException {
        return linkController.redirectShortUri(id);
    }
}
