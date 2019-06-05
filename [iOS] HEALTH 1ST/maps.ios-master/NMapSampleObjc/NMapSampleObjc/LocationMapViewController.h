//
//  LocationMapViewController.h
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 11..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <NMapViewerSDK/NMapViewerSDK.h>

@interface LocationMapViewController : UIViewController<NMapViewDelegate, NMapPOIdataOverlayDelegate, NMapLocationManagerDelegate>

@end
