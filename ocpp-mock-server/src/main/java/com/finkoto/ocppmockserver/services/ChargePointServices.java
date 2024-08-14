package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.repository.ChargePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargePointServices {
    private final ChargePointRepository chargePointRepository;

    public List<ChargePoint> findByOnline(boolean online) {
        return chargePointRepository.findByOnline(online);
    }
    public List<ChargePoint> findAll(){
        return chargePointRepository.findAll();
    }
}
