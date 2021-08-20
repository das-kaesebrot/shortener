package eu.ksbrt.shortener.dao;

// import org.hibernate.validator.constraints.Length;

import eu.ksbrt.shortener.model.Link;

import net.bytebuddy.utility.RandomString;

//TODO fix this

public interface LinkDao {
    int insertLink(String redirectURI, Link link);

    default int addLink(Link link) {
        // default length is 5
        String redirectURI = RandomString.make(5);
        return insertLink(redirectURI, link);
    }
}
