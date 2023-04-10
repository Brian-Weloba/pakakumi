package tech.saturdev.pakakumi.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Cacheable
@CacheConfig(cacheNames = "tech.saturdev.pakakumi.models.PakakumiEntry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PakakumiEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "bet_time")
    private LocalDateTime betTime;

    @Column(name = "bet_duration")
    @Getter(AccessLevel.NONE)
    private Duration betDuration;
    @Column(name = "busted_at")
    private String bustedAt;

    @Column(name = "playing")
    private int playing;

    @Column(name = "online")
    private int online;

    public long getBetDuration() {
        return betDuration.toSeconds();
    }

}
