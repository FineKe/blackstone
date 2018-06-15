package com.kefan.blackstone.JavaBean;

/**
 * Created by MY SHIP on 2017/6/27.
 */

public class APIManager {

    private static final String BASE_PROD_URL = "https://prod.api.blackstone.ebirdnote.cn/";

    private static final String BASE_DEV_URL ="http://api.blackstone.ebirdnote.cn/";

    public static final String BASE_URL= BASE_DEV_URL;

    public static final String LOGIN_URL = BASE_URL + "v1/user/login";

    public static final String UPLOAD_IMAGE_URL = BASE_URL +"v1/user/avatar";

    public static final String HOME_MAIN_URL= BASE_URL+"v1/main";
}
