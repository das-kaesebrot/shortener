package eu.kaesebrot.dev.shortener.link;

public class LinkNotFoundException extends RuntimeException {
    LinkNotFoundException(String id) {
        super("Could not find link with id " + id);
    }
}
