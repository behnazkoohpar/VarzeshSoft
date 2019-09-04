package com.koohpar.eram.api;

import com.koohpar.eram.tools.AppConstants;

import static com.koohpar.eram.tools.AppConstants.SERVER_IP;

/**
 * Created by Behnaz on 06/03/2017.
 */
public interface IApiUrls {
//    String SERVER_IP = "http://192.168.1.52:4000/";
//    String SERVER_IP = "http://192.168.43.39:3000/";
    String URL_SIGN_OUT = "/sign_out";
    String URL_GET_LAST_VERSION =  "/get_last_version";

    String URL_CHECK_ACTIVATION_CODE = "/check_activation_code";
    String URL_FORGET_PASSWORD = "/forget_password";
    String URL_SET_PASSWORD = "/set_password";
    String URL_CHANGE_PASSWORD = "/change_password";
    String URL_LOGIN_VARZESH_SOFT =  "/login_varzeshsoft";
    String URL_SEND_TOKEN = "/set_token";
    String URL_SEND_TOKEN_WITH_DEVICE_TYPE = "/set_token_with_device_type";
    String URL_SET_IMAGE_USER= "/set_image_user";
    String URL_GET_ALL_SERVICES= "/get_all_services";
    String URL_GET_MAIN_SERVICE_DETAILS= "/get_main_service_detailes_service";
    String URL_GET_SUB_SERVICE_DETAILS= "/get_sub_service_detailes_service";
    String URL_GET_CREDIT_SERVICE_DETAILS= "/get_credit_service_detailes_service";
    String URL_GET_FACTOR= "/get_factor";
    String URL_GET_TRANSACTION= "/get_transaction";
    String URL_SET_EVALUATE_RATE= "/set_evaluate_rate";
    String URL_GET_MESSAGES= "/get_messages";
    String URL_GET_MESSAGE_TYPE= "/get_type_message";
    String URL_SEND_TO_ADMIN_MESSAGE= "/send_to_admin_message";
    String URL_GET_ALL_MESSAGE_FROM_ADMIN= "/get_all_message_from_admin";
    String URL_SEEN_MESSAGE= "/seen_message";
    String URL_GET_DETAILS_FACTOR_LIST= "/get_factor_detail";
    String URL_GET_COUNT_MESSAGE_FROM_ADMIN= "/get_count_message";
    String URL_SET_NEW_GUEST= "/set_new_guest";
    String URL_GET_CREDIT_AMOUNT_INFO= "/get_credit_amount_info";
    String URL_SET_PACKAGE_REGISTRATION= "/set_package_registration";
    String URL_GET_ALL_SALABLE_SERVICS= "/get_all_salable_services";
    String URL_GET_ORGANIZATION_UNIT= "/get_organization_unit";
    String URL_GET_RESERVE= "/get_all_reservable_services";
    String URL_GET_SABT_RESERVE= "/set_reserve_request";
    String URL_GET_FORMULA_FRACTION_OF_CHARGE= "/get_formula_fraction_of_charge";

}
