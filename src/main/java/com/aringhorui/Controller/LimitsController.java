package com.aringhorui.Controller;

import com.aringhorui.Entities.Limits;
import com.aringhorui.Service.LimitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits")
@CrossOrigin(origins = "*") // Allow CORS for frontend
public class LimitsController {

    @Autowired
    private LimitsService limitsService;

    /**
     * Endpoint to add or update a limit for a super category and email.
     */
    @PostMapping("/add")
    public ResponseEntity<Limits> addLimit(
            @RequestParam String email,
            @RequestParam String superCategory,
            @RequestParam Double amount) {

        Limits savedLimit = limitsService.addOrUpdateLimit(email, superCategory, amount);
        return ResponseEntity.ok(savedLimit);
    }

    /**
     * Endpoint to fetch all limits by email.
     * This endpoint is used by the dashboard.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Limits>> getLimitsList(@RequestParam String email) {
        List<Limits> limits = limitsService.getLimitsByEmail(email);
        return ResponseEntity.ok(limits);
    }

    /**
     * Alternative endpoint (keeping for backward compatibility).
     */
    @GetMapping("/by-email")
    public ResponseEntity<List<Limits>> getLimitsByEmail(@RequestParam String email) {
        List<Limits> limits = limitsService.getLimitsByEmail(email);
        return ResponseEntity.ok(limits);
    }

    /**
     * Delete a limit by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLimitById(@PathVariable Long id) {
        boolean deleted = limitsService.deleteLimitById(id);
        if (deleted) {
            return ResponseEntity.ok("Limit deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("No limit found with given ID");
        }
    }

    /**
     * Delete a limit by email and super category.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLimit(
            @RequestParam String email,
            @RequestParam String superCategory) {

        boolean deleted = limitsService.deleteLimit(email, superCategory);
        if (deleted) {
            return ResponseEntity.ok("Limit deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("No limit found for given email and category");
        }
    }
}
