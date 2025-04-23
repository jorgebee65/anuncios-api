package com.dom.adv.api.service.advertise;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import com.dom.adv.api.entity.Advertise;
import com.dom.adv.api.exception.ResourceNotFoundException;
import com.dom.adv.api.mapper.AdvertiseMapper;
import com.dom.adv.api.repository.AdvertiseRepository;
import com.dom.adv.api.security.UserContextService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public abstract class AbstractAdvertService implements PublicAdvertService {

    protected final AdvertiseRepository advertiseRepository;
    protected final AdvertiseMapper mapper;
    protected final UserContextService userContextService;

    public AbstractAdvertService(AdvertiseRepository advertiseRepository, AdvertiseMapper mapper, UserContextService userContextService) {
        this.advertiseRepository = advertiseRepository;
        this.mapper = mapper;
        this.userContextService = userContextService;
    }

    public Page<AdvertiseSummaryDTO> findAll(Pageable pageable, Boolean active, String category) {
        Page<Advertise> result;

        if (active != null && category != null) {
            result = advertiseRepository.findAllByActiveAndCategoryDescriptionIgnoreCase(active, category, pageable);
        } else if (active != null) {
            result = advertiseRepository.findAllByActive(active, pageable);
        } else if (category != null) {
            result = advertiseRepository.findAllByCategoryDescriptionIgnoreCase(category, pageable);
        } else {
            result = advertiseRepository.findAll(pageable);
        }

        return result.map(mapper::toSummaryDTO);
    }

    public AdvertiseDetailDTO findById(Long id) {
        return advertiseRepository.findById(id)
                .map(mapper::toDetailDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Advertise", id));
    }

    protected void prepareAdvertiseEntity(Advertise advertise) {
        advertise.setCreation(new Date());
        advertise.setActive(true);
        advertise.setDeleted(false);
        advertise.setUser(userContextService.getAuthenticatedUsername());
    }

    protected AdvertiseDetailDTO persist(Advertise entity) {
        var saved = advertiseRepository.save(entity);
        return mapper.toDetailDTO(saved);
    }
}
