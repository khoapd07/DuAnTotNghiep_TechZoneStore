package com.poly.backend.controller;

import com.poly.backend.dto.SlideShowDTO;
import com.poly.backend.service.SlideShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slideshows")
@CrossOrigin("*")
public class SlideShowController {

    private final SlideShowService slideShowService;

    public SlideShowController(SlideShowService slideShowService) {
        this.slideShowService = slideShowService;
    }

    @GetMapping
    public ResponseEntity<List<SlideShowDTO>> getAll() {
        return ResponseEntity.ok(slideShowService.getAllSlideShows());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SlideShowDTO>> getActive() {
        return ResponseEntity.ok(slideShowService.getActiveSlideShows());
    }

    @PostMapping
    public ResponseEntity<SlideShowDTO> create(@RequestBody SlideShowDTO dto) {
        return ResponseEntity.ok(slideShowService.createSlideShow(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlideShowDTO> update(@PathVariable Integer id, @RequestBody SlideShowDTO dto) {
        return ResponseEntity.ok(slideShowService.updateSlideShow(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        slideShowService.deleteSlideShow(id);
        return ResponseEntity.ok().build();
    }
}