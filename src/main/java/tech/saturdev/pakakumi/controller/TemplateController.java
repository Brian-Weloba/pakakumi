package tech.saturdev.pakakumi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;
import tech.saturdev.pakakumi.service.PakakumiEntryService;
import tech.saturdev.pakakumi.service.RequestService;
import tech.saturdev.pakakumi.service.ScraperStatusService;

@Controller
public class TemplateController {
    @Autowired
    private PakakumiEntryRepository repository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PakakumiEntryService pakakumiEntryService;

    @Autowired
    private ScraperStatusService scraperStatusService;

    @GetMapping("/login")
    public String getHomepage(Model model) {
        return "login";
    }

    @GetMapping("/requests")
    public String getRequests(Model model, @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("requests", requestService.getPage(page, size));
        return "requests";
    }

    @GetMapping("/entries")
    public String ShowTemplate(Model model, @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("entries", pakakumiEntryService.getPage(page, size));
        return "entries";
    }

    @GetMapping("/status")
    public String getStatus(Model model) {
        model.addAttribute("requestCount", scraperStatusService.getRequestCount());
        model.addAttribute("cacheClearCount", scraperStatusService.getCacheClearCount());
        model.addAttribute("browserRestartCount", scraperStatusService.getBrowserRestartCount());
        return "status";
    }

    @GetMapping("/api/status")
    @ResponseBody
    public Map<String, Integer> getStatusJson() {
        Map<String, Integer> status = new HashMap<>();
        status.put("requestCount", scraperStatusService.getRequestCount());
        status.put("cacheClearCount", scraperStatusService.getCacheClearCount());
        status.put("browserRestartCount", scraperStatusService.getBrowserRestartCount());
        return status;
    }

    @GetMapping("/graph/{time}")
    public String getGraphByTime(Model model, @PathVariable int time) {

        Map<String, Double> data = new LinkedHashMap<>();
        Map<String, Integer> playingData = new LinkedHashMap<>();
        Map<String, Integer> onlineData = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Create a DateTimeFormatter for the output time format "HH:mm:ss"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Get the current time and the time x minutes ago
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime xMinutesAgo = currentTime.minusMinutes(time);

        List<PakakumiEntry> bets = repository.findByBetTimeBetween(xMinutesAgo, currentTime);

        for (PakakumiEntry bet : bets) {
            LocalDateTime timestamp = bet.getBetTime();
            String timeString = timestamp.format(formatter);
            LocalDateTime utcTime = LocalDateTime.parse(timeString, formatter);

            // Convert the UTC time to Kenyan Time by adding 3 hours
            LocalDateTime kenyanTime = utcTime.plusHours(3);

            // Format the Kenyan Time as a string using the output formatter
            String outputTimeString = kenyanTime.format(outputFormatter);

            double bustedAt = Double.parseDouble(bet.getBustedAt().replace("x", ""));
            int playing = bet.getPlaying();
            int online = bet.getOnline();

            data.put(outputTimeString, bustedAt);
            playingData.put(outputTimeString, playing);
            onlineData.put(outputTimeString, online);
        }

        model.addAttribute("data", data);
        model.addAttribute("playingData", playingData);
        model.addAttribute("onlineData", onlineData);

        return "graph";
    }

    @GetMapping("/graph")
    public String getGraph(Model model) {
        List<PakakumiEntry> bets = repository.findAll();

        Map<String, Double> data = new LinkedHashMap<>();
        Map<String, Integer> playingData = new LinkedHashMap<>();
        Map<String, Integer> onlineData = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Create a DateTimeFormatter for the output time format "HH:mm:ss"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (PakakumiEntry bet : bets) {
            LocalDateTime timestamp = bet.getBetTime();
            String timeString = timestamp.format(formatter);
            LocalDateTime utcTime = LocalDateTime.parse(timeString, formatter);

            // Convert the UTC time to Kenyan Time by adding 3 hours
            LocalDateTime kenyanTime = utcTime.plusHours(3);

            // Format the Kenyan Time as a string using the output formatter
            String outputTimeString = kenyanTime.format(outputFormatter);

            double bustedAt = Double.parseDouble(bet.getBustedAt().replace("x", ""));
            int playing = bet.getPlaying();
            int online = bet.getOnline();

            data.put(outputTimeString, bustedAt);
            playingData.put(outputTimeString, playing);
            onlineData.put(outputTimeString, online);
        }
        model.addAttribute("data", data);
        model.addAttribute("playingData", playingData);
        model.addAttribute("onlineData", onlineData);

        return "graph";

    }

}
