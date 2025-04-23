package com.dom.adv.api.service.advertise.impl;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.mapper.AdvertiseMapper;
import com.dom.adv.api.repository.AdvertiseRepository;
import com.dom.adv.api.security.UserContextService;
import com.dom.adv.api.service.advertise.AbstractAdvertService;
import com.dom.adv.api.service.advertise.AdminAdvertService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AdminAdvertServiceImpl extends AbstractAdvertService implements AdminAdvertService {

    public AdminAdvertServiceImpl(
            AdvertiseRepository advertiseRepository,
            AdvertiseMapper mapper,
            UserContextService userContextService) {
        super(advertiseRepository, mapper, userContextService);
    }

    @Override
    public AdvertiseDetailDTO approveAdvert(AdvertiseDetailDTO dto) throws IOException {
        dto.setActive(true);
        var entity = mapper.toEntity(dto);
        prepareAdvertiseEntity(entity);
        return persist(entity);
    }
}
