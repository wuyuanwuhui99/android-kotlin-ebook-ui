package com.player.ebook.config

class Api {
    companion object {

        const val HOST = "http://192.168.0.103:7001"

        //查询所有大分类
        const val FINDALLBYCLASSIFYGROUP = "/service/ebook/findAllByClassifyGroup"

        //获取banner图片
        const val GETBANNER = "/service/ebook/getBanner"

        //根据大分类查询课程
        const val FINDBOOLIST = "/service/ebook/findBookList"

        //根据类别查询课程
        const val FINDALLBYCATEGORY = "/service/ebook/findAllByCategory"

        //根据大分类查询课程
        const val FINDALLBYCOURSENAME = "/service/ebook/findAllByCourseName"

        //登录校验
        const val LOGIN = "/service/ebook/login"

        //查询用户信息
        const val GETUSERDATA = "/service/ebook/getUserData"

        //保存课程日志信息
        const val SAVECOURSELOG = "/service/ebook/saveCourseLog"

        //保存观看记录
        const val SAVECHAPTERLOG = "/service/ebook/saveChapterLog"
    }
}
