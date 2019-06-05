//
//  RotationViewController.swift
//  NMapSampleSwift
//
//  Created by Junggyun Ahn on 2016. 11. 22..
//  Copyright © 2016년 Naver. All rights reserved.
//

class RotationViewController: UIViewController, NMapViewDelegate, NMapPOIdataOverlayDelegate {
    
    let rotateAnglePerClick:CGFloat = 45
    
    var mapView: NMapView?
    
    var previousAngle:CGFloat = 0
    
    var hasInit = false
    
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
            
            view.insertSubview(mapView, at: 0)
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
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        mapView?.viewWillDisappear()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        mapView?.viewDidDisappear()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        
        if view.frame.size.width != size.width {
            if let mapView = mapView, hasInit, mapView.isAutoRotateEnabled {
                previousAngle = mapView.rotateAngle
                mapView.setAutoRotateEnabled(false, animate: false)
                
                coordinator.animate(alongsideTransition: {(context: UIViewControllerTransitionCoordinatorContext) -> Void in
                    if let mapView = self.mapView, self.hasInit {
                        mapView.setAutoRotateEnabled(true, animate: false)
                        mapView.setRotateAngle(Float(self.previousAngle), animate: true)
                    }
                }, completion: nil)
            }
        }
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
            
            hasInit = true
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
        return CGPoint.zero
    }
    
    open func onMapOverlay(_ poiDataOverlay: NMapPOIdataOverlay!, imageForCalloutOverlayItem poiItem: NMapPOIitem!, constraintSize: CGSize, selected: Bool, imageForCalloutRightAccessory: UIImage!, calloutPosition: UnsafeMutablePointer<CGPoint>!, calloutHit calloutHitRect: UnsafeMutablePointer<CGRect>!) -> UIImage! {
        return nil
    }
    
    // MARK: - UI Actions
    
    @IBAction func rightButtonClicked(_ sender: UIButton) {
        if let mapView = mapView {
            
            if mapView.isAutoRotateEnabled == false {
                mapView.setAutoRotateEnabled(true, animate: true)
            }
            
            let newAngle = mapView.rotateAngle - rotateAnglePerClick// + (mapView.rotateAngle < rotateAnglePerClick ? 360 : 0)
            mapView.setRotateAngle(Float(newAngle), animate: true)
        }
    }
    
    @IBAction func leftButtonClicked(_ sender: UIButton) {
        if let mapView = mapView {
            
            if mapView.isAutoRotateEnabled == false {
                mapView.setAutoRotateEnabled(true, animate: true)
            }
            
            let newAngle = mapView.rotateAngle + rotateAnglePerClick// - (mapView.rotateAngle > 360 - rotateAnglePerClick ? 360 : 0)
            mapView.setRotateAngle(Float(newAngle), animate: true)
        }
    }
}
