package eu.ksbrt.shortener.model;

public class Link {
    private final String redirectURI;
    private final String shortenedPath;
    // private final User owner;

    public Link(String redirectURI, String shortenedPath) {
        this.redirectURI = redirectURI;
        this.shortenedPath = shortenedPath;
    }
    
    public String getShortenedPath() {
        return shortenedPath;
    }

    public String getRedirectURI() {
        return redirectURI;
    }
}
