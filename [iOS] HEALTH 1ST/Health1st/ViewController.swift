//
//  ViewController.swift
//  Health1st
//
//  Created by 양창엽 on 2017. 11. 13..
//  Copyright © 2017년 Yang-Chang-Yeop. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import MessageUI
import PopupDialog
import AudioToolbox.AudioServices

class ViewController: UIViewController, MFMessageComposeViewControllerDelegate {
    
    /* MARK - : Label Outlet Variable */
    @IBOutlet weak var lb_PatientName: UILabel!
    @IBOutlet weak var lb_PatientAge: UILabel!
    @IBOutlet weak var lb_PatientHW: UILabel!
    @IBOutlet weak var lb_PatientBlood: UILabel!
    @IBOutlet weak var lb_PatientHRM: UILabel!
    @IBOutlet weak var lb_PatientSPO2: UILabel!
    @IBOutlet weak var lb_PatientState: UILabel!

    /* MARK - : ImageView Outlet Variable */
    @IBOutlet weak var iv_PatientFace: UIImageView!
    @IBOutlet weak var iv_PatientState: UIImageView!
    
    /* MARK - : String Variable */
    fileprivate var sPatientTel:String! = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        /* POINT - : UserDefalut */
        let defaluts = UserDefaults.standard
        if let sPhone = defaluts.string(forKey: PublicData.SHREAD_PATIENT_PHONE) { getPatientData(sTelNumber: sPhone) }
        else { showEditAlert(title: "환자 휴대폰 번호 입력", message: "환자의 휴대폰 번호를 입력해주세요.") }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    /* MARK - : Button Action Method */
    @IBAction func act_HelperBtn(_ sender: UIButton) {
        
        /* POINT - : Vibrate */
        AudioServicesPlaySystemSoundWithCompletion(kSystemSoundID_Vibrate) {}
        
        switch sender.tag {
            /* POINT - : Patient Call Button */
            case 11 :
                if let mURL:URL = URL(string: "tel://01045829311)") { UIApplication.shared.openURL(mURL) }
            /* POINT - : Patient Message Button */
            case 12 : sendMessage(sMessage: "<#T##String#>", sTel: "<#T##String#>")
            
            default : print("")
        }
    }
    
    /* MARK - : Custom User Method */
    func getPatientData(sTelNumber:String)
    {
        /* POINT - : String */
        let mURL:String = "http://fhirtest.uhn.ca/baseDstu2/Patient/_search?telecom=\(sTelNumber)"
        
        /* POINT - : Almofire */
        Alamofire.request(mURL, method: .get).validate().responseJSON() { response in
            
            switch response.result
            {
                case .success(let data) :
                    
                /* POINT - : PatientAdapter */
                let mPatient:PatientAdapter = PatientAdapter(json:JSON(data)["entry"][0]["resource"])
                
                /* POINT - : Label */
                self.lb_PatientName.text = mPatient.getPatientName()
                self.lb_PatientAge.text = "\(mPatient.getPatientAge())세"
                self.lb_PatientHW.text = "\(mPatient.getPatientWeight())kg / \(mPatient.getPatientHeight())cm"
                self.lb_PatientHRM.text = "\(mPatient.getPatientHRM()) BPM"
                self.lb_PatientSPO2.text = "\(mPatient.getPatientSPO2())%"
                self.lb_PatientBlood.text = mPatient.getPatientBlood()
                self.lb_PatientState.text = self.setPatientState(mHRM: mPatient.getPatientHRM(), mSPO2: mPatient.getPatientSPO2())
                
                case .failure(let error) :
                    print(error)
            }
        }
    }
    
    func showEditAlert(title:String, message:String) {
        
        /* POINT - : Alert */
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addTextField { mTextFiled in mTextFiled.keyboardType = UIKeyboardType.namePhonePad }
        alert.addAction(UIAlertAction(title: "확인", style: .default, handler: { [weak alert] (_) in
            
            if let sTel = alert?.textFields![0].text {
                /* POINT - : Save Patient Phone-Number & Get Patient Data */
                UserDefaults.standard.set(sTel, forKey: PublicData.SHREAD_PATIENT_PHONE);
                self.getPatientData(sTelNumber: sTel)
            }
        }))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    func setPatientState(mHRM:Int, mSPO2:Int) -> String {
        
        var sPatientMessage:String! = nil
        
        /* POINT - : Check HRM and SPO2 */
        switch (mHRM, mSPO2) {
            case (110...120, 97...93) : sPatientMessage = "위험상태"; self.iv_PatientState.image = #imageLiteral(resourceName: "ic_circle_yel.png")
            case (120...150, 92...80) : sPatientMessage = "긴급상태"; self.iv_PatientState.image = #imageLiteral(resourceName: "ic_circle_red.png")
            default : sPatientMessage = "일반상태"; self.iv_PatientState.image = #imageLiteral(resourceName: "ic_circle_gre.png")
        }
        
        return sPatientMessage
    }
    
    func sendMessage(sMessage:String, sTel:String) {
        
        /* POINT - : Check Message Service */
        if !MFMessageComposeViewController.canSendText() {
            print("SMS Service are not availeable")
            
            /* POINT - : Popup Alert Dialog */
            let mPopup = PopupDialog(title: "Error, Send SMS", message: "메세지 전송에 문제가 발생하였습니다.", image: nil)
            let mButton = DefaultButton(title: "확인", dismissOnTap: false) { mPopup.dismiss() }
            self.present(mPopup, animated: true, completion: nil)
        }
        
        /* POINT - : MFMessageComposeViewController */
        let composeVC:MFMessageComposeViewController = MFMessageComposeViewController()
        composeVC.messageComposeDelegate = self
        composeVC.recipients = [sTel];  composeVC.body = sMessage;
        self.present(composeVC, animated: true, completion: nil)
    }

    /* TODO - : MessageComposeViewController Method */
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        // Check the result or perform other tasks.
        
        // Dismiss the message compose view controller.
        controller.dismiss(animated: true, completion: nil)
    }
}

