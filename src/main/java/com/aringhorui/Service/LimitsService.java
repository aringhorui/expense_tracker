package com.aringhorui.Service;

import com.aringhorui.Entities.Limits;
import com.aringhorui.Repositories.LimitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class LimitsService {

    @Autowired
    private LimitsRepository limitsRepository;

    /**
     * Adds or updates a spending limit for a given email and super category.
     * If a record already exists, it updates the amount.
     */
    public Limits addOrUpdateLimit(String email, String superCategory, Double amount) {
        Optional<Limits> existingLimit = limitsRepository.findByEmailAndSuperCategory(email, superCategory);

        if (existingLimit.isPresent()) {
            Limits limit = existingLimit.get();
            limit.setAmount(amount);
            return limitsRepository.save(limit);
        } else {
            Limits newLimit = new Limits(superCategory, amount, email);
            return limitsRepository.save(newLimit);
        }
    }

    public List<Limits> getLimitsByEmail(String email) {
        return limitsRepository.findByEmail(email);
    }

    public boolean deleteLimit(String email, String superCategory) {
        Optional<Limits> existingLimit = limitsRepository.findByEmailAndSuperCategory(email, superCategory);
        if (existingLimit.isPresent()) {
            limitsRepository.delete(existingLimit.get());
            return true;
        }
        return false;
    }

    public boolean deleteLimitById(Long id) {
        if (limitsRepository.existsById(id)) {
            limitsRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
