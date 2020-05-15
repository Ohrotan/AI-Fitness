package kr.ssu.ai_fitness.url;

public class URLs {

    private static final String ROOT_URL = "https://ai-fitness-369.an.r.appspot.com";//*****올바른 url로 수정해야함

    public static final String URL_SIGNUP = ROOT_URL + "/member/create_signup";
    public static final String URL_LOGIN = ROOT_URL + "/member/read_login";
    public static final String URL_HOMEDATA = ROOT_URL + "/member/read_home_data";
    public static final String URL_CHATLIST = ROOT_URL + "/member/read_chat_list";

    public static final String URL_ADMINUSERMANAGE = ROOT_URL + "/member/read_admin_user_manage";
    public static final String URL_DELETEADMINUSERMANAGE = ROOT_URL + "/member/delete_admin_user_manage";
    public static final String URL_READTRAINERLIST = ROOT_URL + "/member/read_trainer_list";
    public static final String URL_SETALARM = ROOT_URL + "/member/set_alarm";
    public static final String URL_SETPROFILE = ROOT_URL + "/member/set_profile";
    public static final String URL_SETPWD = ROOT_URL + "/member/set_pwd";
    public static final String URL_READTRAINERDATA = ROOT_URL + "/member/read_trainer_data";
    public static final String URL_READTRAINERPROGRAM = ROOT_URL + "/member/read_trainer_program";
    public static final String URL_READTRAINERPROGRAMNUM = ROOT_URL + "/member/read_trainer_program_num";

    public static final String URL_EXR_CREATE = ROOT_URL + "/exr/create";

    public static final String URL_DAY_EXR_CREATE = ROOT_URL + "/day_exr/create";
    public static final String URL_DAYPROGRAM = ROOT_URL + "/day_exr/read_day_program";
    public static final String URL_DAYPROGRAMAFTER = ROOT_URL + "/day_exr/read_day_program_after";
    public static final String URL_UPDATEMEMBERHISTORYFEEDBACK = ROOT_URL + "/day_exr/update_member_history_feedback";

    public static final String URL_DAY_VIDEO_CREATE = ROOT_URL + "/day_video/create";

    public static final String URL_VIDEO = ROOT_URL + "/video/tr_video_read";
}