package com.poly.backend.service.impl;

import com.poly.backend.dao.SlideShowDAO;
import com.poly.backend.dto.SlideShowDTO;
import com.poly.backend.entity.SlideShow;
import com.poly.backend.service.SlideShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlideShowServiceImpl implements SlideShowService {

    private final SlideShowDAO slideShowDAO;

    @Override
    public List<SlideShowDTO> getAllSlideShows() {
        return slideShowDAO.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SlideShowDTO> getActiveSlideShows() {
        return slideShowDAO.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional // Đảm bảo việc sắp xếp được lưu an toàn
    public SlideShowDTO createSlideShow(SlideShowDTO dto) {
        SlideShow slide = new SlideShow();
        mapToEntity(dto, slide);

        // Lấy tất cả danh sách để chèn
        List<SlideShow> allSlides = slideShowDAO.findAllByOrderByDisplayOrderAsc();

        // Xác định vị trí chèn (Nếu người dùng nhập sai số âm hoặc số quá lớn thì tự động nắn lại)
        int targetOrder = dto.getDisplayOrder() != null ? dto.getDisplayOrder() : allSlides.size() + 1;
        if (targetOrder < 1) targetOrder = 1;
        if (targetOrder > allSlides.size() + 1) targetOrder = allSlides.size() + 1;

        // Chèn slide mới vào đúng vị trí (Trừ 1 vì Index của List trong Java bắt đầu từ 0)
        allSlides.add(targetOrder - 1, slide);

        // Vòng lặp thần thánh: Đánh lại số thứ tự chuẩn 1, 2, 3... cho tất cả
        for (int i = 0; i < allSlides.size(); i++) {
            allSlides.get(i).setDisplayOrder(i + 1);
        }

        // Lưu toàn bộ danh sách đã được sắp xếp lại xuống DB
        slideShowDAO.saveAll(allSlides);
        return mapToDTO(slide);
    }

    @Override
    @Transactional
    public SlideShowDTO updateSlideShow(Integer id, SlideShowDTO dto) {
        SlideShow slide = slideShowDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SlideShow"));
        mapToEntity(dto, slide);

        // 1. Lấy toàn bộ danh sách hiện tại
        List<SlideShow> allSlides = slideShowDAO.findAllByOrderByDisplayOrderAsc();

        // 2. Rút slide đang sửa ra khỏi danh sách
        allSlides.removeIf(s -> s.getSlideId().equals(id));

        // 3. Xác định vị trí muốn nhét vào
        int targetOrder = dto.getDisplayOrder() != null ? dto.getDisplayOrder() : allSlides.size() + 1;
        if (targetOrder < 1) targetOrder = 1;
        if (targetOrder > allSlides.size() + 1) targetOrder = allSlides.size() + 1;

        // 4. Nhét slide vào vị trí mới
        allSlides.add(targetOrder - 1, slide);

        // 5. Đánh lại số thứ tự chuẩn
        for (int i = 0; i < allSlides.size(); i++) {
            allSlides.get(i).setDisplayOrder(i + 1);
        }

        // 6. Lưu lại cập nhật
        slideShowDAO.saveAll(allSlides);
        return mapToDTO(slide);
    }

    @Override
    @Transactional
    public void deleteSlideShow(Integer id) {
        slideShowDAO.deleteById(id);

        // AUTO DỌN DẸP: Sau khi xóa 1 cái, các cái bên dưới tự động đôn lên cho khít
        List<SlideShow> allSlides = slideShowDAO.findAllByOrderByDisplayOrderAsc();
        for (int i = 0; i < allSlides.size(); i++) {
            allSlides.get(i).setDisplayOrder(i + 1);
        }
        slideShowDAO.saveAll(allSlides);
    }

    private SlideShowDTO mapToDTO(SlideShow entity) {
        SlideShowDTO dto = new SlideShowDTO();
        dto.setSlideId(entity.getSlideId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setBadgeText(entity.getBadgeText());
        dto.setBadgeClass(entity.getBadgeClass());
        dto.setTitle(entity.getTitle());
        dto.setTitleHighlight(entity.getTitleHighlight());
        dto.setHighlightClass(entity.getHighlightClass());
        dto.setDescription(entity.getDescription());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setActive(entity.getActive());
        return dto;
    }

    private void mapToEntity(SlideShowDTO dto, SlideShow entity) {
        entity.setImageUrl(dto.getImageUrl());
        entity.setBadgeText(dto.getBadgeText());
        entity.setBadgeClass(dto.getBadgeClass() != null ? dto.getBadgeClass() : "#00FF33");
        entity.setTitle(dto.getTitle());
        entity.setTitleHighlight(dto.getTitleHighlight());
        entity.setHighlightClass(dto.getHighlightClass() != null ? dto.getHighlightClass() : "#00FF33");
        entity.setDescription(dto.getDescription());
        // displayOrder sẽ được tính toán lại trong hàm tạo/sửa nên không map cứng ở đây
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
    }
}