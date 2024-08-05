package com.finkoto.chargestation.services;


import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargePointMapper;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.repository.ChargePointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ChargePointService {
    private ChargePointRepository chargePointRepository;
    private ChargePointMapper chargePointMapper;

    @Transactional
    public PageableResponseDto<ChargePointDto> getAll(Pageable pageable) {
        final Page<ChargePoint> pageData = chargePointRepository.findAll(pageable);
        final List<ChargePointDto> chargePointDtoList = chargePointMapper.toDto(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }
    @Transactional
    public void delete(Long id) {
        chargePointRepository.deleteById(id);
    }
    @Transactional
    public ChargePointDto create(ChargePointDto chargePointDto) {
        return chargePointMapper.toDto(chargePointMapper.toEntity(chargePointDto));
    }
    @Transactional
    public ChargePointDto findById(Long id) {
        return chargePointMapper.toDto(chargePointRepository.findById(id).orElse(null));
    }
    @Transactional
    public void update(ChargePointDto chargePointDto) {
        final ChargePoint chargePoint = chargePointMapper.toEntity(chargePointDto);
        chargePointRepository.save(chargePoint);

    }

}
