//
//  ViewController.swift
//  NMapSampleSwift
//
//  Created by Naver on 2016. 11. 8..
//  Copyright © 2016년 Naver. All rights reserved.
//

import UIKit

class MainViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch(indexPath.row) {
        case 1:
            let viewController = LocationMapViewController()
            viewController.title = "내위치"
            self.navigationController?.pushViewController(viewController, animated: true)
        case 2:
            let viewController = ReverseGeocoderViewController()
            viewController.title = "주소찾기"
            self.navigationController?.pushViewController(viewController, animated: true)
        case 4:
            let viewController = MovableMarkerViewController()
            viewController.title = "이동 가능한 마커 그리기"
            self.navigationController?.pushViewController(viewController, animated: true)
        case 5:
            let viewController = PolylinesViewController()
            viewController.title = "폴리라인 그리기"
            self.navigationController?.pushViewController(viewController, animated: true)
        case 6:
            let viewController = PolygonsViewController()
            viewController.title = "폴리곤 그리기"
            self.navigationController?.pushViewController(viewController, animated: true)
        case 7:
            let viewController = CirclesViewController()
            viewController.title = "원 그리기"
            self.navigationController?.pushViewController(viewController, animated: true)
        default:
            break
        }
    }

}
