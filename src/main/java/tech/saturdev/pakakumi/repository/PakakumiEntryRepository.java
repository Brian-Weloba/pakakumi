package tech.saturdev.pakakumi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.saturdev.pakakumi.models.PakakumiEntry;

public interface PakakumiEntryRepository extends JpaRepository<PakakumiEntry, UUID> {

    List<PakakumiEntry> findAll();

    Page<PakakumiEntry> findAll(Pageable pageable);

    List<PakakumiEntry> findByBetTimeGreaterThan(LocalDateTime numberOfHours);

    List<PakakumiEntry> findByBetTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
