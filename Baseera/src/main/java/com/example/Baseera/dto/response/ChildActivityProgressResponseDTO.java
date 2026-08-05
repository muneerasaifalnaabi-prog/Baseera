package com.example.Baseera.dto.response;

import com.example.Baseera.entity.ChildActivityProgress;
import com.example.Baseera.enums.ProgressStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildActivityProgressResponseDTO {

    private Long id;
    private Long activityId;
    private String activityName;
    private String activityDescription;
    private ProgressStatus status;
    private String notes;
    private LocalDateTime addedAt;

    public static ChildActivityProgressResponseDTO fromEntity(ChildActivityProgress entity) {
        ChildActivityProgressResponseDTO dto = new ChildActivityProgressResponseDTO();

        dto.setId(entity.getId());
        dto.setActivityId(entity.getActivity().getId());
        dto.setActivityName(entity.getActivity().getName());
        dto.setActivityDescription(entity.getActivity().getDescription());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setAddedAt(entity.getCreatedAt());

        return dto;
    }

    public static List<ChildActivityProgressResponseDTO> fromEntity(List<ChildActivityProgress> entities) {
        List<ChildActivityProgressResponseDTO> dtos = new ArrayList<>();
        for (ChildActivityProgress entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}
