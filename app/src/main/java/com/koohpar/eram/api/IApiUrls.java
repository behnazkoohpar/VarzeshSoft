package com.koohpar.eram.api;

import com.koohpar.eram.tools.AppConstants;

import static com.koohpar.eram.tools.AppConstants.SERVER_IP;

/**
 * Created by Behnaz on 06/03/2017.
 */
public interface IApiUrls {

     String SERVER = "http://5.202.192.146:4000/";
//    String SERVER_IP = "http://192.168.1.52:4000/";
//    String SERVER_IP = "http://192.168.43.39:3000/";
    String URL_SIGN_OUT = SERVER_IP + "/sign_out";
    String URL_GET_LAST_VERSION = SERVER + "get_last_version";

    String URL_CHECK_ACTIVATION_CODE = SERVER_IP + "/check_activation_code";
    String URL_FORGET_PASSWORD = SERVER_IP + "/forget_password";
    String URL_SET_PASSWORD = SERVER_IP + "/set_password";
    String URL_CHANGE_PASSWORD = SERVER_IP + "/change_password";
    String URL_LOGIN_VARZESH_SOFT = SERVER_IP + "/login_varzeshsoft";
    String URL_SEND_TOKEN = SERVER_IP + "/set_token";
    String URL_SEND_TOKEN_WITH_DEVICE_TYPE = SERVER_IP + "/set_token_with_device_type";
    String URL_SET_IMAGE_USER= SERVER_IP + "/set_image_user";
    String URL_GET_ALL_SERVICES= SERVER_IP + "/get_all_services";
    String URL_GET_MAIN_SERVICE_DETAILS= SERVER_IP + "/get_main_service_detailes_service";
    String URL_GET_SUB_SERVICE_DETAILS= SERVER_IP + "/get_sub_service_detailes_service";
    String URL_GET_CREDIT_SERVICE_DETAILS= SERVER_IP + "/get_credit_service_detailes_service";
    String URL_GET_FACTOR= SERVER_IP + "/get_factor";
    String URL_GET_TRANSACTION= SERVER_IP + "/get_transaction";
    String URL_SET_EVALUATE_RATE= SERVER_IP + "/set_evaluate_rate";
    String URL_GET_MESSAGES= SERVER_IP + "/get_messages";
    String URL_GET_MESSAGE_TYPE= SERVER_IP + "/get_type_message";
    String URL_SEND_TO_ADMIN_MESSAGE= SERVER_IP + "/send_to_admin_message";
    String URL_GET_ALL_MESSAGE_FROM_ADMIN= SERVER_IP + "/get_all_message_from_admin";
    String URL_SEEN_MESSAGE= SERVER_IP + "/seen_message";
    String URL_GET_DETAILS_FACTOR_LIST= SERVER_IP + "/get_factor_detail";
    String URL_GET_COUNT_MESSAGE_FROM_ADMIN= SERVER_IP + "/get_count_message";
    String URL_SET_NEW_GUEST= SERVER_IP + "/set_new_guest";
    String URL_GET_CREDIT_AMOUNT_INFO= SERVER_IP + "/get_credit_amount_info";
    String URL_SET_PACKAGE_REGISTRATION= SERVER_IP + "/set_package_registration";
    String URL_GET_ALL_SALABLE_SERVICS= SERVER_IP + "/get_all_salable_services";
    String URL_GET_ORGANIZATION_UNIT= SERVER_IP + "/get_organization_unit";
    String URL_GET_RESERVE= SERVER_IP + "/get_all_reservable_services";
    String URL_GET_SABT_RESERVE= SERVER_IP + "/set_reserve_request";
    String URL_GET_FORMULA_FRACTION_OF_CHARGE= SERVER_IP + "/get_formula_fraction_of_charge";

}
