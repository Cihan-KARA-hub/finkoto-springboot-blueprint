package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.api.dto.ChargeHardwareSpecDto;
import com.finkoto.ocppmockserver.api.mapper.ChargeHardwareSpecMapper;
import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import com.finkoto.ocppmockserver.repository.MockChargeHardwareRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MockHardwareSpecServices {
    private final MockChargeHardwareRepository chargeHardwareSpecRepository;
    private final ChargeHardwareSpecMapper chargeHardwareSpecMapper;

    @Transactional
    public List<ChargeHardwareSpec> getChargeHardwareSpec() {
        return chargeHardwareSpecRepository.findAll();
    }

    @Transactional
    public void create(ChargeHardwareSpecDto chargeHardwareSpecDto) {
        final ChargeHardwareSpec newChargeHardwareSpec = new ChargeHardwareSpec();
        chargeHardwareSpecMapper.toEntity(newChargeHardwareSpec, chargeHardwareSpecDto);
        chargeHardwareSpecRepository.save(newChargeHardwareSpec);
    }

    @Transactional
    public void update(Long id ,ChargeHardwareSpecDto chargeHardwareSpecDto) {
        chargeHardwareSpecRepository.findById(id).ifPresent(chargeHardwareSpec -> {
            chargeHardwareSpecMapper.toEntity(chargeHardwareSpec, chargeHardwareSpecDto);
            chargeHardwareSpecRepository.save(chargeHardwareSpec);
        });
    }

    @Transactional
    public void delete(Long id) {
        chargeHardwareSpecRepository.findById(id).ifPresent(chargeHardwareSpecRepository::delete);
    }

    @Transactional
    public ChargeHardwareSpecDto getChargeHardwareSpec(Long id) {
        return chargeHardwareSpecRepository.findById(id).map(chargeHardwareSpecMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
    }
}
