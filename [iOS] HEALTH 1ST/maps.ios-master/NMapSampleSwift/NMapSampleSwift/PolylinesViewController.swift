//
//  PolylinesViewController.swift
//  NMapSampleSwift
//
//  Created by Junggyun Ahn on 2016. 11. 14..
//  Copyright © 2016년 Naver. All rights reserved.
//

import UIKit

class PolylinesViewController: UIViewController, NMapViewDelegate, NMapPOIdataOverlayDelegate {

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
        super.didReceiveMemoryWarning()
        mapView?.didReceiveMemoryWarning()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        mapView?.viewWillAppear()
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

    // MARK: - PolylinesViewController

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        mapView?.viewDidAppear()
        addPolylines()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)

        clearOverlays()
        mapView?.viewWillDisappear()
    }

    func addPolylines() {

        if let mapOverlayManager = mapView?.mapOverlayManager {

            // set path data points
            if let pathData = NMapPathData.init(capacity: 9) {

                pathData.initPathData()

                pathData.addPathPointLongitude(127.108099, latitude: 37.366034, lineType: .solid)
                pathData.addPathPointLongitude(127.108088, latitude: 37.366043, lineType: .none)
                pathData.addPathPointLongitude(127.108079, latitude: 37.365619, lineType: .none)
                pathData.addPathPointLongitude(127.107458, latitude: 37.365608, lineType: .none)
                pathData.addPathPointLongitude(127.107232, latitude: 37.365608, lineType: .none)
                pathData.addPathPointLongitude(127.106904, latitude: 37.365624, lineType: .none)
                pathData.addPathPointLongitude(127.105933, latitude: 37.365621, lineType: .dash)
                pathData.addPathPointLongitude(127.105929, latitude: 37.366378, lineType: .none)
                pathData.addPathPointLongitude(127.106279, latitude: 37.366380, lineType: .none)

                pathData.end()

                // create path data overlay
                if let pathDataOverlay = mapOverlayManager.newPathDataOverlay(pathData) {

                    if let pathData = NMapPathData.init(capacity: 7) {

                        pathData.initPathData()

                        pathData.addPathPointLongitude(127.108099, latitude: 37.367034, lineType: .solid)
                        pathData.addPathPointLongitude(127.108088, latitude: 37.367043, lineType: .none)
                        pathData.addPathPointLongitude(127.108079, latitude: 37.366619, lineType: .none)
                        pathData.addPathPointLongitude(127.107458, latitude: 37.366608, lineType: .none)
                        pathData.addPathPointLongitude(127.107232, latitude: 37.366608, lineType: .none)
                        pathData.addPathPointLongitude(127.106904, latitude: 37.366624, lineType: .dash)
                        pathData.addPathPointLongitude(127.106904, latitude: 37.367721, lineType: .none)

                        pathData.end()

                        let pathLineStyle = NMapPathLineStyle()
                        pathLineStyle.lineColor = .green

                        pathData.pathLineStyle = pathLineStyle

                        pathDataOverlay.add(pathData)
                    }

                    pathDataOverlay.showAllPathData()
                }
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
