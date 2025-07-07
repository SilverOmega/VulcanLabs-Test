package controller;


import lombok.RequiredArgsConstructor;
import model.Seat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CinemaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cinema")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping("/configure")
    public ResponseEntity<String> configure(@RequestBody Map<String, Integer> config) {
        cinemaService.configure(config.get("rows"), config.get("cols"), config.get("min_distance"));
        return ResponseEntity.ok("Cinema configured.");
    }

    @GetMapping("/available-seats")
    public ResponseEntity<List<List<Seat>>> getAvailableSeats(@RequestParam int groupSize) {
        return ResponseEntity.ok(cinemaService.getAvailableGroups(groupSize));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeats(@RequestBody List<Seat> seats) {
        boolean success = cinemaService.reserve(seats);
        return success ? ResponseEntity.ok("Reserved.") : ResponseEntity.status(400).body("Failed to reserve.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSeats(@RequestBody List<Seat> seats) {
        cinemaService.cancel(seats);
        return ResponseEntity.ok("Cancelled.");
    }
}
