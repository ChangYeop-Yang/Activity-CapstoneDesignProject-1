//
//  LocationMapViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 11..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "LocationMapViewController.h"
#import "NMapViewResources.h"

typedef enum : NSUInteger {
    kDisabled, // for your value; kCircle = 5, ...
    kTracking,
    kTrackingWithHeading
} State;


@interface LocationMapViewController ()

@property (nonatomic, strong) NMapView * mapView;
@property (nonatomic) State currentState;
@property (nonatomic, strong) UIButton * changeStateButton;
@end

#define kMapInvalidCompassValue (-360)

@implementation LocationMapViewController


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
    
    self.currentState = kDisabled;
    
    // ceate the button for command to get the current location
    UIButton * button = [[UIButton alloc] initWithFrame:CGRectMake(15, 30, 36, 36)];
    [button setImage:[UIImage imageNamed:@"v4_btn_navi_location_normal"] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(buttonClicked:) forControlEvents:UIControlEventTouchUpInside];
    self.changeStateButton = button;
    [self.view addSubview:button];
}


- (void) viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator {
    
    if (size.width != self.view.frame.size.width) {
        if ([self.mapView isAutoRotateEnabled] == YES) {
            [self.mapView setAutoRotateEnabled:NO animate:NO];
            [coordinator animateAlongsideTransition:^ (id <UIViewControllerTransitionCoordinatorContext> context) {
                [self.mapView setAutoRotateEnabled:YES animate:YES];
            } completion:nil];
        }
    }
}

- (void) willTransitionToTraitCollection:(UITraitCollection *)newCollection withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator {
    if (self.view.traitCollection.horizontalSizeClass != newCollection.horizontalSizeClass ||
        self.view.traitCollection.verticalSizeClass != newCollection.verticalSizeClass) {
        if ([self.mapView isAutoRotateEnabled] == YES) {
            [self.mapView setAutoRotateEnabled:NO animate:NO];
        }
    }
}

- (void) traitCollectionDidChange:(UITraitCollection *)previousTraitCollection {
    if (self.view.traitCollection.horizontalSizeClass != previousTraitCollection.horizontalSizeClass ||
        self.view.traitCollection.verticalSizeClass != previousTraitCollection.verticalSizeClass) {
        if (self.currentState == kTrackingWithHeading) {
            [self.mapView setAutoRotateEnabled:YES animate:YES];
        }
    }
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

// return UIImage for callout overlay item, otherwise return nil and use onMapOverlay:viewForCalloutOverlayItem:calloutPosition:
- (UIImage*) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay imageForCalloutOverlayItem:(NMapPOIitem *)poiItem
           constraintSize:(CGSize)constraintSize selected:(BOOL)selected
imageForCalloutRightAccessory:(UIImage *)imageForCalloutRightAccessory
          calloutPosition:(CGPoint *)calloutPosition calloutHitRect:(CGRect *)calloutHitRect {
    return nil;
}

- (CGPoint) onMapOverlay:(NMapPOIdataOverlay *)poiDataOverlay calloutOffsetWithType:(NMapPOIflagType)poiFlagType {
    return CGPointZero;
}

#pragma mark LocationMapViewController

- (void) viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
 //   [self findCurrentLocation];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.mapView viewWillDisappear];
    [self stopLocationUpdating];
}

- (void)locationManager:(NMapLocationManager *)locationManager didUpdateToLocation:(CLLocation *)location {
    
    CLLocationCoordinate2D coordinate = [location coordinate];
    
    NGeoPoint myLocation;
    myLocation.longitude = coordinate.longitude;
    myLocation.latitude = coordinate.latitude;
    float locationAccuracy = [location horizontalAccuracy];

    [[self.mapView mapOverlayManager] setMyLocation:myLocation locationAccuracy:locationAccuracy];
    [self.mapView setMapCenter:myLocation];
}

- (void)locationManager:(NMapLocationManager *)locationManager didFailWithError:(NMapLocationManagerErrorType)errorType {
    NSString *message = nil;
    switch (errorType) {
        case NMapLocationManagerErrorTypeUnknown:
        case NMapLocationManagerErrorTypeCanceled:
        case NMapLocationManagerErrorTypeTimeout:
            message = @"일시적으로 내위치를 확인 할 수 없습니다.";
            break;
        case NMapLocationManagerErrorTypeDenied:
            if ( [[[UIDevice currentDevice] systemVersion] floatValue] >= 4.0f )
                message = @"위치 정보를 확인 할 수 없습니다.\n사용자의 위치 정보를 확인하도록 허용하시려면 위치서비스를 켜십시오.";
            else
                message = @"위치 정보를 확인 할 수 없습니다.";
            break;
        case NMapLocationManagerErrorTypeUnavailableArea:
            message = @"현재 위치는 지도내에 표시 할 수 없습니다.";
            break;
        case NMapLocationManagerErrorTypeHeading:
            message = @"나침반 정보를 확인 할 수 없습니다.";
            break;
        default:
            break;
    }
    if (message) {
        
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"NMapViewer"
                                      message:message
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* ok = [UIAlertAction
                             actionWithTitle:@"OK"
                             style:UIAlertActionStyleDefault
                             handler:nil];
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
    if ([self.mapView isAutoRotateEnabled]) {
        [self.mapView setAutoRotateEnabled:NO animate:YES];
    }
    
    [[self.mapView mapOverlayManager] clearMyLocationOverlay];
}

- (void)locationManager:(NMapLocationManager *)locationManager didUpdateHeading:(CLHeading *)heading {
    double headingValue = [heading trueHeading] < 0.0 ? [heading magneticHeading] : [heading trueHeading];
    [self setCompassHeadingValue:headingValue];
}

- (BOOL) onMapViewIsGPSTracking:(NMapView *)mapView {
    return [[NMapLocationManager getSharedInstance] isTrackingEnabled];
}

#pragma mark LocationMapViewController


- (void)findCurrentLocation {
    [self enableLocationUpdate];
}

- (void) setCompassHeadingValue : (CGFloat) headingValue {
    if ([self.mapView isAutoRotateEnabled] == YES) {
        [self.mapView setRotateAngle:headingValue animate:YES];
    }
}

- (void) stopLocationUpdating {
    [self disableHeading];
    [self disableLocationUpdate];
}

- (void) enableLocationUpdate {
    NMapLocationManager *lm = [NMapLocationManager getSharedInstance];
    if ([lm locationServiceEnabled] == NO) {
        [self locationManager:lm didFailWithError:NMapLocationManagerErrorTypeDenied];
        return;
    }
    if ([lm isUpdateLocationStarted] == NO){
        // set delegate
        [lm setDelegate:self];
        // start updating location
        [lm startContinuousLocationInfo];
    }
}

- (void) disableLocationUpdate {
    NMapLocationManager *lm = [NMapLocationManager getSharedInstance];
    if ([lm isUpdateLocationStarted]){
        // start updating location
        [lm stopUpdateLocationInfo];
        // set delegate
        [lm setDelegate:nil];
    }
    [[self.mapView mapOverlayManager] clearOverlays];
}

- (BOOL) enableHeading {
    NMapLocationManager *lm = [NMapLocationManager getSharedInstance];
    if (lm.headingAvailable == YES) {
        [self.mapView setAutoRotateEnabled:YES animate:YES];
        [lm startUpdatingHeading];
    } else {
        return NO;
    }
    return YES;
}

- (void) disableHeading {
    NMapLocationManager *lm = [NMapLocationManager getSharedInstance];
    if (lm.headingAvailable == YES) {
        [lm stopUpdatingHeading];
    }
    [self.mapView setAutoRotateEnabled:NO animate:YES];
}

- (void) buttonClicked:(UIButton*) sender {
    NMapLocationManager *lm = [NMapLocationManager getSharedInstance];
    switch (_currentState) {
        case kDisabled:
            [self enableLocationUpdate];
            [self updateState:kTracking];
            break;
        case kTracking:
            if (lm.headingAvailable == YES) {
                [self enableLocationUpdate];
                if ([self enableHeading] == YES) {
                    [self updateState:kTrackingWithHeading];
                }
            } else {
                [self stopLocationUpdating];
                [self updateState:kDisabled];
            }
            break;
        case kTrackingWithHeading:
            [self stopLocationUpdating];
            [self updateState:kDisabled];
            break;
    }
}

- (void) updateState : (State) newState {
    
    self.currentState = newState;
    switch (self.currentState) {
        case kDisabled:
            [self.changeStateButton setImage:[UIImage imageNamed:@"v4_btn_navi_location_normal"] forState:UIControlStateNormal];
            break;
        case kTracking:
            [self.changeStateButton setImage:[UIImage imageNamed:@"v4_btn_navi_location_selected"] forState:UIControlStateNormal];
            break;
        case kTrackingWithHeading:
            [self.changeStateButton setImage:[UIImage imageNamed:@"v4_btn_navi_location_my"] forState:UIControlStateNormal];
            break;
    }
}

@end
