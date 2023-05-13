package tech.saturdev.pakakumi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tech.saturdev.pakakumi.models.Request;
import tech.saturdev.pakakumi.models.paging.Paged;
import tech.saturdev.pakakumi.models.paging.Paging;
import tech.saturdev.pakakumi.repository.RequestRepository;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repo;

    public List<Request> getAllRequests() {
        List<Request> requests = repo.findAll();
        return requests;
    }

    public Page<Request> getPagedRequests(int page, int size) {
        Page<Request> requests = repo.findAll(PageRequest.of(page, size));
        // System.out.println(requests);
        return requests;
    }

    public Paged<Request> getPage(int pageNumber, int size) {
        Page<Request> requests = repo
                .findAll(PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC,
                        "time")));
        return new Paged<>(requests, Paging.of(requests.getTotalPages(), pageNumber,
                size));
    }

}
