//
//  CirclesViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 16..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "CirclesViewController.h"
#import "NMapViewResources.h"

@interface CirclesViewController ()

@property (nonatomic, strong) NMapView * mapView;

@end

@implementation CirclesViewController

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
    [self addCircles];
}

- (void)viewWillDisappear:(BOOL)animated {
    [self clearOverlays];
    [self.mapView viewWillDisappear];
    [super viewWillDisappear:animated];
}

- (void) addCircles {
    [self clearOverlays];
    
    NMapOverlayManager *mapOverlayManager = [self.mapView mapOverlayManager];
    NMapPathDataOverlay *pathDataOverlay = [mapOverlayManager newPathDataOverlay:[[NMapPathData alloc] init]];

    // add circle data
    NMapCircleData *circleData1 = [[NMapCircleData alloc] initWithCapacity:1];
    [circleData1 initCircleData];
    [circleData1 addCirclePointLongitude:127.1085 latitude:37.3675 radius:50.0F];
    // set circle style
    NMapCircleStyle *circleStyle1 = [[NMapCircleStyle alloc] init];
    [circleStyle1 setLineType:NMapPathLineTypeDash];
    [circleData1 setCircleStyle:circleStyle1];
    [circleData1 endCircleData];

    [pathDataOverlay addCircleData:circleData1];

    // add circle data
    NMapCircleData *circleData2 = [[NMapCircleData alloc] initWithCapacity:1];
    [circleData2 initCircleData];
    [circleData2 addCirclePointLongitude:127.1065 latitude:37.3685 radius:27.0F];
    [circleData2 endCircleData];
    // set circle style
    NMapCircleStyle *circleStyle2 = [[NMapCircleStyle alloc] init];
    [circleStyle2 setLineType:NMapPathLineTypeSolid];
    [circleStyle2 setFillColor:[UIColor greenColor]];
    [circleData2 setCircleStyle:circleStyle2];
    
    [pathDataOverlay addCircleData:circleData2];
    
    // add circle data
    NMapCircleData *circleData3 = [[NMapCircleData alloc] initWithCapacity:1];
    [circleData3 initCircleData];
    [circleData3 addCirclePointLongitude:127.1085 latitude:37.3685 radius:35.0F];
    [circleData3 endCircleData];
    // set circle style
    NMapCircleStyle *circleStyle3 = [[NMapCircleStyle alloc] init];
    [circleStyle3 setLineType:NMapPathLineTypeSolid];
    [circleStyle3 setFillColor:[UIColor grayColor]];
    [circleData3 setCircleStyle:circleStyle3];
    
    [pathDataOverlay addCircleData:circleData3];
    [pathDataOverlay showAllPathData];
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

- (UIImage*) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForCalloutOverlayItem:(NMapPOIitem *)poiItem
           constraintSize:(CGSize)constraintSize selected:(BOOL)selected
imageForCalloutRightAccessory:(UIImage *)imageForCalloutRightAccessory
          calloutPosition:(CGPoint *)calloutPosition calloutHitRect:(CGRect *)calloutHitRect {
    return nil;
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay calloutOffsetWithType:(NMapPOIflagType)poiFlagType {
    return CGPointZero;
}

@end
