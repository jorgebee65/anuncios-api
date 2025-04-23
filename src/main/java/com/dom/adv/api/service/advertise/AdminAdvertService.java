package com.dom.adv.api.service.advertise;

import com.dom.adv.api.dto.AdvertiseDetailDTO;

import java.io.IOException;

public interface AdminAdvertService extends PublicAdvertService {
    AdvertiseDetailDTO approveAdvert(AdvertiseDetailDTO dto) throws IOException;

}
