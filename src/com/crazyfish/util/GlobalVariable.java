package com.crazyfish.util;

public class GlobalVariable {
	public static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int REQUEST_CODE = 1;
	public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final int RESULT_CODE = 1;
	public static final int SELECT_PIC = 2;
	public static final int GAG_PAGE_SIZE = 3;
	public static final String FILE_CACHE_LOCATION = "sdcard/aqi";
	public static final String URLHEAD = "http://192.168.1.4:8080/ssm_demo";
    //what value
	public static final int HANDLER_GET_CODE = 1;
    public static final int HANDLER_GOOD_CODE = 2;
    public static final int HANDLER_COLLECTION_CODE = 3;
    public static final int HANDLER_REC_CODE = 4;

    //filter type
    public static final int FILTER_ALL_GAG = 1;
    public static final int FILTER_PURE_TEXT = 2;
    public static final int FILTER_HAVE_PIC = 3;
    public static final int FILTER_SCHOOL = 4;
    public static final int FILTER_SELECTED = 5;

    //pic type
    public static final int PIC_USER_HEAD = 1;
    public static final int PIC_USER_UPLOAD = 2;
    public static final String  LOGINURL = "http://192.168.1.4:8080/ssm_demo/customer/1.0/" +
            "customerLogin";
}
