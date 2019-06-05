//
//  ReverseGeocoderViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 14..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "ReverseGeocoderViewController.h"
#import "NMapViewResources.h"

@interface ReverseGeocoderViewController ()

@property (nonatomic, strong) NMapView * mapView;
@property (nonatomic, strong) NMapPOIdataOverlay * mapPOIdataOverlay;

@end

@implementation ReverseGeocoderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView = [[NMapView alloc] initWithFrame:self.view.frame];
    self.navigationController.navigationBar.translucent = NO;
    
    // set the delegate for map view
    [self.mapView setDelegate:self];
    
    // set the application api key for Open MapViewer Library

    [self.mapView setClientId:@"YOUR CLIENT ID"];

    self.mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

    // set delegate to use reverse geocoder API
    [self.mapView setReverseGeocoderDelegate:self];
    
    [self.view addSubview:self.mapView];
}

- (void) viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self.mapView setFrame:self.view.bounds];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.mapView viewWillAppear];
}

- (void)viewDidDisappear:(BOOL)animated {
    [self.mapView viewDidDisappear];
    [super viewDidDisappear:animated];
}

#pragma mark NMapViewDelegate Method

- (void) onMapView:(NMapView *)mapView initHandler:(NMapError *)error {
    if (error == nil) { // success
        // set map center and level
        [self.mapView setMapCenter:NGeoPointMake(126.978371, 37.5666091) atLevel:11];
        // set for retina display
        [self.mapView setMapEnlarged:YES mapHD:YES];
        // set default map mode
        [self.mapView setMapViewMode:NMapViewModeVector];
    } else { // fail
        NSLog(@"onMapView:initHandler: %@", [error description]);
    }
}

#pragma mark NMapPOIdataOverlayDelegate

- (UIImage *) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForOverlayItem:(NMapPOIitem *)poiItem selected:(BOOL)selected {
    return [NMapViewResources imageWithType:poiItem.poiFlagType selected:selected];
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay anchorPointWithType:(NMapPOIflagType)poiFlagTyp{
    return [NMapViewResources anchorPoint:poiFlagTyp];
}

- (UIImage*) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForCalloutOverlayItem:(NMapPOIitem *)poiItem
           constraintSize:(CGSize)constraintSize selected:(BOOL)selected
imageForCalloutRightAccessory:(UIImage *)imageForCalloutRightAccessory
          calloutPosition:(CGPoint *)calloutPosition calloutHitRect:(CGRect *)calloutHitRect {
    return nil;
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay calloutOffsetWithType:(NMapPOIflagType)poiFlagType {
    return CGPointZero;
}

#pragma mark GeocoderViewController

- (void)viewDidAppear:(BOOL)animated {
    [self.mapView viewDidAppear];
    [self requestAddressByCoordination:NGeoPointMake(126.978371, 37.5666091)];
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.mapView viewWillDisappear];
    [self clearOverlays];
}

- (void) requestAddressByCoordination:(NGeoPoint) point  {
    [self.mapView findPlacemarkAtLocation:point];
    [self setMarker:point];
}

- (void) onMapView:(NMapView *)mapView touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {

    UITouch *touch = [touches anyObject];
    
    // Get the specific point that was touched
    CGPoint scrPoint = [touch locationInView:self.mapView];
    [self requestAddressByCoordination:[self.mapView fromPoint:scrPoint]];
}

- (void) setMarker:(NGeoPoint)point {
    
    [self clearOverlays];
    
    NMapOverlayManager * mapOverlayManager = [self.mapView mapOverlayManager];
    // create POI data overlay
    NMapPOIdataOverlay * poiDataOverlay = [mapOverlayManager newPOIdataOverlay];

    [poiDataOverlay initPOIdata:1];
    [poiDataOverlay addPOIitemAtLocation:point title:@"마커 1" type:UserPOIflagTypeDefault iconIndex:0 withObject:nil];
    [poiDataOverlay endPOIdata];
}

- (void)clearOverlays {
    NMapOverlayManager *mapOverlayManager = [self.mapView mapOverlayManager];
    [mapOverlayManager clearOverlays];
}

#pragma mark MMapReverseGeocoderDelegate

- (void) location:(NGeoPoint)location didFindPlacemark:(NMapPlacemark *)placemark
{
    NSLog(@"location:(%f, %f) didFindPlacemark: %@", location.longitude, location.latitude, [placemark address]);
    self.title = [placemark address];
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"ReverseGeocoder"
                                  message:[placemark address]
                                  preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:nil];
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
 }

- (void) location:(NGeoPoint)location didFailWithError:(NMapError *)error
{
    NSLog(@"location:(%f, %f) didFailWithError: %@", location.longitude, location.latitude, [error description]);
}



@end
