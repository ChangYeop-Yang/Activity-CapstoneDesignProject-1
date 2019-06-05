//
//  PatientAdapter.swift
//  Health1st
//
//  Created by 양창엽 on 2017. 11. 15..
//  Copyright © 2017년 Yang-Chang-Yeop. All rights reserved.
//

import Foundation
import SwiftyJSON

class PatientAdapter
{
    /* MARK - : String */
    fileprivate var sName:String! = nil
    fileprivate var sBlood:String! = nil
    
    /* MARK - : Integer */
    fileprivate var mAge:Int! = 0
    fileprivate var mWeight:Int! = 0
    fileprivate var mHeight:Int! = 0
    fileprivate var mHRM:Int! = 0
    fileprivate var mSPO2:Int = 0
    
    /* MARK - : Double */
    fileprivate var dLatitude:Double! = 0.0
    fileprivate var dLongitude:Double! = 0.0
    
    /* MARK - : Init */
    init(json:JSON) {
        
        /* POINT - : String */
        sName = json["name"][0]["text"].stringValue
        sBlood = json["contained"][0]["bodySite"]["coding"][2]["display"].stringValue
        
        /* POINT - : Integer */
        mAge = json["contained"][0]["category"]["coding"][0]["display"].intValue
        mWeight = json["contained"][0]["bodySite"]["coding"][0]["display"].intValue
        mHeight = json["contained"][0]["bodySite"]["coding"][1]["display"].intValue
        mHRM = json["contained"][0]["bodySite"]["coding"][3]["display"].intValue
        mSPO2 = json["contained"][0]["bodySite"]["coding"][4]["display"].intValue
        
        /* POINT - : Double */
        dLatitude = Double(json["contained"][0]["category"]["coding"][5]["display"].stringValue.split(separator: ",")[0])
        dLongitude = Double(json["contained"][0]["category"]["coding"][5]["display"].stringValue.split(separator: ",")[1])
    }
    
    /* MARK - Getter Method */
    public final func getPatientName() -> String { return self.sName }
    public final func getPatientBlood() -> String { return self.sBlood }
    public final func getPatientAge() -> Int { return self.mAge }
    public final func getPatientWeight() -> Int { return self.mWeight }
    public final func getPatientHeight() -> Int { return self.mHeight }
    public final func getPatientHRM() -> Int { return self.mHRM }
    public final func getPatientSPO2() -> Int { return self.mSPO2 }
    public final func getPatientLocation() -> (Latitude:Double, Longitude:Double) { return (self.dLatitude, self.dLongitude)  }
}
