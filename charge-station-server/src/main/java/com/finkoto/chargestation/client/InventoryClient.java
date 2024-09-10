package com.finkoto.chargestation.client;

import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    @GetExchange("/api/isActiveGetCp")
    boolean isActiveGetCp();
}
