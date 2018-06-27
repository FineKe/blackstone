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

    public static final String COLLECTION_URL = BASE_URL + "v1/species/collection/";

    public static final String SPECIES_LIST_URL = BASE_URL+ "v1/species/list";

    public static final String UPLOAD_RECORD = BASE_URL+"v1/record/new";

    public static final String DELETE_RECORD = BASE_URL+"v1/record/";

    public static final String PULL_RECORDS = BASE_URL+"v1/record/user/";

    public static final String ALTER_RECORD = BASE_URL+"v1/record/edit";

    public static final String NETX_QUESTION = BASE_URL+"v1/game/nextQ";

    public static final String GAME_SUBMIT = BASE_URL+"v1/game/submit";

    public static final String RANK_LIST = BASE_URL+"v1/game/leaderBoard";

    public static final String RECORD_DETAILED = BASE_URL+"v1/record/";
}
