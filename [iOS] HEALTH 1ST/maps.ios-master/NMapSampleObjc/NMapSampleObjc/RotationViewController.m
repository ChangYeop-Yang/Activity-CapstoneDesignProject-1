//
//  RotationViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 21..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "RotationViewController.h"
#import "NMapViewResources.h"

@interface RotationViewController ()

@property (nonatomic, strong) NMapView * mapView;
@property (nonatomic) CGFloat viewAngle;

- (IBAction)leftButtonClicked:(id)sender;
- (IBAction)rightButtonClicked:(id)sender;

@end

@implementation RotationViewController

BOOL hasInit = NO;

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

- (void) viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator {
    
    if (size.width != self.view.frame.size.width) {
        if ([self.mapView isAutoRotateEnabled] == YES && hasInit == YES) {
            self.viewAngle = self.mapView.rotateAngle;
            [self.mapView setAutoRotateEnabled:NO animate:NO];
            [coordinator animateAlongsideTransition:^ (id <UIViewControllerTransitionCoordinatorContext> context) {
                if (hasInit == YES) {
                    [self.mapView setAutoRotateEnabled:YES animate:YES];
                    [self.mapView setRotateAngle:self.viewAngle];
                }
        
            } completion:nil];
        }
    }
    
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
        hasInit = YES;
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

#pragma mark RotationViewController

- (IBAction)leftButtonClicked:(id)sender {
    if (self.mapView.isAutoRotateEnabled == NO) {
        [self.mapView setAutoRotateEnabled:YES animate:YES];
    }
    self.viewAngle += 45.f;
    [self.mapView setRotateAngle:self.viewAngle];
}

- (IBAction)rightButtonClicked:(id)sender {
    if (self.mapView.isAutoRotateEnabled == NO) {
        [self.mapView setAutoRotateEnabled:YES animate:YES];
    }
    self.viewAngle -= 45.f;
    [self.mapView setRotateAngle:self.viewAngle];
}
@end
