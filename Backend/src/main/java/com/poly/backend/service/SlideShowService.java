package com.poly.backend.service;

import com.poly.backend.dto.SlideShowDTO;
import java.util.List;

public interface SlideShowService {
    List<SlideShowDTO> getAllSlideShows();
    List<SlideShowDTO> getActiveSlideShows();
    SlideShowDTO createSlideShow(SlideShowDTO dto);
    SlideShowDTO updateSlideShow(Integer id, SlideShowDTO dto);
    void deleteSlideShow(Integer id);
}