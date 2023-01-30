package com.mine.common.repository.local.db.source

import com.mine.common.repository.local.db.LocalDatabase
import com.mine.common.repository.local.db.dao.UserDao
import com.mine.common.repository.local.db.entity.User

class UserSource {

    companion object : ILocalSource() {
        suspend fun query(): List<User> {
            return runIO {
                getUserDao().query()
            }
        }

        suspend fun insert(user: User) {
            runIO {
                getUserDao().insert(user)
            }
        }

        suspend fun delete() {
            runIO {
                getUserDao().delete()
            }
        }

        private fun getUserDao(): UserDao = LocalDatabase.getInstance().getUserDao()
    }
}