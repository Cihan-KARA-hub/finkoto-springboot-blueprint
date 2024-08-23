package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.api.dto.ChargePointDto;
import com.finkoto.ocppmockserver.api.dto.PageableResponseDto;
import com.finkoto.ocppmockserver.api.mapper.ChargePointMapper;
import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.repository.MockChargeHardwareRepository;
import com.finkoto.ocppmockserver.repository.MockChargePointRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MockChargePointServices {
    private final MockChargePointRepository mockChargePointRepository;
    private final ChargePointMapper chargePointMapper;
    private final MockChargeHardwareRepository mockChargeHardwareRepository;

    @Transactional
    public void create( ChargePointDto chargePointDto) {
        final ChargePoint newChargePoint = new ChargePoint();
        chargePointMapper.toEntity(newChargePoint, chargePointDto);
        mockChargePointRepository.save(newChargePoint);
    }

    @Transactional
    public List<ChargePoint> findByOnline(boolean online) {
        return mockChargePointRepository.findByOnline(online);
    }

    @Transactional
    public List<ChargePoint> findAll() {
        return mockChargePointRepository.findAll();
    }

    @Transactional
    public PageableResponseDto<ChargePointDto> getAll(Pageable pageable) {
        final Page<ChargePoint> pageData = mockChargePointRepository.findAll(pageable);
        final List<ChargePointDto> chargePointDtoList = chargePointMapper.toDto(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public ChargePointDto update(Long id, ChargePointDto chargePointDto) {
        final ChargePoint chargePoint = mockChargePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entity with id: " + id + " not found."));
        chargePointMapper.toEntity(chargePoint, chargePointDto);
        final ChargePoint updatedChargePoint = mockChargePointRepository.save(chargePoint);
        return chargePointMapper.toDto(updatedChargePoint);
    }

    @Transactional
    public void delete(Long id) {
        mockChargePointRepository.deleteById(id);
    }

    @Transactional
    public ChargePointDto findById(Long id) {
        return mockChargePointRepository.findById(id).map(chargePointMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
    }
}
