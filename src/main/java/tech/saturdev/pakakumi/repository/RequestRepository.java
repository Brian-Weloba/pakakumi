package tech.saturdev.pakakumi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.saturdev.pakakumi.models.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findAll(Pageable pageable);

    List<Request> findAll();

}
