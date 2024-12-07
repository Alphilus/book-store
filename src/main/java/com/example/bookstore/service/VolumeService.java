package com.example.bookstore.service;

import com.example.bookstore.entity.Volumes;
import com.example.bookstore.repository.VolumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolumeService {
    private final VolumeRepository volumeRepository;

    public List<Volumes> getVolumeByBookId(Integer id) {
        return volumeRepository.findByBooksIdOrderByDisplayOrder(id);
    }

}
