package com.dom.adv.api.service.impl;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import com.dom.adv.api.entity.Advertise;
import com.dom.adv.api.exception.ResourceNotFoundException;
import com.dom.adv.api.mapper.AdvertiseMapper;
import com.dom.adv.api.repository.AdvertiseRepository;
import com.dom.adv.api.request.RequestAdvertise;
import com.dom.adv.api.service.AdvertiseService;
import com.dom.adv.api.service.S3UploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private final AdvertiseRepository repository;
    private final AdvertiseMapper mapper;
    private final S3UploadService s3UploadService;

    public AdvertiseServiceImpl(AdvertiseRepository repository, AdvertiseMapper mapper, S3UploadService s3UploadService) {
        this.repository = repository;
        this.mapper = mapper;
        this.s3UploadService = s3UploadService;
    }

    public Page<AdvertiseSummaryDTO> findAll(Pageable pageable, Boolean active) {
        return Optional.ofNullable(active)
                .map(a -> repository.findAllByActive(a, pageable))
                .orElseGet(() -> repository.findAll(pageable))
                .map(mapper::toSummaryDTO);
    }

    public AdvertiseDetailDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDetailDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Advertise", id));
    }

    @Override
    public AdvertiseDetailDTO save(RequestAdvertise requestAdvertise) {
        var entity = mapper.toEntity(requestAdvertise);
        prepareAdvertiseEntity(entity);
        return persist(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anuncio no encontrado");
        }
        repository.deleteById(id);
    }

    @Override
    public AdvertiseDetailDTO createWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException {
        // 1. Subir imagen y obtener la URL
        String imageUrl = s3UploadService.uploadImage(file);
        dto.setImage(imageUrl);

        var entity = mapper.toEntity(dto);
        prepareAdvertiseEntity(entity);
        return persist(entity);
    }

    @Override
    public AdvertiseDetailDTO updateWithImage(AdvertiseDetailDTO dto, MultipartFile file) throws IOException {
        var adExist = repository.findById(dto.getId());
        if (adExist.isEmpty()) {
            throw new ResourceNotFoundException("Advertise", dto.getId());
        }
        String imageUrl = s3UploadService.uploadImage(file);
        dto.setImage(imageUrl);

        Advertise entity = mapper.toEntity(dto);
        return persist(entity);
    }

    private void prepareAdvertiseEntity(Advertise advertise) {
        advertise.setCreation(new Date());
        advertise.setActive(true);
        advertise.setDeleted(false);
        advertise.setUser("jorgebee65"); // Podrías hacerlo configurable más adelante
    }

    private AdvertiseDetailDTO persist(Advertise entity) {
        var saved = repository.save(entity);
        return mapper.toDetailDTO(saved);
    }

}
