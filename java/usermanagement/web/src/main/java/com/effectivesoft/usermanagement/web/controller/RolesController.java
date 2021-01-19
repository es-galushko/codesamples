package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.*;
import com.effectivesoft.usermanagement.service.ProductService;
import com.effectivesoft.usermanagement.service.RolePermissionService;
import com.effectivesoft.usermanagement.service.RoleService;
import com.effectivesoft.usermanagement.service.UserRoleService;
import com.effectivesoft.usermanagement.web.dto.SortOrder;
import com.effectivesoft.usermanagement.web.form.RoleForm;
import com.effectivesoft.usermanagement.web.form.validator.RoleFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;


@Controller
public class RolesController extends AbstractController {
    private static final String ROLES_MENU_ITEM = "Roles";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String ROLE_FORM_ATTRIBUTE = "roleForm";
    private static final String PERMISSION_OBJECT_TYPES_ATTRIBUTE = "permissionObjectTypes";

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    private Map<String, SortOrder> sortParamSortOrderMap;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleFormValidator roleFormValidator;

    @PostConstruct
    private void init() {
        sortParamSortOrderMap = new HashMap<String, SortOrder>();
        sortParamSortOrderMap.put(SORT_NAME_ASK, new SortOrder(Sort.Direction.ASC, SORT_NAME_PROPERTY));
        sortParamSortOrderMap.put(SORT_NAME_DESC, new SortOrder(Sort.Direction.DESC, SORT_NAME_PROPERTY));
    }

    @RequestMapping(value = ROLES_MAPPING, method = RequestMethod.GET)
    public String goToRoles(Model model,
                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER_VALUE) Integer page,
                            @RequestParam(required = false, defaultValue = SORT_NAME_ASK) String sort) {
        model.addAttribute(CURRENT_PAGE_PARAM, ROLES_MENU_ITEM);
        if (page < DEFAULT_PAGE_NUMBER) {
            page = DEFAULT_PAGE_NUMBER;
        }
        SortOrder sortOrder = sortParamSortOrderMap.get(sort);
        if (sortOrder == null) {
            sortOrder = new SortOrder(Sort.Direction.ASC, SORT_NAME_PROPERTY);
        }
        model.addAttribute(SORT_ORDER_ATTRIBUTE, sortOrder);
        Pageable pageable = new PageRequest(page, DEFAULT_PAGE_SIZE, new Sort(sortOrder.getDirection(), sortOrder.getProperty()));
        Page<Role> rolesPage = roleService.findAll(pageable);
        List<Product> products = getAvailableProducts();
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);

        List<PermissionObjectType> objectTypes = PermissionObjectType.toArrayList();
        model.addAttribute(PERMISSION_OBJECT_TYPES_ATTRIBUTE, objectTypes);

        model.addAttribute(PAGE_ATTRIBUTE, rolesPage);
        return ROLES_VIEW;
    }

    @RequestMapping(value = DELETE_ROLE_MAPPING, method = RequestMethod.GET)
    public String delete(@RequestParam Integer id) {
        Role role = roleService.findOne(id);
        if (role != null) {
            roleService.delete(role);
        }
        return REDIRECT + ROLES_MAPPING;
    }

    @RequestMapping(value = EDIT_ROLE_MAPPING, method = RequestMethod.GET)
    public String edit(Model model, @RequestParam Integer id) {
        model.addAttribute(CURRENT_PAGE_PARAM, ROLES_MENU_ITEM);
        Role role = roleService.findOne(id);
        if (role == null) {
            return REDIRECT + ROLES_MAPPING;
        }
        prepareAddEditPages(model, role, null);
        return EDIT_ROLE_VIEW;
    }

    @RequestMapping(value = ADD_ROLE_MAPPING, method = RequestMethod.GET)
    public String add(Model model) {
        prepareAddEditPages(model, null, null);
        return ADD_ROLE_VIEW;
    }

    @RequestMapping(value = SAVE_ROLE_MAPPING, method = RequestMethod.POST)
    public String save(Model model,
                       @ModelAttribute(ROLE_FORM_ATTRIBUTE) RoleForm roleForm,
                       BindingResult result) {
        if (roleForm == null) {
            return REDIRECT + ROLES_MAPPING;
        }

        List<ObjectError> errors = roleFormValidator.validate(roleForm);
        if (errors.size() > 0) {
            for (ObjectError error : errors) {
                result.addError(error);
            }
        }

        if (result.hasErrors()) {
            prepareAddEditPages(model, null, roleForm);
            return roleForm.getId() == null ? ADD_ROLE_VIEW : EDIT_ROLE_VIEW;
        }

        Role role = createRole(roleForm);
        roleService.save(role);

        return REDIRECT + ROLES_MAPPING;
    }

    private Role createRole(RoleForm roleForm){
        Role role = null;
        Calendar calendar = Calendar.getInstance();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (roleForm.getId() != null) {
            role = roleService.findOne(roleForm.getId());
        }
        if (role == null) {
            role = new Role();
            role.setCreatedAt(calendar.getTime());
            role.setCreatedBy(username);
        }

        role.setName(roleForm.getName());
        role.setUpdatedAt(calendar.getTime());
        role.setUpdatedBy(username);

        role.setRolePermissions(new ArrayList<RolePermission>());

        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.MANAGEDSERVER,
                role, roleForm.getOwls12cManagedServerCanRead(), roleForm.getOwls12cManagedServerCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.DATASOURCE,
                role, roleForm.getOwls12cDatasourceCanRead(), roleForm.getOwls12cDatasourceCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.JMSSERVER,
                role, roleForm.getOwls12cJmsServerCanRead(), roleForm.getOwls12cJmsServerCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.JMSMODULE,
                role, roleForm.getOwls12cJmsModuleCanRead(), roleForm.getOwls12cJmsModuleCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.MACHINE,
                role, roleForm.getOwls12cMachineCanRead(), roleForm.getOwls12cMachineCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.CLUSTER,
                role, roleForm.getOwls12cClusterCanRead(), roleForm.getOwls12cClusterCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.LOG,
                role, roleForm.getOwls12cLogCanRead(), roleForm.getOwls12cLogCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_12_C, PermissionObjectType.REPORT,
                role, roleForm.getOwls12cReportCanRead(), roleForm.getOwls12cReportCanExecute());

        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.MANAGEDSERVER,
                role, roleForm.getOwls11gManagedServerCanRead(), roleForm.getOwls11gManagedServerCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.DATASOURCE,
                role, roleForm.getOwls11gDatasourceCanRead(), roleForm.getOwls11gDatasourceCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.JMSSERVER,
                role, roleForm.getOwls11gJmsServerCanRead(), roleForm.getOwls11gJmsServerCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.JMSMODULE,
                role, roleForm.getOwls11gJmsModuleCanRead(), roleForm.getOwls11gJmsModuleCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.MACHINE,
                role, roleForm.getOwls11gMachineCanRead(), roleForm.getOwls11gMachineCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.CLUSTER,
                role, roleForm.getOwls11gClusterCanRead(), roleForm.getOwls11gClusterCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.LOG,
                role, roleForm.getOwls11gLogCanRead(), roleForm.getOwls11gLogCanExecute());
        createOrUpdatePermissions(ProductName.ORACLE_WEBLOGIC_SERVER_11_G, PermissionObjectType.REPORT,
                role, roleForm.getOwls11gReportCanRead(), roleForm.getOwls11gReportCanExecute());

        return role;
    }

    private void prepareAddEditPages(Model model, Role role, RoleForm roleForm){
        model.addAttribute(CURRENT_PAGE_PARAM, ROLES_MENU_ITEM);

        List<Product> products = getAvailableProducts();
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);

        if(roleForm == null){
            roleForm = new RoleForm();
            if (role != null){
                fillRoleForm(roleForm, role);
            }
        }
        model.addAttribute(ROLE_FORM_ATTRIBUTE, roleForm);
    }

    private void createOrUpdatePermissions(ProductName productName, PermissionObjectType objectType, Role role,
                                           Boolean canRead, Boolean canExecute) {
        List<RolePermission> result = new ArrayList<RolePermission>();
        Calendar calendar = Calendar.getInstance();
        Product product = productService.findByName(productName.getName());
        List<RolePermission> rolePermissions = rolePermissionService.findByRoleProductObjectType(role, product,
                objectType);
        if (rolePermissions != null && rolePermissions.size() > 0) {
            for (RolePermission rolePermission : rolePermissions) {
                if (!(rolePermission.getCanRead().equals(canRead) && rolePermission.getCanExecute().equals(canExecute))) {
                    rolePermission.setCanRead(canRead);
                    rolePermission.setCanExecute(canExecute);
                    rolePermission.setUpdatedAt(calendar.getTime());
                }
                result.add(rolePermission);
            }
        } else {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setProduct(product);
            rolePermission.setObjectType(objectType.getName());

            //TODO - remove for new database version
            rolePermission.setObjectId(0);

            rolePermission.setCanRead(canRead);
            rolePermission.setCanExecute(canExecute);
            rolePermission.setCreatedAt(calendar.getTime());
            rolePermission.setUpdatedAt(calendar.getTime());
            result.add(rolePermission);
        }
        role.getRolePermissions().addAll(result);
    }

    private void fillRoleForm(RoleForm roleForm, Role role) {
        roleForm.setId(role.getId());
        roleForm.setName(role.getName());
        for (RolePermission rolePermission : role.getRolePermissions()) {
            fillRoleFormPermissions(roleForm, rolePermission);
        }
    }

    private void fillRoleFormPermissions(RoleForm roleForm, RolePermission rolePermission) {
        String rolePermissionProductName = rolePermission.getProduct().getName();
        String rolePermissionObjectType = rolePermission.getObjectType();
        Boolean canRead = rolePermission.getCanRead();
        Boolean canExecute = rolePermission.getCanExecute();

        ProductName productName = ProductName.getByName(rolePermissionProductName);
        PermissionObjectType objectType = PermissionObjectType.getByName(rolePermissionObjectType);

        if (productName != null && objectType != null) {
            switch (productName) {
                case ORACLE_WEBLOGIC_SERVER_12_C:
                    fillOracleWeblogicServer12cPermissions(roleForm, objectType, canRead, canExecute);
                    break;
                case ORACLE_WEBLOGIC_SERVER_11_G:
                    fillOracleWeblogicServer11gPermissions(roleForm, objectType, canRead, canExecute);
                    break;
            }
        }

    }

    private void fillOracleWeblogicServer12cPermissions(RoleForm roleForm, PermissionObjectType objectType,
                                                        Boolean canRead, Boolean canExecute) {
        switch (objectType) {
            case MANAGEDSERVER:
                roleForm.setOwls12cManagedServerCanRead(canRead);
                roleForm.setOwls12cManagedServerCanExecute(canExecute);
                break;
            case DATASOURCE:
                roleForm.setOwls12cDatasourceCanRead(canRead);
                roleForm.setOwls12cDatasourceCanExecute(canExecute);
                break;
            case JMSSERVER:
                roleForm.setOwls12cJmsServerCanRead(canRead);
                roleForm.setOwls12cJmsServerCanExecute(canExecute);
                break;
            case JMSMODULE:
                roleForm.setOwls12cJmsModuleCanRead(canRead);
                roleForm.setOwls12cJmsModuleCanExecute(canExecute);
                break;
            case MACHINE:
                roleForm.setOwls12cMachineCanRead(canRead);
                roleForm.setOwls12cMachineCanExecute(canExecute);
                break;
            case CLUSTER:
                roleForm.setOwls12cClusterCanRead(canRead);
                roleForm.setOwls12cClusterCanExecute(canExecute);
                break;
            case LOG:
                roleForm.setOwls12cLogCanRead(canRead);
                roleForm.setOwls12cLogCanExecute(canExecute);
                break;
            case REPORT:
                roleForm.setOwls12cReportCanRead(canRead);
                roleForm.setOwls12cReportCanExecute(canExecute);
                break;
        }
    }

    private void fillOracleWeblogicServer11gPermissions(RoleForm roleForm, PermissionObjectType objectType,
                                                        Boolean canRead, Boolean canExecute) {
        switch (objectType) {
            case MANAGEDSERVER:
                roleForm.setOwls11gManagedServerCanRead(canRead);
                roleForm.setOwls11gManagedServerCanExecute(canExecute);
                break;
            case DATASOURCE:
                roleForm.setOwls11gDatasourceCanRead(canRead);
                roleForm.setOwls11gDatasourceCanExecute(canExecute);
                break;
            case JMSSERVER:
                roleForm.setOwls11gJmsServerCanRead(canRead);
                roleForm.setOwls11gJmsServerCanExecute(canExecute);
                break;
            case JMSMODULE:
                roleForm.setOwls11gJmsModuleCanRead(canRead);
                roleForm.setOwls11gJmsModuleCanExecute(canExecute);
                break;
            case MACHINE:
                roleForm.setOwls11gMachineCanRead(canRead);
                roleForm.setOwls11gMachineCanExecute(canExecute);
                break;
            case CLUSTER:
                roleForm.setOwls11gClusterCanRead(canRead);
                roleForm.setOwls11gClusterCanExecute(canExecute);
                break;
            case LOG:
                roleForm.setOwls11gLogCanRead(canRead);
                roleForm.setOwls11gLogCanExecute(canExecute);
                break;
            case REPORT:
                roleForm.setOwls11gReportCanRead(canRead);
                roleForm.setOwls11gReportCanExecute(canExecute);
                break;
        }
    }
}
