package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Center;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CenterRequestDTO {
    @NotBlank(message = "Center name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    @NotBlank(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    public Center toEntity() {

        Center center = new Center();

        center.setName(name);
        center.setCity(city);
        center.setSpecialty(specialty);
        center.setPhone(phone);
        center.setLatitude(latitude);
        center.setLongitude(longitude);

        return center;
    }

    public void applyTo(Center center) {

        center.setName(name);
        center.setCity(city);
        center.setSpecialty(specialty);
        center.setPhone(phone);
        center.setLatitude(latitude);
        center.setLongitude(longitude);
    }
}
