//
//  CustomMarkersMapViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 11..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "CustomMarkersMapViewController.h"
#import "NMapViewResources.h"

@interface CustomMarkersMapViewController ()

@property (nonatomic, strong) NMapView * mapView;
@property (strong, nonatomic) IBOutlet UIView *calloutView;
@property (weak, nonatomic) IBOutlet UILabel *calloutLabel;

@end

@implementation CustomMarkersMapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView = [[NMapView alloc] initWithFrame:self.view.frame];
    self.navigationController.navigationBar.translucent = NO;
   
    // set the delegate for map view
    [self.mapView setDelegate:self];

    // set the application api key for Open MapViewer Library
    [self.mapView setClientId:@"YOUR CLIENT ID"];
 
    self.mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

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

#pragma mark CustomMarkersMapViewController

- (void) viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self addMarkers];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self clearOverlays];
    [self.mapView viewWillDisappear];
}

- (void) addMarkers {
    NMapOverlayManager *mapOverlayManager = [self.mapView mapOverlayManager];
    // create POI data overlay
    NMapPOIdataOverlay *poiDataOverlay = [mapOverlayManager newPOIdataOverlay];
    
    [poiDataOverlay initPOIdata:3];
    [poiDataOverlay addPOIitemAtLocation:NGeoPointMake(126.979, 37.567) title:@"마커 1" type:UserPOIflagTypeDefault iconIndex:0 withObject:nil];
    [poiDataOverlay addPOIitemAtLocation:NGeoPointMake(126.974, 37.566) title:@"마커 2" type:UserPOIflagTypeDefault iconIndex:1 withObject:nil];
    [poiDataOverlay addPOIitemAtLocation:NGeoPointMake(126.984, 37.565) title:@"마커 3" type:UserPOIflagTypeInvisible iconIndex:2 withObject:nil];
    [poiDataOverlay endPOIdata];
    
    // show all POI data
    [poiDataOverlay showAllPOIdata];
    
    [poiDataOverlay selectPOIitemAtIndex:2 moveToCenter:NO focusedBySelectItem:YES];
}

- (void)clearOverlays {
    NMapOverlayManager *mapOverlayManager = [self.mapView mapOverlayManager];
    [mapOverlayManager clearOverlays];
}

#pragma mark NMapPOIdataOverlayDelegate

- (UIImage *) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForOverlayItem:(NMapPOIitem *)poiItem selected:(BOOL)selected {
    return [NMapViewResources imageWithType:poiItem.poiFlagType selected:selected];
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay anchorPointWithType:(NMapPOIflagType)poiFlagTyp{
    return [NMapViewResources anchorPoint:poiFlagTyp];
}

- (UIView *)onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay viewForCalloutOverlayItem:(NMapPOIitem *)poiItem
         calloutPosition:(CGPoint *)calloutPosition
{
    calloutPosition->x = roundf((self.calloutView.bounds.size.width)/2) + 1;
    self.calloutLabel.text = poiItem.title;
    return self.calloutView;
}

- (UIImage*) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForCalloutOverlayItem:(NMapPOIitem *)poiItem
           constraintSize:(CGSize)constraintSize selected:(BOOL)selected
imageForCalloutRightAccessory:(UIImage *)imageForCalloutRightAccessory
          calloutPosition:(CGPoint *)calloutPosition calloutHitRect:(CGRect *)calloutHitRect {
    return nil;
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay calloutOffsetWithType:(NMapPOIflagType)poiFlagType {
    return CGPointMake(0.5, 0.5);
}



@end
