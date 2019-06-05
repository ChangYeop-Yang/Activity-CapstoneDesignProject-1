//
//  MapViewController.swift
//  NMapSampleSwift
//
//  Created by Junggyun Ahn on 2016. 11. 9..
//  Copyright © 2016년 Naver. All rights reserved.
//

import UIKit

class MapViewController: UIViewController, NMapViewDelegate, NMapPOIdataOverlayDelegate {

    var mapView: NMapView?

    @IBOutlet weak var levelStepper: UIStepper!

    override func viewDidLoad() {
        super.viewDidLoad()

        mapView = NMapView(frame: self.view.bounds)

        if let mapView = mapView {

            // set the delegate for map view
            mapView.delegate = self

            // set the application api key for Open MapViewer Library
            mapView.setClientId("YOUR CLIENT ID")

            mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]

            view.addSubview(mapView)
            
            // Zoom 용 UIStepper 셋팅.
            initLevelStepper(mapView.minZoomLevel(), maxValue:mapView.maxZoomLevel(), initialValue:11)
            view.bringSubview(toFront: levelStepper)

            mapView.setBuiltInAppControl(true)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
        mapView?.didReceiveMemoryWarning()
    }

    // MARK: - NMapViewDelegate

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

    // MARK: - NMapPOIdataOverlayDelegate

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForOverlayItem poiItem: NMapPOIitem!, selected: Bool) -> UIImage! {
        return NMapViewResources.imageWithType(poiItem.poiFlagType, selected: selected)
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, anchorPointWithType poiFlagType: NMapPOIflagType) -> CGPoint {
        return NMapViewResources.anchorPoint(withType: poiFlagType)
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, calloutOffsetWithType poiFlagType: NMapPOIflagType) -> CGPoint {
        return CGPoint(x: 0, y: 0)
    }

    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForCalloutOverlayItem poiItem: NMapPOIitem!, constraintSize: CGSize, selected: Bool, imageForCalloutRightAccessory: UIImage!, calloutPosition: UnsafeMutablePointer<CGPoint>!, calloutHit calloutHitRect: UnsafeMutablePointer<CGRect>!) -> UIImage! {
        return nil
    }

    // MARK: - Layer Button
    @IBAction func layerButtonAction(_ sender: UIBarButtonItem) {

        let alertController = UIAlertController(title: "Layers", message: nil, preferredStyle: .actionSheet)

        if let map = mapView {
            // Action Sheet 생성

            let trafficAction = UIAlertAction(title: "Traffic layer is " + (map.mapViewTrafficMode ? "On" : "Off"), style: .default, handler: { (action) -> Void in
                print("Traffic layer Selected...")
                map.mapViewTrafficMode = !map.mapViewTrafficMode
            })

            let bicycleAction = UIAlertAction(title: "Bicycle layer is " + (map.mapViewBicycleMode ? "On" : "Off"), style: .default, handler: { (action) -> Void in
                print("Traffic layer Selected...")
                map.mapViewBicycleMode = !map.mapViewBicycleMode
            })
            
            let alphaAction = UIAlertAction(title: "Alpha layer is " + (map.mapViewAlphaLayerMode ? "On" : "Off"), style: .default, handler: { (action) -> Void in
                print("Alpha layer Selected...")
                map.mapViewAlphaLayerMode = !map.mapViewAlphaLayerMode
                
//                지도 위 반투명 레이어에 색을 지정할 때에는 다음 메서드를 사용한다
//                map.setMapViewAlphaLayerMode(!map.mapViewAlphaLayerMode, with: UIColor(red: 0.5, green: 1.0, blue: 0.5, alpha: 0.9))
            })

            alertController.addAction(trafficAction)
            alertController.addAction(bicycleAction)
            alertController.addAction(alphaAction)
        }

        alertController.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))

        self.present(alertController, animated: true, completion: nil)
    }

    // MARK: - Map Mode Segmented Control

    @IBAction func modeChanged(_ sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex {
        case 0:
            mapView?.mapViewMode = .vector
        case 1:
            mapView?.mapViewMode = .satellite
        case 2:
            mapView?.mapViewMode = .hybrid
        default:
            mapView?.mapViewMode = .vector
        }
    }

    // MARK: - Level Stepper

    func initLevelStepper(_ minValue: Int32, maxValue: Int32, initialValue: Int32) {
        levelStepper.minimumValue = Double(minValue)
        levelStepper.maximumValue = Double(maxValue)
        levelStepper.stepValue = 1
        levelStepper.value = Double(initialValue)
    }

    @IBAction func levelStepperValeChanged(_ sender: UIStepper) {
        mapView?.setZoomLevel(Int32(sender.value))
    }
}
