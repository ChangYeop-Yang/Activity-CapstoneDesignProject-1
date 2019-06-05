//
//  MapViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 10..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "MapViewController.h"
#import "NMapViewResources.h"

@interface MapViewController ()

@property (nonatomic, strong) NMapView * mapView;
@property (weak, nonatomic) IBOutlet UIStepper *levelStepper;

@end

@implementation MapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView = [[NMapView alloc] initWithFrame:self.view.frame];
    self.navigationController.navigationBar.translucent = NO;
    
    // set the delegate for map view
    [self.mapView setDelegate:self];
    
    // set the application api key for Open MapViewer Library
    [self.mapView setClientId:@"YOUR CLIENT ID"];
 
    self.mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    
    [self.view insertSubview:self.mapView atIndex:0];

    // set min and max level for range in the UIStepper
    [self initLevelStepper:self.mapView.minZoomLevel maxValue:self.mapView.maxZoomLevel initialValue:11];
    [self.view bringSubviewToFront:self.levelStepper];
    
    [self.mapView setBuiltInAppControl:YES];
}

- (void) viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self.mapView setFrame:self.view.bounds];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.mapView viewWillAppear];
}

- (void)viewDidAppear:(BOOL)animated {
    [self.mapView viewDidAppear];
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.mapView viewWillDisappear];
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

#pragma mark MapViewController

- (IBAction)showOverlayLayers:(UIBarButtonItem *)sender {

    UIAlertController* alertController = [UIAlertController alertControllerWithTitle:@"Overlay Layers"
                                                                   message:nil
                                                                 preferredStyle:UIAlertControllerStyleActionSheet];

    UIAlertAction* trafficAction = [UIAlertAction actionWithTitle:[NSString stringWithFormat:@"Traffic layer is %@", (self.mapView.mapViewTrafficMode ? @"On" : @"Off")]
                                                           style:UIAlertActionStyleDefault
                                                         handler:^(UIAlertAction * action) {
                                                             self.mapView.mapViewTrafficMode = !self.mapView.mapViewTrafficMode;
                                                         }];
    UIAlertAction* bicycleAction = [UIAlertAction actionWithTitle:[NSString stringWithFormat:@"Bicycle layer is %@", (self.mapView.mapViewBicycleMode ? @"On" : @"Off")]
                                                            style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              self.mapView.mapViewBicycleMode = !self.mapView.mapViewBicycleMode;
                                                          }];
    UIAlertAction* alphaAction = [UIAlertAction actionWithTitle:[NSString stringWithFormat:@"Alpha layer is %@", (self.mapView.mapViewAlphaLayerMode ? @"On" : @"Off")]
                                                          style:UIAlertActionStyleDefault
                                                        handler:^(UIAlertAction * action) {
                                                            self.mapView.mapViewAlphaLayerMode = !self.mapView.mapViewAlphaLayerMode;
                                                            
//                                                            지도 위 반투명 레이어에 색을 지정할 때에는 다음 메서드를 사용한다
//                                                            [self.mapView setMapViewAlphaLayerMode:!self.mapView.mapViewAlphaLayerMode
//                                                                                         withColor:[UIColor colorWithRed:0.5 green:1.0 blue:0.5 alpha:0.9]];
                                                        }];

    [alertController addAction:trafficAction];
    [alertController addAction:bicycleAction];
    [alertController addAction:alphaAction];
    [alertController addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil]];
    [self presentViewController:alertController animated:YES completion:nil];
}

- (IBAction)changeMapMode:(UISegmentedControl *)sender {
    
    switch ([sender selectedSegmentIndex]) {
        case 0:
            [self.mapView setMapViewMode:NMapViewModeVector];
            break;
        case 1:
            [self.mapView setMapViewMode:NMapViewModeSatellite];
            break;
        case 2:
            [self.mapView setMapViewMode:NMapViewModeHybrid];
            break;
        default:
            [self.mapView setMapViewMode:NMapViewModeVector];
            break;
    }
}

- (void) initLevelStepper:(NSInteger)minValue maxValue:(NSInteger)max initialValue:(NSInteger) init {
    self.levelStepper.minimumValue = minValue;
    self.levelStepper.maximumValue = max;
    self.levelStepper.stepValue = 1;
    self.levelStepper.value = init;
    
}
- (IBAction)levelStepperValeChanged:(UIStepper *)sender {
    [self.mapView setZoomLevel:sender.value];
}

@end
