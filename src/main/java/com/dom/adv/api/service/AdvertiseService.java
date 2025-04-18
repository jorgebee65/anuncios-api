package com.dom.adv.api.service;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import com.dom.adv.api.request.RequestAdvertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdvertiseService {
    Page<AdvertiseSummaryDTO> findAll(Pageable pageable, Boolean active);
    AdvertiseDetailDTO findById(Long id);
    AdvertiseDetailDTO save(RequestAdvertise requestAdvertise);
    void deleteById(Long id);
    AdvertiseDetailDTO createWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException;
    AdvertiseDetailDTO updateWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException;
}
