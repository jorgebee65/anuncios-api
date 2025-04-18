package com.dom.adv.api.mapper;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import com.dom.adv.api.dto.CategoryDTO;
import com.dom.adv.api.entity.Advertise;
import com.dom.adv.api.entity.Category;
import com.dom.adv.api.request.RequestAdvertise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertiseMapper {
    AdvertiseSummaryDTO toSummaryDTO(Advertise advertise);
    AdvertiseDetailDTO toDetailDTO(Advertise advertise);
    CategoryDTO toDTO(Category category);
    List<AdvertiseSummaryDTO> toSummaryDTOList(List<Advertise> advertises);
    Advertise toEntity(AdvertiseDetailDTO dto);
    Advertise toEntity(RequestAdvertise dto);
    Category toEntity(CategoryDTO dto);
}
