package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Center;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CenterResponseDTO {

    private Long id;
    private String name;
    private String city;
    private String specialty;
    private String phone;
    private Double latitude;
    private Double longitude;

    public static CenterResponseDTO fromEntity(Center entity) {

        CenterResponseDTO dto = new CenterResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCity(entity.getCity());
        dto.setSpecialty(entity.getSpecialty());
        dto.setPhone(entity.getPhone());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());

        return dto;
    }

    public static List<CenterResponseDTO> fromEntity(List<Center> entities) {

        List<CenterResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Center entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}

