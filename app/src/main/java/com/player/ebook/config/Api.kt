package com.player.ebook.config

object Api {
    open val HOST = "http://192.168.0.102:7000"

    open val PATH = "/service/ebook"

    //查询所有大分类
    val FINDALLBYCLASSIFYGROUP = "$HOST$PATH/findAllByClassifyGroup"

    //获取banner图片
    val GETBANNER =  "$HOST$PATH/getBanner"

    //根据大分类查询课程
    val FINDBOOLIST = "$HOST$PATH/findBookList"

    //根据类别查询课程
    val FINDALLBYCATEGORY = "$HOST$PATH/findAllByCategory"

    //根据大分类查询课程
    val FINDALLBYCOURSENAME = "$HOST$PATH/findAllByCourseName"

    //登录校验
    val LOGIN = "$HOST$PATH/login"

    //查询用户信息
    val GETUSERDATA = "$HOST$PATH/getUserData"

    //保存课程日志信息
    val SAVECOURSELOG = "$HOST$PATH/saveCourseLog"

    //保存观看记录
    val SAVECHAPTERLOG = "$HOST$PATH/saveChapterLog"
}
