package com.player.ebook.config;

public final class API {
    public static final String  HOST = "http://192.168.0.102:7000/";

    public static final String PATH = "/service/ebook";

    //查询所有大分类
    public static final String FINDALLBYCLASSIFYGROUP = PATH + "/findAllByClassifyGroup";

    //获取banner图片
    public static final String GETBANNER =  PATH + "/getBanner";

    //根据大分类查询课程
    public static final String FINDBOOLIST = PATH + "/findBookList";

    //根据类别查询课程
    public static final String FINDALLBYCATEGORY = PATH + "/findAllByCategory";

    //根据大分类查询课程
    public static final String FINDALLBYCOURSENAME = PATH + "/findAllByCourseName";

    //登录校验
    public static final String LOGIN = PATH + "/login";

    //查询用户信息
    public static final String GETUSERDATA = PATH + "/getUserData";

    //保存课程日志信息
    public static final String SAVECOURSELOG = PATH + "/saveCourseLog";

    //保存观看记录
    public static final String SAVECHAPTERLOG = PATH + "/saveChapterLog";
}
