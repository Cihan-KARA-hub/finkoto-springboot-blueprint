package com.finkoto.chargestation.services;


import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargingSessionMapper;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.repository.ChargingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChargingSessionService {
    private final ChargingSessionRepository chargingSessionRepository;
    private final ChargingSessionMapper chargingSessionMapper;

    @Transactional
    public PageableResponseDto<ChargingSessionDto> getAll(Pageable pageable) {
        final Page<ChargingSession> pageData = chargingSessionRepository.findAll(pageable);
        final List<ChargingSessionDto> chargePointDtoList = chargingSessionMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public void delete(Long id) {
        chargingSessionRepository.deleteById(id);
    }

    @Transactional
    public void create(ChargingSessionDto chargingSessionDto) {
       ChargingSession newChargingsession = new ChargingSession();
        chargingSessionMapper.toEntity(newChargingsession, chargingSessionDto);
        chargingSessionRepository.save(newChargingsession);
    }

    @Transactional
    public ChargingSessionDto findById(Long id) {
        ChargingSession chargingSession = chargingSessionRepository.findById(id).orElse(null);
        return chargingSessionMapper.toDto(chargingSession);
    }

    @Transactional
    public void update(ChargingSessionDto chargingSessionDto) {
        ChargingSession newChargingsession = new ChargingSession();
        final ChargingSession session = chargingSessionMapper.toEntity(newChargingsession,chargingSessionDto);
        chargingSessionMapper.toDto(chargingSessionRepository.save(session));
    }
}
