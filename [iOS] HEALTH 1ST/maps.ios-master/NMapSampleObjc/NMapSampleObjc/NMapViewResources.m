//
//  NMapViewResources.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 23..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "NMapViewResources.h"

@implementation NMapViewResources

+ (UIImage *)imageWithType:(NMapPOIflagType) poiFlagType selected:(BOOL)selected {
    switch (poiFlagType) {
        case NMapPOIflagTypeLocation:
            return [UIImage imageNamed:@"pubtrans_ic_mylocation_on"];
        case NMapPOIflagTypeLocationOff:
            return [UIImage imageNamed:@"pubtrans_ic_mylocation_off"];
        case NMapPOIflagTypeCompass:
            return [UIImage imageNamed:@"ic_angle"];
        case UserPOIflagTypeDefault:
            return [UIImage imageNamed:@"pubtrans_exact_default"];
        case UserPOIflagTypeInvisible:
            return [UIImage imageNamed:@"1px_dot"];
        default:
            return nil;
    }
}

+ (CGPoint)anchorPoint:(NMapPOIflagType)poiFlagType {
    switch (poiFlagType) {
        case NMapPOIflagTypeLocation:
            return CGPointMake(0.5, 0.5);
        case NMapPOIflagTypeLocationOff:
            return CGPointMake(0.5, 0.5);
        case NMapPOIflagTypeCompass:
            return CGPointMake(0.5, 0.5);
        case UserPOIflagTypeDefault:
            return CGPointMake(0.5, 1.0);
        case UserPOIflagTypeInvisible:
            return CGPointMake(0.5, 0.5);
        default:
            return CGPointMake(0.5, 0.5);
    }
}

@end
