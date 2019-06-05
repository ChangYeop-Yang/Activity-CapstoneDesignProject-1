//
//  CustomMarkersViewController.swift
//  NMapSampleSwift
//
//  Created by Junggyun Ahn on 2016. 11. 14..
//  Copyright © 2016년 Naver. All rights reserved.
//

import UIKit

class CustomMarkersViewController: UIViewController, NMapViewDelegate, NMapPOIdataOverlayDelegate {

    var mapView: NMapView?

    @IBOutlet var calloutView: UIView!
    @IBOutlet weak var calloutLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationController?.navigationBar.isTranslucent = false

        mapView = NMapView(frame: self.view.frame)

        if let mapView = mapView {

            // set the delegate for map view
            mapView.delegate = self

            // set the application api key for Open MapViewer Library
            mapView.setClientId("YOUR CLIENT ID")

            mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]

            view.addSubview(mapView)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        mapView?.didReceiveMemoryWarning()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        mapView?.viewWillAppear()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        mapView?.viewDidAppear()

        showMarkers()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)

        mapView?.viewWillDisappear()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)

        mapView?.viewDidDisappear()
    }

    // MARK: - NMapViewDelegate Methods

    open func onMapView(_ mapView: NMapView!, initHandler error: NMapError!) {
        if (error == nil) { // success
            // set map center and level
            mapView.setMapCenter(NGeoPoint(longitude:126.978371, latitude:37.5666091), atLevel:11)
            // set for retina display
            mapView.setMapEnlarged(true, mapHD: true)
            // set map mode : vector/satelite/hybrid
            mapView.mapViewMode = .vector
        } else { // fail
            print("onMapView:initHandler: \(error.description)")
        }
    }

    // MARK: - NMapPOIdataOverlayDelegate Methods

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForOverlayItem poiItem: NMapPOIitem!, selected: Bool) -> UIImage! {
        return NMapViewResources.imageWithType(poiItem.poiFlagType, selected: selected);
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, anchorPointWithType poiFlagType: NMapPOIflagType) -> CGPoint {
        return NMapViewResources.anchorPoint(withType: poiFlagType)
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, calloutOffsetWithType poiFlagType: NMapPOIflagType) -> CGPoint {
        return CGPoint(x: 0.5, y: 0.0)
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForCalloutOverlayItem poiItem: NMapPOIitem!, constraintSize: CGSize, selected: Bool, imageForCalloutRightAccessory: UIImage!, calloutPosition: UnsafeMutablePointer<CGPoint>!, calloutHit calloutHitRect: UnsafeMutablePointer<CGRect>!) -> UIImage! {
        return nil
    }

    func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, viewForCalloutOverlayItem poiItem: NMapPOIitem!, calloutPosition: UnsafeMutablePointer<CGPoint>!) -> UIView! {
        calloutLabel.text = poiItem.title
        calloutPosition.pointee.x = round(calloutView.bounds.size.width / 2) + 1
        return calloutView
    }
    
    // MARK : Marker

    func showMarkers() {

        if let mapOverlayManager = mapView?.mapOverlayManager {

            // create POI data overlay
            if let poiDataOverlay = mapOverlayManager.newPOIdataOverlay() {

                poiDataOverlay.initPOIdata(3)

                poiDataOverlay.addPOIitem(atLocation: NGeoPoint(longitude: 126.979, latitude: 37.567), title: "마커 1", type: UserPOIflagTypeDefault, iconIndex: 0, with: nil)

                poiDataOverlay.addPOIitem(atLocation: NGeoPoint(longitude: 126.974, latitude: 37.566), title: "마커 2", type: UserPOIflagTypeDefault, iconIndex: 1, with: nil)

                poiDataOverlay.addPOIitem(atLocation: NGeoPoint(longitude: 126.984, latitude: 37.565), title: "마커 3", type: UserPOIflagTypeInvisible, iconIndex: 2, with: nil)

                poiDataOverlay.endPOIdata()

                // show all POI data
                poiDataOverlay.showAllPOIdata()
                
                poiDataOverlay.selectPOIitem(at: 2, moveToCenter: false, focusedBySelectItem: true)

            }
        }
    }

    func clearOverlays() {
        if let mapOverlayManager = mapView?.mapOverlayManager {
            mapOverlayManager.clearOverlays()
        }
    }

}
