//
//  MainViewController.m
//  NMapSampleObjc
//
//  Created by Naver on 2016. 11. 11..
//  Copyright © 2016년 Naver. All rights reserved.
//

#import "MainViewController.h"
#import "PolylinesViewController.h"
#import "PolygonsViewController.h"
#import "CirclesViewController.h"
#import "CustomMarkersMapViewController.h"
#import "LocationMapViewController.h"
#import "ReverseGeocoderViewController.h"
#import "MovableMarkerViewController.h"
#import "RotationViewController.h"

@interface MainViewController ()

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    switch (indexPath.row) {
        case 1:
            {
                LocationMapViewController * viewController = [[LocationMapViewController alloc] init];
                viewController.title = @"내위치";
                [self.navigationController pushViewController:viewController animated:YES];
            }
            break;
        case 2:
            {
                ReverseGeocoderViewController * viewController = [[ReverseGeocoderViewController alloc] init];
                viewController.title = @"주소찾기";
                [self.navigationController pushViewController:viewController animated:YES];
            }
            break;
        case 4:
             {
             MovableMarkerViewController * viewController = [[MovableMarkerViewController alloc] init];
             viewController.title = @"마커 그리기";
             [self.navigationController pushViewController:viewController animated:YES];
             }
            break;
       case 5:
            {
                PolylinesViewController * viewController = [[PolylinesViewController alloc] init];
                viewController.title = @"선 그리기";
                [self.navigationController pushViewController:viewController animated:YES];
            }
            break;
        case 6:
            {
                PolygonsViewController * viewController = [[PolygonsViewController alloc] init];
                viewController.title = @"면 그리기";
                [self.navigationController pushViewController:viewController animated:YES];
            }
            break;
        case 7:
            {
                CirclesViewController * viewController = [[CirclesViewController alloc] init];
                viewController.title = @"원 그리기";
                [self.navigationController pushViewController:viewController animated:YES];
            }
            break;
        default:
            break;
    }
}

@end
