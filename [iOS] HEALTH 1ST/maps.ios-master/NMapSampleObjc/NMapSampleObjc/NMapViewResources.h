//
//  NMapViewResources.h
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 23..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import <NMapViewerSDK/NMapViewerSDK.h>

//
// POI flag type : User Defined
//
static const NMapPOIflagType UserPOIflagTypeDefault = NMapPOIflagTypeReserved + 1;
static const NMapPOIflagType UserPOIflagTypeInvisible = NMapPOIflagTypeReserved + 2;

@interface NMapViewResources : NSObject

+ (UIImage *)imageWithType:(NMapPOIflagType) poiFlagType selected:(BOOL)selected;

+ (CGPoint)anchorPoint:(NMapPOIflagType)poiFlagType;

@end
