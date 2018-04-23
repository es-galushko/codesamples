//
//  SharingViewController.swift
//  EffectiveSoft
//
//  Created by Alex Bakhtin on 7/21/17.
//  Copyright © 2017 EffectiveSoft. All rights reserved.
//

import UIKit
import DPLocalization

enum SharingType {
    case invite
    case suggest
}

class SharingViewController: UIViewController {

    // MARK: - Configuration

    private var user: SPRUser?
    private var type: SharingType!
    @IBOutlet var contentPopupView: UIView!
    @IBOutlet weak var titleLabel: UILabel!

    func configure(with user: SPRUser?, type: SharingType) {
        self.user = user
        self.type = type
    }

    // MARK: - View life cycle

    override func viewDidLoad() {
        super.viewDidLoad()
        contentPopupView.layer.borderWidth = 1
        contentPopupView.layer.borderColor = UIColor.black.cgColor
        
        switch type! {
        case .invite:
            titleLabel.text = "Invite friends"
        case .suggest:
            titleLabel.text = "Matchmake \(user!.name!) with a friend"
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        PandaSlideMenuController.applyCustomization()
    }

    // MARK: - Actions

    @IBAction func close(_: Any) {
        dismiss(animated: true, completion: nil)
    }

    @IBAction func sendEmail(_: Any) {
        SPRAnalyticsController.instance().sendEvent(SPRAnalyticsEventShareWithEmailButtonTapped)
        performSharing(with: .mail)
    }

    @IBAction func sendMessage(_: Any) {
        SPRAnalyticsController.instance().sendEvent(SPRAnalyticsEventShareMessagingButtonTapped)
        performSharing(with: .messaging)
    }

    @IBAction func sendThrowMessenger(_: Any) {
        SPRAnalyticsController.instance().sendEvent(SPRAnalyticsEventShareMessengerButtonTapped)
        performSharing(with: .messenger)
    }

    @IBAction func shareThrowOther(_: Any) {
        SPRAnalyticsController.instance().sendEvent(SPRAnalyticsEventShareOtherButtonTapped)
        performSharing(with: .all)
    }
    
    // MARK: - Sharing
    
    func performSharing(with option: SharingManager.SharingOption) {
        PandaSlideMenuController.applyDarkCustomization()
        switch type! {
        case .invite:
            invite(with: option)
        case .suggest:
            suggest(with: option)
        }
    }

    func suggest(with option: SharingManager.SharingOption) {
        let currentUser = SPRModelController.instance().user!
        SharingManager().suggest(user: user!, from: currentUser, with: option, presentFrom: self) { error in
            guard SPRErrorManager.handleError(error, title:DPLocalizedString("ERROR_COULD_NOT_SHARE", nil)) == nil else { return }
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func invite(with option: SharingManager.SharingOption) {
        let currentUser = SPRModelController.instance().user!
        SharingManager().invite(from: currentUser, with: option, presentFrom: self) { error in
            guard SPRErrorManager.handleError(error, title:DPLocalizedString("ERROR_COULD_NOT_SHARE", nil)) == nil else { return }
            self.dismiss(animated: true, completion: nil)
        }
    }
}
