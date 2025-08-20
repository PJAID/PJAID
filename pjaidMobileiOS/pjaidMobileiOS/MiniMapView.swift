//
//  MiniMapView.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 13/08/2025.
//

import SwiftUI
import MapKit

struct MiniMapView: View {
    let coord: CLLocationCoordinate2D
    var height: CGFloat = 160

    var body: some View {
        let region = MKCoordinateRegion(
            center: coord,
            span: MKCoordinateSpan(latitudeDelta: 0.002, longitudeDelta: 0.002)
        )

        if #available(iOS 17.0, *) {
            Map(position: .constant(.region(region))) {
                Marker("", coordinate: coord)
            }
            .frame(height: height)
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .allowsHitTesting(false)
        } else {
            Map(coordinateRegion: .constant(region), annotationItems: [Pin(coord)]) { item in
                MapAnnotation(coordinate: item.coordinate) {
                    Image(systemName: "mappin.circle.fill")
                        .imageScale(.large)
                }
            }
            .frame(height: height)
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .allowsHitTesting(false)
        }
    }
}

private struct Pin: Identifiable {
    let id = UUID()
    let coordinate: CLLocationCoordinate2D
    init(_ coordinate: CLLocationCoordinate2D) { self.coordinate = coordinate }
}
