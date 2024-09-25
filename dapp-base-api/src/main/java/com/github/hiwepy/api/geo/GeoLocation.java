package com.github.hiwepy.api.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeoLocation {

    private String country;
    private String region;
    private String city;
    private Double latitude;
    private Double longitude;

    public static GeoLocation empty() {
        return new GeoLocation("", "", "", 0.0, 0.0);
    }

}
