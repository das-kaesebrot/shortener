package eu.kaesebrot.dev.shortener.api;
import java.io.IOException;

import eu.kaesebrot.dev.shortener.exceptions.LinkNotFoundException;
import eu.kaesebrot.dev.shortener.model.Link;
import eu.kaesebrot.dev.shortener.model.dto.request.LinkRequestCreation;
import eu.kaesebrot.dev.shortener.repository.LinkRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import eu.kaesebrot.dev.shortener.utils.StringUtils;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("${shortener.hosting.subdirectory:}/api/v1/links")
@Tag(name = "links", description = "The Link API")
@SecurityScheme(
        name = "Authorization",
        scheme = "Bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@RequiredArgsConstructor
public class LinkController {
    private final LinkRepository linkRepository;
    private final RandomStringGenerator randomStringGenerator;

    @GetMapping
    Page<Link> getAllLinks(Pageable pageable) {
        return linkRepository.findAll(pageable);
    }

    @PostMapping
    Link createSingleLink(@Valid @RequestBody LinkRequestCreation linkRequestCreation) {
        String linkId = linkRequestCreation.getId();

        if (StringUtils.isNullOrEmpty(linkId)) {
            do {
                linkId = randomStringGenerator.generate(5);
            } while (linkRepository.existsById(linkId));
        } else if (linkRepository.existsById(linkId)) {
            throw new IllegalArgumentException(String.format("Link with id %s already exists!", linkId));
        }

        Link link = new Link(linkId, linkRequestCreation.getRedirectUri(), null);

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

    @GetMapping("{id}/redirect")
    @ResponseStatus(HttpStatus.FOUND)
    RedirectView redirectShortUri(@PathVariable String id) throws IOException {
        var link = linkRepository.findById(id).orElseThrow();
        link.incrementHits();
        linkRepository.save(link);

        return new RedirectView(link.getRedirectUri().toString());
    }
}
