package eu.ksbrt.shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.ksbrt.shortener.dao.LinkDao;
import eu.ksbrt.shortener.model.Link;

@Service
public class LinkService {

    private final LinkDao linkDao;

    @Autowired
    public LinkService(@Qualifier("fakeLinkDao") LinkDao linkDao) {
        this.linkDao = linkDao;
    }
    
    public int addLink(Link link) {
        return linkDao.addLink(link);
    }

}
