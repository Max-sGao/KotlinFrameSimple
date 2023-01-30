package com.mine.common.repository.bean.server

import com.mine.common.repository.bean.IBean

class ArticleBean : IBean {

    val curPage: Int? = null
    val datas: List<SingleArticle>? = null
    val offset: Int? = null
    val over = false
    val pageCount: Int? = null
    val size: Int? = null
    val total: Int? = null
}

class SingleArticle : IBean {
    private val adminAdd = false
    private val apkLink: String? = null
    private val audit = 0
    private val author: String? = null
    private val canEdit = false
    private val chapterId = 0
    private val chapterName: String? = null
    private val collect = false
    private val courseId = 0
    private val desc: String? = null
    private val descMd: String? = null
    private val envelopePic: String? = null
    private val fresh = false
    private val host: String? = null
    private val id = 0
    private val isAdminAdd = false
    private val link: String? = null
    private val niceDate: Long? = null
    private val niceShareDate: Long? = null
    private val origin: String? = null
    private val prefix: String? = null
    private val projectLink: String? = null
    private val publishTime: Long = 0
    private val realSuperChapterId = 0
    private val selfVisible = 0
    private val shareDate: Long = 0
    private val shareUser: String? = null
    private val superChapterId = 0
    private val superChapterName: String? = null
    private val tags: List<String>? = null
    private val title: String? = null
    private val type = 0
    private val userId: Long = 0
    private val visible = 0
    private val zan = 0
}
