package com.finkoto.chargestation.services;


import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargePointMapper;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.repository.ChargeHardwareSpecRepository;
import com.finkoto.chargestation.repository.ChargePointRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ChargePointService {

    private final ChargePointRepository chargePointRepository;
    private final ChargePointMapper chargePointMapper;
    private final ChargeHardwareSpecRepository chargeHardwareSpecRepository;


    @Transactional
    public PageableResponseDto<ChargePointDto> getAll(Pageable pageable) {
        final Page<ChargePoint> pageData = chargePointRepository.findAll(pageable);
        final List<ChargePointDto> chargePointDtoList = chargePointMapper.toDto(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public void create(ChargePointDto chargePointDto) {
        final ChargePoint newChargePoint = new ChargePoint();
        chargePointMapper.toEntity(newChargePoint, chargePointDto);
        chargePointRepository.save(newChargePoint);
    }


    @Transactional
    public ChargePointDto update(Long id, ChargePointDto chargePointDto) {
        final ChargePoint chargePoint = chargePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entity with id: " + id + " not found."));
        chargePointMapper.toEntity(chargePoint, chargePointDto);
        final ChargePoint updatedChargePoint = chargePointRepository.save(chargePoint);
        return chargePointMapper.toDto(updatedChargePoint);
    }

    @Transactional
    public void delete(Long id) {
        chargePointRepository.deleteById(id);
    }

    @Transactional
    public ChargePointDto findById(Long id) {
        return chargePointRepository.findById(id).map(chargePointMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
    }

    @Transactional
    public ChargePoint findByOcppId(String ocppId) {
        return chargePointRepository.findByOcppId(ocppId).orElseThrow(() -> new EntityNotFoundException("Entity with ocppId: " + ocppId + " not found."));
    }

    @Transactional
    public void newSession(String ocppId) {
        final ChargePoint optionalChargePoint = findByOcppId(ocppId);
        optionalChargePoint.setOnline(true);
        optionalChargePoint.setLastConnected(OffsetDateTime.now());
        optionalChargePoint.setLastDisconnected(null);
        chargePointRepository.save(optionalChargePoint);
    }

    @Transactional
    public void lostSession() {
        final List<ChargePoint> chargePointsList = chargePointRepository.findByOnline(true);
        for (ChargePoint chargePoint : chargePointsList) {
            chargePoint.setOnline(false);
            chargePoint.setLastDisconnected(OffsetDateTime.now());
            chargePointRepository.save(chargePoint);
        }
    }

    //Online charge pointlerin ocppId'lerini listeler
    public List<String> onlineOcppIdList() {
        List<String> ocppIdList = new ArrayList<>();
        final List<ChargePoint> chargePointsList = chargePointRepository.findByOnline(true);
        chargePointsList.forEach(chargePoint -> ocppIdList.add(chargePoint.getOcppId()));
        return ocppIdList;
    }

    @Transactional
    public void handleHeartbeatRequest(String ocppId) {
        ChargePoint chargePoint = findByOcppId(ocppId);
        chargePoint.setLastHealthChecked(OffsetDateTime.now());
    }


}
