//
//  ChartViewController.swift
//  Health1st
//
//  Created by 양창엽 on 2017. 11. 21..
//  Copyright © 2017년 Yang-Chang-Yeop. All rights reserved.
//

import UIKit
import Charts
import RealmSwift

class ChartViewController:UIViewController{
    
    /* MARK - : IBoutlet Variable */
    @IBOutlet weak var lb_LastTime: UILabel!
    @IBOutlet weak var lcv_Meaure: LineChartView!
    
    /* MARK - : LineChart Color Variable */
    fileprivate let colors = [UIColor(red: 137/255, green: 230/255, blue: 81/255, alpha: 1),
                  UIColor(red: 240/255, green: 240/255, blue: 30/255, alpha: 1),
                  UIColor(red: 89/255, green: 199/255, blue: 250/255, alpha: 1),
                  UIColor(red: 250/255, green: 104/255, blue: 104/255, alpha: 1)]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /* MARK - : Realm Helath Data */
        var anHRM:[Int] = [Int]()
        var anSPO2:[Int] = [Int]()
        var sLastDate:String! = nil
        if let mQuery = try? Realm().objects(RealmHealthData.self)
        {
            /* POINT - : Health Data */
            for item in mQuery { anHRM.append(item.mHRM); anSPO2.append(item.mSPO2); sLastDate = item.date }
            lb_LastTime.text = sLastDate
        }
        
        /* POINT - : LineChartDataSet */
        let dataHRMSet = createChartData(mLable: "HRM [BPM]", mColor: colors[0], mValues: anHRM)
        let dataSPO2Set = createChartData(mLable: "SPO2 [%]", mColor: colors[1], mValues: anSPO2)
        let chartData = LineChartData(dataSets: [dataHRMSet, dataSPO2Set])
        chartData.setDrawValues(true)
        chartData.setValueTextColor(.black)
        
        /* MARK - : LineChart */
        lcv_Meaure.noDataText = "현재 측정 된 건강 정보가 없습니다."
        lcv_Meaure.descriptionText = "Patient Health Care Information Chart"
        lcv_Meaure.animate(xAxisDuration: 2.0, yAxisDuration: 2.0, easingOption: ChartEasingOption.linear)
        lcv_Meaure.data = chartData
    }
    
    /* TODO - : Create Chart Data Method */
    private func createChartData(mLable:String, mColor:NSUIColor, mValues:[Int]) -> LineChartDataSet {
        
        /* POINT - : Values Set */
        var dataEntries:[ChartDataEntry] = [ChartDataEntry]()
        for mCount in 0..<mValues.count {
            let dataEntry = ChartDataEntry(x: Double(mCount), y: Double(mValues[mCount]))
            dataEntries.append(dataEntry)
        }
        
        /* POINT - : LineChartDataSet */
        let setData = LineChartDataSet(values: dataEntries, label: mLable)
        setData.lineWidth = 1.75
        setData.circleRadius = 5.0
        setData.circleHoleRadius = 2.5
        setData.setColor(mColor)
        setData.setCircleColor(mColor)
        setData.highlightColor = mColor
        setData.drawValuesEnabled = false
        
        return setData
    }

    /* TODO - : IBAction Method */
    @IBAction func actPitcure(_ sender: UIBarButtonItem) {
        lcv_Meaure.save(to: "./chart.png", format: ChartViewBase.ImageFormat.png, compressionQuality: 1.0)
        
        /* POINT - : Sweet Alert Dialog */
        SweetAlert().showAlert("CAPTURE CHART", subTitle: "환자의 건강정보 차트를 저장하였습니다.", style: AlertStyle.success, buttonTitle: "확인")
    }
}
