package eu.kaesebrot.dev.shortener.api;

import eu.kaesebrot.dev.shortener.repository.LinkRepository;
import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/s")
@Tag(name = "redirect", description = "Alias to the links redirect")
public class AliasLinkController {
    private final LinkRepository linkRepository;

    AliasLinkController(LinkRepository linkRepository, HexStringGenerator hexStringGenerator) {
        this.linkRepository = linkRepository;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    RedirectView redirectShortUri(@PathVariable String id) throws IOException {
        var link = linkRepository.findById(id).orElseThrow();
        link.incrementHits();
        linkRepository.save(link);

        return new RedirectView(link.getRedirectUri().toString());
    }
}
