//
//  UIButtton+Background.swift
//  SafePin
//
//  Created by Egor Veremeychik on 1/11/18.
//  Copyright © 2018 EffectiveSoft. All rights reserved.
//

import UIKit

extension UIButton {
    
    @IBInspectable
    var normalBackgroundColor: UIColor? {
        set {
            setBackgroundColor(color: newValue, forUIControlState: .normal)
        }
        get {
            return nil
        }
    }

    @IBInspectable
    var highlightedBackgroundColor: UIColor? {
        set {
            let newColor:UIColor? = newValue?.withAlphaComponent(0.5)
            setBackgroundColor(color: newColor, forUIControlState: .highlighted)
        }
        get {
            return nil
        }
    }

    func setBackgroundColor(color: UIColor?, forUIControlState state: UIControlState) {
        guard let color = color else {
            setBackgroundImage(nil, for: state)
            return
        }
        self.setBackgroundImage(image(withColor: color), for: state)
    }

    func image(withColor color: UIColor) -> UIImage {
        let rect = CGRect(x: 0, y: 0, width: 1, height: 1)
        UIGraphicsBeginImageContext(rect.size)
        let context = UIGraphicsGetCurrentContext()!
        context.setFillColor(color.cgColor)
        context.fill(rect)
        let image = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return image
    }
}
