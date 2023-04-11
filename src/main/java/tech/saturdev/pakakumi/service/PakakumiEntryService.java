package tech.saturdev.pakakumi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.models.paging.Paged;
import tech.saturdev.pakakumi.models.paging.Paging;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;

@Service
public class PakakumiEntryService {

    @Autowired
    private PakakumiEntryRepository repo;

    public List<PakakumiEntry> getAllEntries() {
        List<PakakumiEntry> entries = repo.findAll();
        return entries;
    }

    public Page<PakakumiEntry> getPagedEntries(int page, int size) {
        Page<PakakumiEntry> entries = repo.findAll(PageRequest.of(page, size));
        System.out.println(entries);
        return entries;
    }

    public Paged<PakakumiEntry> getPage(int pageNumber, int size) {
        Page<PakakumiEntry> entries = repo
                .findAll(PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC,
                        "betTime")));
        return new Paged<>(entries, Paging.of(entries.getTotalPages(), pageNumber,
                size));
    }

}
