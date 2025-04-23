package com.dom.adv.api.service.advertise.strategy;

import com.dom.adv.api.entity.Advertise;

public interface AdvertValidationStrategy {
    void validate(Advertise ad);
}