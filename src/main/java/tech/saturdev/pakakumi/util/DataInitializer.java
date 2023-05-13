// package tech.saturdev.pakakumi.util;

// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import tech.saturdev.pakakumi.models.PakakumiEntry;
// import tech.saturdev.pakakumi.models.User;
// import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;
// import tech.saturdev.pakakumi.service.UserService;

// @Component
// public class DataInitializer implements CommandLineRunner {

// @Autowired
// private PakakumiEntryRepository repository;

// @Autowired
// private UserService userService;

// @Override
// public void run(String... args) throws Exception {
// // Initialize PakakumiEntry data
// // PakakumiEntry entry1 = new PakakumiEntry();
// // entry1.setBetTime(LocalDateTime.now());
// // entry1.setBetDuration(Duration.ofMinutes(15));
// // entry1.setBustedAt("1.9");
// // entry1.setPlaying(1);
// // entry1.setOnline(0);
// // repository.save(entry1);

// // PakakumiEntry entry2 = new PakakumiEntry();
// // entry2.setBetTime(LocalDateTime.now());
// // entry2.setBetDuration(Duration.ofMinutes(20));
// // entry2.setBustedAt("2.5");
// // entry2.setPlaying(0);
// // entry2.setOnline(1);
// // repository.save(entry2);

// // Initialize User data
// // Add user Brian with password Test@123 and roles "ADMIN" and "USER"
// userService.addUser("Brian", "Test@123", List.of("ADMIN", "USER"));

// // Add additional users for testing as needed
// userService.addUser("Alice", "Password123", List.of("USER"));
// }

// }
