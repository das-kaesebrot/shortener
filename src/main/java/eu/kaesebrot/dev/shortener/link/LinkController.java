package eu.kaesebrot.dev.shortener.link;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import eu.kaesebrot.dev.shortener.exceptions.LinkNotFoundException;
import eu.kaesebrot.dev.shortener.model.Link;
import eu.kaesebrot.dev.shortener.model.LinkCreation;
import eu.kaesebrot.dev.shortener.service.LinkRepository;
import eu.kaesebrot.dev.shortener.util.ShortUriGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shortener")
@Tag(name = "link", description = "The Link API")
public class LinkController {
    private final LinkRepository _linkRepository;
    private final ShortUriGenerator _shortUriGenerator;
    
    LinkController(LinkRepository repository, ShortUriGenerator shortUriGenerator) {
        _linkRepository = repository;
        _shortUriGenerator = shortUriGenerator;
    }

    @GetMapping("links")
    Set<Link> getAllLinks() {
        return new HashSet<>(_linkRepository.findAll());
    }

    @PostMapping("links")
    Link createSingleLink(LinkCreation linkCreation) {
        String generatedUri = null;
        do {
            generatedUri = _shortUriGenerator.generate(5);
        } while (_linkRepository.existsByShortUri(generatedUri));

        Link link = new Link(generatedUri, linkCreation.getUrl(), null);

        _linkRepository.save(link);

        return link;
    }

    @GetMapping("links/{id}")
    Link getSingleLink(@PathVariable Long id) {
        return _linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    }

    @GetMapping("/redirect/{shortUri}")
    void redirectShortUri(HttpServletResponse response, @PathVariable String shortUri) throws IOException {
        var link = _linkRepository.findByShortUri(shortUri).orElseThrow();

        response.sendRedirect(link.getFullURI().toString());
    }
}
