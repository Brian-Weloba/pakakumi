package tech.saturdev.pakakumi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;

@Service
public class PakakumiEntryService {

    @Autowired
    private PakakumiEntryRepository repo;

    public List<PakakumiEntry> getAllEntries() {
        List<PakakumiEntry> entries = repo.findAll();
        return entries;
    }

}
