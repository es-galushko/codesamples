//
//  SharingManager.swift
//  EffectiveSoft
//
//  Created by Alex Bakhtin on 7/24/17.
//
//

import UIKit
import Branch
import DPLocalization
import MessageUI
import Social
import FBSDKShareKit

class SharingManager: NSObject {

    // MARK: - Public

    typealias SharingCompletion = (_ error: Error?) -> Void

    enum SharingOption {
        case mail
        case messaging
        case messenger
        case all
    }

    func suggest(user: SPRUser,
                 from inviter: SPRUser,
                 with option: SharingOption,
                 presentFrom viewController: UIViewController,
                 completion: @escaping SharingCompletion) {

        let branchObject = BranchUniversalObject(canonicalIdentifier: NSUUID().uuidString)

        var title: String?
        var description: String?
        if let name = user.name {
            switch option {
            case .mail:
                title = String(format: DPLocalizedString("INVITE_LINK_TITLE_T", nil), arguments: [name])
                description = "You should check out \(name)'s profile on Ponder. I think they'd be a good match for you!"
            case .messaging:
                description = "Check out \(name)'s profile on Ponder."
            case .messenger:
                title = "Do you like \(name)?"
                description = "Check out their profile on Ponder"
            case .all:
                break
            }
        }

        let inviterAccountID = inviter.accountID!
        let suggestedAccountID = user.accountID!
        let deviceToken = SPRDataController.instance().deviceToken!
        branchObject.addMetadataKey("METADATA_CURRENT_USER_ID_KEY", value: inviterAccountID.stringValue)
        branchObject.addMetadataKey("METADATA_SUGGESTED_USER_ID_KEY", value: suggestedAccountID.stringValue)
        branchObject.addMetadataKey("METADATA_DEVICE_TOKEN_KEY", value: deviceToken)
        branchObject.title = title
        branchObject.contentDescription = description
        branchObject.imageUrl = user.primaryImage.imageURL

        let linkProperties = BranchLinkProperties()
        linkProperties.feature = "suggest"
        linkProperties.channel = "branchIO"

        share(object: branchObject, withProperties: linkProperties, with: option, presentFrom: viewController, completion: completion)
    }

    func invite(from inviter: SPRUser,
                with option: SharingOption,
                presentFrom viewController: UIViewController,
                completion: @escaping SharingCompletion) {

        let branchObject = BranchUniversalObject(canonicalIdentifier: NSUUID().uuidString)

        var title: String?
        var description: String?
        switch option {
        case .mail:
            title = "Have you heard of Ponder?"
            description = "It's an app for everyone to play matchmaker. You win real money making successful matches for others. Singles can get matched up by friends and other matchmakers in the community. It's awesome."
        case .messaging:
            description = "Check out Ponder! Play matchmaker and win money."
        case .messenger:
            title = "Have you Pondered recently?"
            description = "Play matchmaker, win money, or be matched"
        case .all:
            break
        }

        branchObject.addMetadataKey("METADATA_CURRENT_USER_ID_KEY", value: inviter.userID!.stringValue)
        if let invtersName = inviter.fullName {
            branchObject.addMetadataKey("METADATA_CURRENT_USER_FIRST_NAME_KEY", value: invtersName)
        }
        if let invitersImageURL = inviter.primaryImage.imageURL {
            branchObject.addMetadataKey("METADATA_CURRENT_USER_PHOTO_KEY", value: invitersImageURL)
        }
        branchObject.title = title
        branchObject.contentDescription = description

        let linkProperties = BranchLinkProperties()
        linkProperties.feature = "invite"
        linkProperties.channel = "branchIO"

        share(object: branchObject, withProperties: linkProperties, with: option, presentFrom: viewController, completion: completion)
    }

    // MARK: - Sharing implementation

    private func share(object: BranchUniversalObject,
                       withProperties properties: BranchLinkProperties,
                       with option: SharingOption,
                       presentFrom viewController: UIViewController,
                       completion: @escaping SharingCompletion) {

        self.completion = { error in _ = self
            completion(error)
        }
        switch option {
        case .mail: presentMailComposer(for: object, withProperties: properties, presentFrom: viewController)
        case .messaging: presentMessageComposer(for: object, withProperties: properties, presentFrom: viewController)
        case .messenger: presentMessengerComposer(for: object, withProperties: properties, presentFrom: viewController)
        case .all: presentActivityController(for: object, withProperties: properties, presentFrom: viewController)
        }
    }

    var completion: SharingCompletion?

    private func presentMailComposer(for object: BranchUniversalObject,
                                     withProperties properties: BranchLinkProperties,
                                     presentFrom viewController: UIViewController) {
        guard MFMailComposeViewController.canSendMail() else {
            completion?(NSError.applicationError(with: .accountNotAvailble))
            return
        }

        let composeViewController = MFMailComposeViewController()
        composeViewController.mailComposeDelegate = self
        if let title = object.title {
            composeViewController.setSubject(title)
        }
        if let contentDescription = object.contentDescription,
            let shortUrl = object.getShortUrl(with: properties) {
            let messageBody = "\(contentDescription)\n\(shortUrl)"
            composeViewController.setMessageBody(messageBody, isHTML: false)
        }
        viewController.present(composeViewController, animated: true, completion: nil)
    }

    private func presentMessageComposer(for object: BranchUniversalObject,
                                        withProperties properties: BranchLinkProperties,
                                        presentFrom viewController: UIViewController) {
        guard MFMessageComposeViewController.canSendText() else {
            completion?(NSError.applicationError(with: .accountNotAvailble))
            return
        }

        let composeViewController = MFMessageComposeViewController()
        composeViewController.messageComposeDelegate = self
        if MFMessageComposeViewController.canSendSubject(),
            let title = object.title {
            composeViewController.subject = title
        }
        if let contentDescription = object.contentDescription,
            let shortUrl = object.getShortUrl(with: properties) {
            let messageBody = "\(contentDescription)\n\(shortUrl)"
            composeViewController.body = messageBody
        }
        viewController.present(composeViewController, animated: true, completion: nil)
    }

    private func presentMessengerComposer(for object: BranchUniversalObject,
                                          withProperties properties: BranchLinkProperties,
                                          presentFrom viewController: UIViewController) {
        
        guard let urlString = object.getShortUrl(with: properties) else { return }
        guard let url = URL(string: urlString) else { return }
        
        let content = FBSDKShareLinkContent()
        content.contentTitle = object.title
        content.contentDescription = object.contentDescription
        content.contentURL = url
        FBSDKMessageDialog.show(with: content, delegate: self)
    }

    private func presentActivityController(for object: BranchUniversalObject,
                                           withProperties properties: BranchLinkProperties,
                                           presentFrom viewController: UIViewController) {
        let shareLink = BranchShareLink(universalObject: object, linkProperties: properties)!
        shareLink.presentActivityViewController(from: viewController, anchor: nil)
        shareLink.delegate = self
    }
}

extension SharingManager: BranchShareLinkDelegate {
    func branchShareLinkWillShare(_ shareLink: BranchShareLink) {
        shareLink.shareText = shareLink.universalObject.contentDescription
    }

    func branchShareLink(_: BranchShareLink, didComplete _: Bool, withError error: Error?) {
        completion?(error)
        completion = nil
    }
}

extension SharingManager: MFMailComposeViewControllerDelegate {
    public func mailComposeController(_: MFMailComposeViewController, didFinishWith _: MFMailComposeResult, error: Error?) {
        completion?(error)
        completion = nil
    }
}

extension SharingManager: MFMessageComposeViewControllerDelegate {
    func messageComposeViewController(_: MFMessageComposeViewController, didFinishWith _: MessageComposeResult) {
        completion?(nil)
        completion = nil
    }
}

extension SharingManager: FBSDKSharingDelegate {
    
    public func sharer(_ sharer: FBSDKSharing!, didCompleteWithResults results: [AnyHashable : Any]!) {
        completion?(nil)
        completion = nil
    }

    public func sharer(_ sharer: FBSDKSharing!, didFailWithError error: Error!) {
        completion?(NSError.applicationError(with: .accountNotAvailble))
        completion = nil
    }

    public func sharerDidCancel(_ sharer: FBSDKSharing!) {
        completion?(nil)
        completion = nil
    }

}
