package kr.ssu.ai_fitness.url;

public class URLs {

    private static final String ROOT_URL = "https://ai-fitness-369.an.r.appspot.com";//*****올바른 url로 수정해야함

    public static final String URL_SIGNUP = ROOT_URL + "/member/create_signup";
    public static final String URL_LOGIN= ROOT_URL + "/member/read_login";
    public static final String URL_VIDEO= ROOT_URL + "/video/tr_video_read";
    public static final String URL_HOMEDATA= ROOT_URL + "/member/read_home_data";
    public static final String URL_CHATLIST= ROOT_URL + "/member/read_chat_list";
    public static final String URL_DAYPROGRAM= ROOT_URL + "/day_exr/read_day_program";
    public static final String URL_ADMINUSERMANAGE = ROOT_URL + "/member/readAdminUserManage";
    public static final String URL_DELETEADMINUSERMANAGE = ROOT_URL + "/member/deleteAdminUserManage";
    public static final String URL_READTRAINERLIST = ROOT_URL + "/member/readTrainerList";
    public static final String URL_SETALARM = ROOT_URL + "/member/setAlarm";
    public static final String URL_SETPROFILE = ROOT_URL + "/member/setProfile";
    public static final String URL_SETPWD = ROOT_URL + "/member/setPwd";
    public static final String URL_READTRAINERDATA = ROOT_URL + "/member/readTrainerData";

}