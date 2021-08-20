package eu.ksbrt.shortener.dao;

import eu.ksbrt.shortener.model.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository("fakeLinkDao")
public class LinkDataAccesss implements LinkDao {
    private static List<Link> DB = new ArrayList<>();
    
    @Override
    public int insertLink(String redirectURI, Link link) {
        DB.add(new Link(redirectURI, link.getShortenedPath()));
        return 1;
    }
    
}
