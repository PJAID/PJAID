package org.api.pjaidapp.service;

import lombok.Getter;
import lombok.Setter;
import org.api.pjaidapp.model.Coordinate;
import org.api.pjaidapp.model.Hall;
import org.api.pjaidapp.repository.HallRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Service
public class LocationService {

    private HallRepository hallRepository;

    public LocationService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public String findHallId(Double latitude, Double longitude) {
        List<Hall> halls = hallRepository.findAll();
        for (Hall hall : halls) {
            Coordinate[] coords = hall.getCoordinates().stream()
                    .sorted(Comparator.comparing(Coordinate::getTopOrder))
                    .toArray(Coordinate[]::new);
            if (isPointInRectangle(coords, latitude, longitude)) {
                return hall.getName();
            }

        }
        return null;
    }

    public static boolean isPointInRectangle(Coordinate[] rectangle, Double latitude, Double longitude) {
        boolean hasNegative = false;
        boolean hasPositive = false;
        for (int i = 0; i < 4; i++) {
            double c = cross(rectangle[i], rectangle[(i + 1) % 4], latitude, longitude);
            if (c < 0) hasNegative = true;
            if (c > 0) hasPositive = true;
        }
        return !(hasNegative && hasPositive);
    }

    private static double cross(Coordinate a, Coordinate b, Double latitude, Double longitude) {
        return (b.getLongitude() - a.getLongitude()) * (latitude - a.getLatitude())
                - (b.getLatitude() - a.getLatitude()) * (longitude - a.getLongitude());
    }
}
