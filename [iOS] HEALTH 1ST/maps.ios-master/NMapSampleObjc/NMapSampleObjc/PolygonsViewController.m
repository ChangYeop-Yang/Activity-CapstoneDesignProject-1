//
//  PolygonsViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 16..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "PolygonsViewController.h"
#import "NMapViewResources.h"

@interface PolygonsViewController ()

@property (nonatomic, strong) NMapView * mapView;

@end

@implementation PolygonsViewController

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
    [self addPolygons];
}

- (void)viewWillDisappear:(BOOL)animated {
    [self clearOverlays];
    [self.mapView viewWillDisappear];
    [super viewWillDisappear:animated];
}

- (void) addPolygons {
    [self clearOverlays];
    
    NMapOverlayManager *mapOverlayManager = [self.mapView mapOverlayManager];
    
    // set path data points
    NMapPathData *pathData1 = [[NMapPathData alloc] initWithCapacity:9];
    
    [pathData1 initPathData];
    [pathData1 addPathPointLongitude:127.108099 latitude:37.366034 lineType:NMapPathLineTypeSolid];
    [pathData1 addPathPointLongitude:127.108088 latitude:37.366043 lineType:0];
    [pathData1 addPathPointLongitude:127.108079 latitude:37.365619 lineType:0];
    [pathData1 addPathPointLongitude:127.107458 latitude:37.365608 lineType:0];
    [pathData1 addPathPointLongitude:127.107232 latitude:37.365608 lineType:0];
    [pathData1 addPathPointLongitude:127.106904 latitude:37.365624 lineType:0];
    [pathData1 addPathPointLongitude:127.105933 latitude:37.365621 lineType:0];
    [pathData1 addPathPointLongitude:127.105929 latitude:37.366378 lineType:0];
    [pathData1 addPathPointLongitude:127.106279 latitude:37.366380 lineType:0];
    [pathData1 endPathData];
    
    NMapPathLineStyle *style1 = [[NMapPathLineStyle alloc] init];
    [style1 setPathDataType:NMapPathDataTypePolygon];
    [style1 setLineColor:[UIColor greenColor]];
    [style1 setFillColor:[UIColor blueColor]];
    [pathData1 setPathLineStyle:style1];
    
    // create path data overlay
    NMapPathDataOverlay *pathDataOverlay = [mapOverlayManager newPathDataOverlay:pathData1];
    if (pathDataOverlay) {
        // set path data points
        NMapPathData *pathData2 = [[NMapPathData alloc] initWithCapacity:7];
        
        [pathData2 initPathData];
        [pathData2 addPathPointLongitude:127.108099 latitude:37.367034 lineType:NMapPathLineTypeSolid];
        [pathData2 addPathPointLongitude:127.108088 latitude:37.367043 lineType:0];
        [pathData2 addPathPointLongitude:127.108079 latitude:37.366619 lineType:0];
        [pathData2 addPathPointLongitude:127.107458 latitude:37.366608 lineType:0];
        [pathData2 addPathPointLongitude:127.107232 latitude:37.366608 lineType:0];
        [pathData2 addPathPointLongitude:127.106904 latitude:37.366624 lineType:0];
        [pathData2 addPathPointLongitude:127.106904 latitude:37.367721 lineType:0];
        [pathData2 endPathData];
        
        NMapPathLineStyle *style2 = [[NMapPathLineStyle alloc] init];
        [style2 setPathDataType:NMapPathDataTypePolygon];
        [style2 setLineColor:[UIColor blueColor]];
        [style2 setFillColor:[UIColor greenColor]];
        [pathData2 setPathLineStyle:style2];
        
        [pathDataOverlay addPathData:pathData2];
    }
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
