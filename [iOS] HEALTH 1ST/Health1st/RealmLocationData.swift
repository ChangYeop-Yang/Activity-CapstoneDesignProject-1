//
//  RealmData.swift
//  Health1st
//
//  Created by 양창엽 on 2017. 11. 21..
//  Copyright © 2017년 Yang-Chang-Yeop. All rights reserved.
//

import RealmSwift

public class RealmLocationData:Object {
    
    /* MARK - : Double */
    @objc dynamic var latitude:Double = 0.0
    @objc dynamic var longitude:Double = 0.0
    
    /* MARK - : String */
    @objc dynamic var date:String! = nil
}
