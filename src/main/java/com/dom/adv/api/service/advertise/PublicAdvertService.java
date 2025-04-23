package com.dom.adv.api.service.advertise;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicAdvertService {
    Page<AdvertiseSummaryDTO> findAll(Pageable pageable, Boolean active, String category);
    AdvertiseDetailDTO findById(Long id);
}
