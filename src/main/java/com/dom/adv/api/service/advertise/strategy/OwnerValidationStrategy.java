package com.dom.adv.api.service.advertise.strategy;

import com.dom.adv.api.entity.Advertise;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class OwnerValidationStrategy implements AdvertValidationStrategy{
    @Override
    public void validate(Advertise ad) {
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!ad.getUser().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para editar este anuncio.");
        }
    }
}
