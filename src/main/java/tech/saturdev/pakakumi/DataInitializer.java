package tech.saturdev.pakakumi;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PakakumiEntryRepository repository;

    @Override
    public void run(String... args) throws Exception {
        PakakumiEntry entry1 = new PakakumiEntry();
        entry1.setBetTime(LocalDateTime.now());
        entry1.setBetDuration(Duration.ofMinutes(15));
        entry1.setBustedAt("1.9");
        entry1.setPlaying(1);
        entry1.setOnline(0);
        repository.save(entry1);

        PakakumiEntry entry2 = new PakakumiEntry();
        entry2.setBetTime(LocalDateTime.now());
        entry2.setBetDuration(Duration.ofMinutes(20));
        entry2.setBustedAt("2.5");
        entry2.setPlaying(0);
        entry2.setOnline(1);
        repository.save(entry2);
    }

}
