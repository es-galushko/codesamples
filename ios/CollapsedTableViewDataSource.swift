//
//  CollapsedTableViewDataSource.swift
//  EffectiveSoft
//
//  Created by Aleksey Bakhtin on 2/26/18.
//  Copyright © 2018 EffectiveSoft. All rights reserved.
//

import Foundation
import DPDataStorage

struct DataSourceSection {
    let sectionInfo: NSFetchedResultsSectionInfo
    let opened: Bool
    let index: Int
}

protocol DataSourceSectionHeaderFooterView {
    func configure(with section: DataSourceSection)
    
    // Autolayout does not work correctly for headers and footers in iOS 10. So handle it manually
    func height(for section: DataSourceSection, in tableView: UITableView) -> CGFloat
}

class CollapsedTableViewDataSource: DPTableViewDataSource {

    // MARK: Public interface

    @IBInspectable var sectionHeaderNibName: String?
    @IBInspectable var sectionFooterNibName: String?
    @IBInspectable var showUnnamedSectionsHeaders: Bool = false
    @IBInspectable var allSectionsInitialyOpened: Bool = false

    let sectionHeaderReuseIdentifier = "DataSourceHeaderViewReuseIdentifier"
    let sectionFooterReuseIdentifier = "DataSourceFooterViewReuseIdentifier"

    func make(section: Int, opened: Bool, animated: Bool = true) {
        if  openedSections.contains(section) && opened {
            return
        }
        
        var sectionsToReload = Set<Int>()
        sectionsToReload.insert(section)
        namedSectionsToClose().forEach {
            openedSections.remove($0)
            sectionsToReload.insert($0)
        }

        if opened {
            openedSections.insert(section)
        } else {
            openedSections.remove(section)
        }
        
        let animation: UITableViewRowAnimation = animated ? .automatic : .none
        tableView?.reloadSections(IndexSet(sectionsToReload), with: animation)
        if opened && animated {
            tableView?.scrollToRow(at: IndexPath(row: NSNotFound, section: section), at: .top, animated: true)
        }
    }

    func makeAllSectionsClosed() {
        let sectionsToReload = self.openedSections
        sectionsToReload.forEach { self.openedSections.remove($0) }
        tableView?.reloadSections(IndexSet(sectionsToReload), with: .none)
    }
    
    func isSectionNamed(at index: Int) -> Bool {
        guard let listController = listController else { return false }
        return !listController.sections[index].name.isEmpty
    }
    
    func isSectionOpened(at index: Int) -> Bool {
        return openedSections.contains(index)
    }

    // MARK: Section headers handling

    override var tableView: UITableView? {
        didSet {
            if let sectionHeaderNibName = sectionHeaderNibName {
                let headerNib = UINib(nibName: sectionHeaderNibName, bundle: nil)
                tableView?.register(headerNib, forHeaderFooterViewReuseIdentifier: sectionHeaderReuseIdentifier)
            }
            if let sectionFooterNibName = sectionFooterNibName {
                let footerNib = UINib(nibName: sectionFooterNibName, bundle: nil)
                tableView?.register(footerNib, forHeaderFooterViewReuseIdentifier: sectionFooterReuseIdentifier)
            }
        }
    }
    
    override func numberOfSections(in tableView: UITableView?) -> Int {
        return super.numberOfSections(in: tableView)
    }

    // MARK: Header processing
    
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if let headerView = dequeueHeaderView(tableView, for: section), let dataSourceSection = dataSourceSection(for: section) {
            headerView.configure(with: dataSourceSection)
            return headerView as? UIView
        }
        return nil
    }

    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if let headerView = dequeueHeaderView(tableView, for: section), let dataSourceSection = dataSourceSection(for: section) {
            return headerView.height(for: dataSourceSection, in: tableView)
        }
        return 0.0
    }

    func dequeueHeaderView(_ tableView: UITableView, for section: Int) -> DataSourceSectionHeaderFooterView? {
        if !showUnnamedSectionsHeaders,
            !isSectionNamed(at: section) {
            return nil
        }
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: sectionHeaderReuseIdentifier)
        if let headerView = headerView as? DataSourceSectionHeaderFooterView {
            return headerView
        }
        return nil
    }

    
    // MARK: Footer processing
    
    override func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        if let footerView = dequeueFooterView(tableView, for: section), let dataSourceSection = dataSourceSection(for: section) {
            footerView.configure(with: dataSourceSection)
            return footerView as? UIView
        }
        return nil
    }
    
    override func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        if let footerView = dequeueFooterView(tableView, for: section), let dataSourceSection = dataSourceSection(for: section) {
            return footerView.height(for: dataSourceSection, in: tableView)
        }
        return 0.0
    }

    func dequeueFooterView(_ tableView: UITableView, for section: Int) -> DataSourceSectionHeaderFooterView? {
        guard let sectionFooterNibName = sectionFooterNibName,
            !sectionFooterNibName.isEmpty,
            isSectionOpened(at: section) else {
            return nil
        }
        let footerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: sectionFooterReuseIdentifier)
        if let footerView = footerView as? DataSourceSectionHeaderFooterView {
            return footerView
        }
        return nil
    }

    func dataSourceSection(for index: Int) -> DataSourceSection? {
        guard let listController = listController else { return nil }
        return DataSourceSection(sectionInfo: listController.sections[index], opened: isSectionOpened(at: index), index: index)
    }
    
    // MARK: Sections closing implementing

    private var openedSections: Set<Int> = []

    override func tableView(_ tableView: UITableView?, numberOfRowsInSection section: Int) -> Int {
        guard isSectionOpened(at: section) else {
            return 0
        }
        return super.tableView(tableView, numberOfRowsInSection: section)
    }
    
    override var listController: DataSourceContainerController? {
        didSet {
            if allSectionsInitialyOpened {
                openAllSections()
            }
            if !showUnnamedSectionsHeaders {
                openUnnamedSections()
            }
        }
    }
    
    private func openAllSections() {
        guard let listController = listController else { return }
        for i in 0...listController.numberOfSections() {
            openedSections.insert(i)
        }
    }

    private func openUnnamedSections() {
        unnamedSectionsIndexes().forEach {
            openedSections.insert($0)
        }
    }

    private func namedSectionsToClose() -> [Int] {
        return openedNamedSectionsIndexes()
    }
    
    private func unnamedSectionsIndexes() -> [Int] {
        guard let listController = listController, listController.numberOfSections() > 0 else { return [] }
        
        let allSections = [Int](0...listController.numberOfSections() - 1)
        return allSections.filter({
            return !isSectionNamed(at: $0)
        })
    }
    
    private func openedNamedSectionsIndexes() -> [Int] {
        return openedSections.filter({
            return isSectionNamed(at: $0)
        })
    }
    
    // MARK: NSFetchedResultsController overriding
    
    override func controller(_ controller: DataSourceContainerController, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        guard let newIndexPath = newIndexPath,
            isSectionOpened(at: newIndexPath.section) else {
            return
        }
        
        super.controller(controller, didChange: anObject, at: indexPath, for: type, newIndexPath: newIndexPath)
    }
}
