package com.finkoto.chargestation.services.specification;

import com.finkoto.chargestation.model.OcppLogger;
import org.springframework.data.jpa.domain.Specification;

public class OcppLoggerSpecification {

    public static Specification<OcppLogger> hasChargePointOcppId(String chargePointOcppId) {
        return (root, query, criteriaBuilder) -> chargePointOcppId != null ?
                criteriaBuilder.equal(root.get("chargePointOcppId"), chargePointOcppId) : null;
    }

    public static Specification<OcppLogger> hasConnectorOcppId(String connectorOcppId) {
        return (root, query, criteriaBuilder) ->
                connectorOcppId != null ? criteriaBuilder.equal(root.get("connectorOcppId"), connectorOcppId) : null;
    }

    public static Specification<OcppLogger> hasChargingSessionId(String chargingSessionId) {
        return (root, query, criteriaBuilder) ->
                chargingSessionId != null ? criteriaBuilder.equal(root.get("chargingSessionId"), chargingSessionId) : null;
    }
}
