package com.dom.adv.api.service.advertise.impl;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.entity.Advertise;
import com.dom.adv.api.exception.ResourceNotFoundException;
import com.dom.adv.api.mapper.AdvertiseMapper;
import com.dom.adv.api.repository.AdvertiseRepository;
import com.dom.adv.api.request.RequestAdvertise;
import com.dom.adv.api.security.UserContextService;
import com.dom.adv.api.service.advertise.strategy.AdvertValidationStrategy;
import com.dom.adv.api.service.aws.impl.S3UploadServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class UserAdvertServiceImplTest {

    @Mock
    private AdvertiseRepository advertiseRepository;

    @Mock
    private AdvertiseMapper mapper;

    @Mock
    private S3UploadServiceImpl s3UploadService;

    @Mock
    private AdvertValidationStrategy advertValidationStrategy;

    @Mock
    private UserContextService userContextService;

    @InjectMocks
    private UserAdvertServiceImpl userAdvertService;

    @Test
    void save_shouldReturnAdvertiseDetailDTO() {
        RequestAdvertise request = new RequestAdvertise();
        Advertise advertise = new Advertise();
        AdvertiseDetailDTO dto = new AdvertiseDetailDTO();

        Mockito.when(mapper.toEntity(request)).thenReturn(advertise);
        Mockito.when(advertiseRepository.save(advertise)).thenReturn(advertise);
        Mockito.when(mapper.toDetailDTO(advertise)).thenReturn(dto);
        Mockito.when(userContextService.getAuthenticatedUsername()).thenReturn("admin");

        var result = userAdvertService.save(request);

        Assertions.assertEquals(dto, result);
        Mockito.verify(advertiseRepository).save(advertise);
    }

    @Test
    void deleteById_shouldDeleteAdvertise() {
        Long id = 1L;
        Mockito.when(advertiseRepository.existsById(id)).thenReturn(true);

        userAdvertService.deleteById(id);

        Mockito.verify(advertiseRepository).deleteById(id);
    }

    @Test
    void deleteById_shouldThrowWhenNotFound() {
        Long id = 2L;
        Mockito.when(advertiseRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class, () -> userAdvertService.deleteById(id));
    }

    @Test
    void createWithImage_shouldUploadAndSaveAdvertise() throws IOException {
        AdvertiseDetailDTO dto = new AdvertiseDetailDTO();
        dto.setId(1L);
        Advertise advertise = new Advertise();
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.when(s3UploadService.uploadImage(file)).thenReturn("http://image-url");
        Mockito.when(mapper.toEntity(dto)).thenReturn(advertise);
        Mockito.when(advertiseRepository.save(advertise)).thenReturn(advertise);
        Mockito.when(mapper.toDetailDTO(advertise)).thenReturn(dto);
        Mockito.when(userContextService.getAuthenticatedUsername()).thenReturn("admin");

        var result = userAdvertService.createWithImage(dto, file);

        Assertions.assertEquals("http://image-url", result.getImage());
    }

    @Test
    void updateWithImage_shouldUpdateImageAndSave() throws IOException {
        Long id = 1L;
        AdvertiseDetailDTO dto = new AdvertiseDetailDTO();
        dto.setId(id);
        Advertise advertise = new Advertise();
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.when(advertiseRepository.findById(id)).thenReturn(Optional.of(new Advertise()));
        Mockito.when(s3UploadService.uploadImage(file)).thenReturn("http://new-url");
        Mockito.when(mapper.toEntity(dto)).thenReturn(advertise);
        Mockito.when(advertiseRepository.save(advertise)).thenReturn(advertise);
        Mockito.when(mapper.toDetailDTO(advertise)).thenReturn(dto);
        doNothing().when(advertValidationStrategy).validate(any());

        var result = userAdvertService.updateWithImage(dto, file);

        Assertions.assertEquals("http://new-url", result.getImage());
    }

    @Test
    void updateWithImage_shouldThrowIfNotFound() {
        Long id = 99L;
        AdvertiseDetailDTO dto = new AdvertiseDetailDTO();
        dto.setId(id);
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.when(advertiseRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userAdvertService.updateWithImage(dto, file));
    }
}
