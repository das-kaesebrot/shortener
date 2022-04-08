package de.ksbrt.shortener.link;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class LinkController {
    private final LinkRepository _linkRepository;
    
    LinkController(LinkRepository repository) {
        _linkRepository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/links")
    Set<Link> getAllLinks() {
        return new HashSet<>(_linkRepository.findAll());
    }
    // end::get-aggregate-root[]

    @GetMapping("/links/{id}")
    Link getSingleLink(@PathVariable String id) {
        return _linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    }
}
