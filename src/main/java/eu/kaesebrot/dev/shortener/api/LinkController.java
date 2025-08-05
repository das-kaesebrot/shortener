package eu.kaesebrot.dev.shortener.api;
import java.util.UUID;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.Link;
import eu.kaesebrot.dev.shortener.model.dto.request.LinkRequestCreation;
import eu.kaesebrot.dev.shortener.model.dto.request.LinkRequestPatch;
import eu.kaesebrot.dev.shortener.model.dto.response.LinkResponse;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
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
    private final AuthUserRepository authUserRepository;

    @GetMapping
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<Page<LinkResponse>> getAllLinks(@Valid @RequestParam Pageable pageable, final Authentication authentication) {
        boolean isAdmin = AuthUtils.hasScope(authentication, "SCOPE_links_admin");

        if (isAdmin) {
            return ResponseEntity.ok(linkRepository.findAll(pageable).map(Link::toDto));
        }

        return ResponseEntity.ok(linkRepository.findLinksByOwnerId(UUID.fromString(authentication.getName()), pageable).map(Link::toDto));
    }

    @PostMapping
    @Transactional
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<LinkResponse> createSingleLink(@Valid @RequestBody LinkRequestCreation linkRequestCreation, final Authentication authentication) {
        String linkId = linkRequestCreation.shortUri();

        if (StringUtils.isNullOrEmpty(linkId)) {
            do {
                linkId = randomStringGenerator.generate(5);
            } while (linkRepository.existsByShortUri(linkId));
        } else if (linkRepository.existsByShortUri(linkId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Link with id %s already exists!", linkId));
        }

        final AuthUser owner = authUserRepository.findById(UUID.fromString(authentication.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found!"));
        Link link = new Link(linkId, linkRequestCreation.shortUri(), owner);
        linkRepository.save(link);

        return ResponseEntity.ok(link.toDto());
    }

    @GetMapping("{id}")
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<LinkResponse> getSingleLink(@PathVariable UUID id, final Authentication authentication) {
        boolean isAdmin = AuthUtils.hasScope(authentication, "SCOPE_links_admin");

        if (!linkRepository.existsByIdAndOwnerId(id, UUID.fromString(authentication.getName())) || (isAdmin && !linkRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The link could not be found");
        }

        return ResponseEntity.ok(linkRepository.findById(id).get().toDto());
    }

    @PatchMapping("{id}")
    @SecurityRequirement(name = "Authorization")
    @Transactional
    ResponseEntity<LinkResponse> updateLink(@PathVariable UUID id, @Valid @RequestBody LinkRequestPatch linkRequestPatch, final Authentication authentication) {
        boolean isAdmin = AuthUtils.hasScope(authentication, "SCOPE_links_admin");

        if (!linkRepository.existsByIdAndOwnerId(id, UUID.fromString(authentication.getName())) || (isAdmin && !linkRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The link could not be found");
        }

        Link link = linkRepository.findById(id).get();

        if (linkRequestPatch.shortUri() != null && !linkRequestPatch.shortUri().equals(link.getShortUri())) {
            link.setShortUri(linkRequestPatch.shortUri());
        }

        if (linkRequestPatch.redirectUri() != null && !linkRequestPatch.redirectUri().equals(link.getRedirectUri().toString())) {
            link.setRedirectUri(linkRequestPatch.redirectUri());
        }

        if (linkRequestPatch.ownerUsername() != null && !linkRequestPatch.ownerUsername().equals(link.getOwner().getUsername())) {
            AuthUser newOwner = authUserRepository.findByUsername(linkRequestPatch.ownerUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found!"));
            link.setOwner(newOwner);
        }

        return ResponseEntity.ok(linkRepository.save(link).toDto());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Authorization")
    @Transactional
    ResponseEntity deleteLink(@PathVariable UUID id, final Authentication authentication) {
        boolean isAdmin = AuthUtils.hasScope(authentication, "SCOPE_links_admin");

        if (!linkRepository.existsByIdAndOwnerId(id, UUID.fromString(authentication.getName())) || (isAdmin && !linkRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The link could not be found");
        }

        linkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
