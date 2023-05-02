package tech.saturdev.pakakumi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.service.PakakumiEntryService;
import tech.saturdev.pakakumi.service.UserService;

@RestController
@RequestMapping("/api/")
public class PakakumiEntityController {

    @Autowired
    private PakakumiEntryService service;

    @Autowired
    private UserService userService;

    @GetMapping("pakakumi-entries")
    public List<PakakumiEntry> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("pakakumi-entries/paged")
    public Page<PakakumiEntry> getPagedEntries(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return service.getPagedEntries(page, size);
    }

    @GetMapping("/AddB")
    public String addBrian() {
        // userService.addUser("Brian", "Test@123", List.of("ADMIN", "USER"));
        return userService.loadUserByUsername("Brian").toString();
    }
}
