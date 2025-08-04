package eu.kaesebrot.dev.shortener.api;
import java.util.UUID;

import eu.kaesebrot.dev.shortener.model.Link;
import eu.kaesebrot.dev.shortener.model.dto.request.LinkRequestCreation;
import eu.kaesebrot.dev.shortener.model.dto.response.LinkResponse;
import eu.kaesebrot.dev.shortener.repository.LinkRepository;
import eu.kaesebrot.dev.shortener.utils.AuthUtils;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAuthority('SCOPE_links')")
    ResponseEntity<Page<LinkResponse>> getAllLinks(@Valid @RequestParam Pageable pageable, final Authentication authentication) {
        boolean isAdmin = AuthUtils.hasScope(authentication, "SCOPE_links_admin");

        if (isAdmin) {
            return ResponseEntity.ok(linkRepository.findAll(pageable).map(Link::toDto));
        }

        return ResponseEntity.ok(linkRepository.findLinksByOwnerUsername(authentication.getName(), pageable).map(Link::toDto));
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

    @GetMapping("{shortUri}/redirect")
    @ResponseStatus(HttpStatus.FOUND)
    @Transactional
    RedirectView redirectShortUri(@PathVariable String shortUri) {
        var link = linkRepository.findByShortUri(shortUri).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The link could not be found"));
        link.incrementHits();
        linkRepository.save(link);

        return new RedirectView(link.getRedirectUri().toString());
    }
}
