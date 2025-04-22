package com.dom.adv.api.controller;

import com.dom.adv.api.dto.AdvertiseDetailDTO;
import com.dom.adv.api.dto.AdvertiseSummaryDTO;
import com.dom.adv.api.request.RequestAdvertise;
import com.dom.adv.api.service.AdvertiseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/advertises")
public class AdvertiseController {

    private final AdvertiseService advertiseService;
    private final ObjectMapper objectMapper;

    public AdvertiseController(AdvertiseService service, ObjectMapper objectMapper) {
        this.advertiseService = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<Page<AdvertiseSummaryDTO>> getAllAdvertises(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creation,desc") String[] sort,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String category
    ) {
        Sort sorting = Sort.by(Sort.Order.desc("creation"));
        if (sort.length == 2) {
            sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<AdvertiseSummaryDTO> advertises = advertiseService.findAll(pageable, active, category);
        return ResponseEntity.ok(advertises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertiseDetailDTO> getAdvertiseById(@PathVariable Long id) {
        AdvertiseDetailDTO advertise = advertiseService.findById(id);
        return ResponseEntity.ok(advertise);
    }

    @PostMapping
    public ResponseEntity<AdvertiseDetailDTO> createAdvertise(@Valid @RequestBody RequestAdvertise request) {
        AdvertiseDetailDTO saved = advertiseService.save(request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertise(@PathVariable Long id) {
        advertiseService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping(value = "/with-image", consumes = "multipart/form-data")
    public ResponseEntity<?> createAdvertiseWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") String advertiseJson // Aquí llega el JSON como String
    ) {
        try {
            AdvertiseDetailDTO dto = objectMapper.readValue(advertiseJson, AdvertiseDetailDTO.class);
            AdvertiseDetailDTO created = advertiseService.createWithImage(dto, file);
            return ResponseEntity.ok(created);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}/with-image", consumes = "multipart/form-data")
    public ResponseEntity<?> updateAdvertiseWithImage(
            @PathVariable Long id,
            @RequestPart(name = "file", required = false) MultipartFile file ,
            @RequestPart("data") String advertiseJson
    ) {
        try {
            AdvertiseDetailDTO dto = objectMapper.readValue(advertiseJson, AdvertiseDetailDTO.class);
            dto.setId(id); // asegurar el ID desde el path
            AdvertiseDetailDTO updated = advertiseService.updateWithImage(dto, file);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
