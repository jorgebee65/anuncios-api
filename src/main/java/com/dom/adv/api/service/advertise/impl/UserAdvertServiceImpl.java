package com.dom.adv.api.service.advertise.impl;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.entity.Advertise;
import com.dom.adv.api.exception.ResourceNotFoundException;
import com.dom.adv.api.mapper.AdvertiseMapper;
import com.dom.adv.api.repository.AdvertiseRepository;
import com.dom.adv.api.request.RequestAdvertise;
import com.dom.adv.api.security.UserContextService;
import com.dom.adv.api.service.advertise.strategy.AdvertValidationStrategy;
import com.dom.adv.api.service.aws.UploadService;
import com.dom.adv.api.service.advertise.AbstractAdvertService;
import com.dom.adv.api.service.advertise.UserAdvertService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserAdvertServiceImpl extends AbstractAdvertService implements UserAdvertService {

    private final UploadService uploadService;
    private final AdvertValidationStrategy validationStrategy;

    protected UserAdvertServiceImpl(
            AdvertiseRepository advertiseRepository,
            AdvertiseMapper mapper,
            UploadService uploadService,
            UserContextService userContextService, AdvertValidationStrategy validationStrategy
    ) {
        super(advertiseRepository, mapper, userContextService);
        this.uploadService = uploadService;
        this.validationStrategy = validationStrategy;
    }


    @Override
    public AdvertiseDetailDTO save(RequestAdvertise requestAdvertise) {
        var entity = mapper.toEntity(requestAdvertise);
        prepareAdvertiseEntity(entity);
        return persist(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!advertiseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anuncio no encontrado");
        }
        advertiseRepository.deleteById(id);
    }

    @Override
    public AdvertiseDetailDTO createWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException {
        String imageUrl = uploadService.uploadImage(file);
        dto.setImage(imageUrl);

        var entity = mapper.toEntity(dto);
        prepareAdvertiseEntity(entity);
        return persist(entity);
    }

    @Override
    public AdvertiseDetailDTO updateWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException {
        var adExist = advertiseRepository.findById(dto.getId());
        if (adExist.isEmpty()) {
            throw new ResourceNotFoundException("Advertise", dto.getId());
        }
        Advertise existingAdvert = adExist.get();

        validationStrategy.validate(existingAdvert);

        if(Objects.nonNull(file)) {
            String imageUrl = uploadService.uploadImage(file);
            dto.setImage(imageUrl);
        }
        Advertise entity = mapper.toEntity(dto);
        return persist(entity);
    }

}
