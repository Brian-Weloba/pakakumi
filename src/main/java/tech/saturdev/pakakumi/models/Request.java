package tech.saturdev.pakakumi.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@Data
@Cacheable
@CacheConfig(cacheNames = "tech.saturdev.pakakumi.models.Request")
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "user_agent")
    private String userAgent;
}
