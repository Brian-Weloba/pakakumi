package tech.saturdev.pakakumi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;

@RestController
public class PakakumiEntityController {

    @Autowired
    private PakakumiEntryRepository repository;

    @GetMapping("/api/pakakumi-entries")
    public List<PakakumiEntry> getAllEntries() {
        return repository.findAll();
    }

}
