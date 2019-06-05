//
//  ReverseGeocoderViewController.h
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 14..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <NMapViewerSDK/NMapViewerSDK.h>

@interface ReverseGeocoderViewController : UIViewController<NMapViewDelegate, NMapPOIdataOverlayDelegate, MMapReverseGeocoderDelegate>

@end
