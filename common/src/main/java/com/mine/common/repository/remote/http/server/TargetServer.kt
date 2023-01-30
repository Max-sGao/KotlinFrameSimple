package com.mine.common.repository.remote.http.server

enum class TargetServer constructor(code: Int, envName: String, baseUrl: String, imgPrefUrl: String) {

    RELEASE(0, "正式环境", NetDevHelper.ApiUrls.RELEASE_BASE, NetDevHelper.ImagePrefixUrls.RELEASE_IMAGE),

    DEV(1, "开发环境", NetDevHelper.ApiUrls.DEV_BASE, NetDevHelper.ImagePrefixUrls.DEV_IMAGE),

    TEST(2, "测试环境", NetDevHelper.ApiUrls.TEST_BASE, NetDevHelper.ImagePrefixUrls.TEST_IMAGE);

    var code = -1

    var baseUrl = ""

    init {
        this.code = code
        this.baseUrl = baseUrl
    }

    companion object {
        fun getDefaultServer() = TEST

        fun findBy(code: Int): TargetServer? {
            for (server in values()) {
                if (code == server.code) return server
            }
            return null
        }
    }

}