package eu.kaesebrot.dev.shortener.api;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import eu.kaesebrot.dev.shortener.exceptions.LinkNotFoundException;
import eu.kaesebrot.dev.shortener.model.Link;
import eu.kaesebrot.dev.shortener.model.LinkCreation;
import eu.kaesebrot.dev.shortener.repository.LinkRepository;
import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;
import eu.kaesebrot.dev.shortener.utils.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/shortener/links")
@Tag(name = "links", description = "The Link API")
public class LinkController {
    private final LinkRepository linkRepository;
    private final HexStringGenerator hexStringGenerator;
    
    LinkController(LinkRepository linkRepository, HexStringGenerator hexStringGenerator) {
        this.linkRepository = linkRepository;
        this.hexStringGenerator = hexStringGenerator;
    }

    @GetMapping
    Set<Link> getAllLinks() {
        return new HashSet<>(linkRepository.findAll());
    }

    @PostMapping
    Link createSingleLink(@Valid @RequestBody LinkCreation linkCreation) {
        String linkId = linkCreation.getId();

        if (StringUtils.isNullOrEmpty(linkId)) {
            do {
                linkId = hexStringGenerator.generate(5);
            } while (linkRepository.existsById(linkId));
        } else if (linkRepository.existsById(linkId)) {
            throw new IllegalArgumentException(String.format("Link with id %s already exists!", linkId));
        }

        Link link = new Link(linkId, linkCreation.getRedirectUri(), null);

        linkRepository.save(link);

        return link;
    }

    @GetMapping("{id}")
    Link getSingleLink(@PathVariable String id) {
        return linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLink(@PathVariable String id) {
        if (!linkRepository.existsById(id)) {
            throw new LinkNotFoundException(id);
        }

        linkRepository.deleteById(id);
    }

    @GetMapping("redirect/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    RedirectView redirectShortUri(@PathVariable String id) throws IOException {
        var link = linkRepository.findById(id).orElseThrow();
        link.incrementHits();
        linkRepository.save(link);

        return new RedirectView(link.getRedirectUri().toString());
    }
}
