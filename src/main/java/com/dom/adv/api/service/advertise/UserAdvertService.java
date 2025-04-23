package com.dom.adv.api.service.advertise;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.request.RequestAdvertise;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserAdvertService extends PublicAdvertService {
    AdvertiseDetailDTO save(RequestAdvertise requestAdvertise);
    void deleteById(Long id);
    AdvertiseDetailDTO createWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException;
    AdvertiseDetailDTO updateWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException;
}
