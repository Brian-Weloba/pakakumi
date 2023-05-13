package tech.saturdev.pakakumi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;
import tech.saturdev.pakakumi.security.login.models.User;
import tech.saturdev.pakakumi.service.PakakumiEntryService;
import tech.saturdev.pakakumi.service.RequestService;
import tech.saturdev.pakakumi.service.ScraperStatusService;
import tech.saturdev.pakakumi.service.UserService;
import tech.saturdev.pakakumi.util.PasswordMismatchException;

@Slf4j
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

    @Autowired
    private UserService userService;

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
    public String showTemplate(Model model, @RequestParam(defaultValue = "15") int size,
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

        // Sort the list by the "bustedAt" field in ascending order using the merge sort
        // algorithm
        List<PakakumiEntry> sortedBets = mergeSort(bets);

        // Find the index at which 70% of the values are above the "bustedAt" value
        int index = (int) Math.ceil(0.30 * sortedBets.size());

        // Get the "bustedAt" value at the index
        String value = sortedBets.get(index).getBustedAt();
        Double valueDouble = Double.parseDouble(value.replace("x", ""));

        int countAbove = 0;
        int countBelow = 0;
        for (PakakumiEntry bet : sortedBets) {
            if (Double.parseDouble(bet.getBustedAt().replace("x", "")) > valueDouble) {
                countAbove++;
            } else {
                countBelow++;
            }
        }

        // Calculate percentiles with increments of 10
        Map<Integer, Double> percentiles = new LinkedHashMap<>();
        for (int i = 10; i < 100; i += 10) {
            int percentileIndex = (int) Math.ceil(i / 100.0 * sortedBets.size());
            double percentileValue;
            try {
                percentileValue = Double
                        .parseDouble(sortedBets.get(percentileIndex).getBustedAt().replace("x", ""));
            } catch (IndexOutOfBoundsException e) {
                percentileValue = Double
                        .parseDouble(sortedBets.get(percentileIndex - 1).getBustedAt().replace("x", ""));
            }
            percentiles.put(i, percentileValue);
        }

        model.addAttribute("percentiles", percentiles); // Add the percentiles to the model
        model.addAttribute("betCount", bets.size());
        model.addAttribute("below", countBelow);
        model.addAttribute("above", countAbove);
        model.addAttribute("optimal", value);
        model.addAttribute("data", data);
        model.addAttribute("playingData", playingData);
        model.addAttribute("onlineData", onlineData);

        return "graph";
    }

    // Merge sort implementation
    private List<PakakumiEntry> mergeSort(List<PakakumiEntry> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<PakakumiEntry> left = list.subList(0, mid);
        List<PakakumiEntry> right = list.subList(mid, list.size());

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private List<PakakumiEntry> merge(List<PakakumiEntry> left, List<PakakumiEntry> right) {
        List<PakakumiEntry> merged = new ArrayList<>();

        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            double leftBustedAt = Double.parseDouble(left.get(i).getBustedAt().replace("x", ""));
            double rightBustedAt = Double.parseDouble(right.get(j).getBustedAt().replace("x", ""));
            if (leftBustedAt < rightBustedAt) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
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

    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/user/registration")
    public String processRegistrationForm(@ModelAttribute("user") User user, BindingResult result) {
        try {
            if (result.hasErrors()) {
                log.error("Error submitting registration form: " + result.getAllErrors());
                return "registration";
            }

            // check if the email or username already exist in the database
            if (userService.findByEmail(user.getEmail()) != null) {
                result.rejectValue("email", "EmailExists", "Email address is already registered");
                return "registration"; // show form with email validation error
            }

            if (userService.findByUserName(user.getUserName()) != null) {
                result.rejectValue("userName", "UsernameTaken", "Username is already taken");
                return "registration"; // show form with username validation error
            }

            // save the new user to the database
            userService.saveUser(user);

            log.error("New user registered: " + user.getUserName());
            return "redirect:/login?success"; // redirect to login page

        } catch (PasswordMismatchException e) {
            result.rejectValue("password", "PasswordMismatch", e.getMessage());
            return "registration";
        }
    }

}
