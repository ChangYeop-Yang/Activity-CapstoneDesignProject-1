//
//  CirclesViewController.swift
//  NMapSampleSwift
//
//  Created by Junggyun Ahn on 2016. 11. 14..
//  Copyright © 2016년 Naver. All rights reserved.
//

import UIKit

class CirclesViewController: UIViewController, NMapViewDelegate, NMapPOIdataOverlayDelegate {

    var mapView: NMapView?

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
        mapView?.didReceiveMemoryWarning()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        mapView?.viewWillAppear()
    }

    override func viewDidDisappear(_ animated: Bool) {
        mapView?.viewDidDisappear()
        super.viewDidDisappear(animated)
    }

    // MARK: - NMapViewDelegate Method

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

    // MARK: - CirclesMapViewController

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        mapView?.viewDidAppear()
        
        addCircles()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        mapView?.viewWillDisappear()

        clearOverlays()
    }

    func addCircles() {

        clearOverlays()

        if let mapOverlayManager = mapView?.mapOverlayManager {

            if let pathDataOverlay = mapOverlayManager.newPathDataOverlay(NMapPathData()) {

                // add circle data
                if let circleData = NMapCircleData(capacity: 1) {

                    circleData.initCircleData()
                    circleData.addCirclePointLongitude(127.1085, latitude: 37.3675, radius: 50.0)
                    circleData.end()

                    // set circle style
                    if let circleStyle = NMapCircleStyle() {

                        circleStyle.setLineType(.dash)

                        circleData.circleStyle = circleStyle
                    }

                    pathDataOverlay.add(circleData)
                }

                // add circle data
                if let circleData = NMapCircleData(capacity: 1) {

                    circleData.initCircleData()
                    circleData.addCirclePointLongitude(127.1065, latitude: 37.3685, radius: 27.0)
                    circleData.end()

                    // set circle style
                    if let circleStyle = NMapCircleStyle() {

                        circleStyle.setLineType(.solid)
                        circleStyle.fillColor = .green

                        circleData.circleStyle = circleStyle
                    }

                    pathDataOverlay.add(circleData)
                }

                // add circle data
                if let circleData = NMapCircleData(capacity: 1) {

                    circleData.initCircleData()
                    circleData.addCirclePointLongitude(127.1085, latitude: 37.3685, radius: 35.0)
                    circleData.end()

                    // set circle style
                    if let circleStyle = NMapCircleStyle() {

                        circleStyle.setLineType(.solid)
                        circleStyle.fillColor = .gray

                        circleData.circleStyle = circleStyle
                    }

                    pathDataOverlay.add(circleData)
                }

                pathDataOverlay.showAllPathData()
            }
        }
    }

    func clearOverlays() {
        if let mapOverlayManager = mapView?.mapOverlayManager {
            mapOverlayManager.clearOverlays()
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
        return CGPoint.zero
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForCalloutOverlayItem poiItem: NMapPOIitem!, constraintSize: CGSize, selected: Bool, imageForCalloutRightAccessory: UIImage!, calloutPosition: UnsafeMutablePointer<CGPoint>!, calloutHit calloutHitRect: UnsafeMutablePointer<CGRect>!) -> UIImage! {
        return nil
    }
}
