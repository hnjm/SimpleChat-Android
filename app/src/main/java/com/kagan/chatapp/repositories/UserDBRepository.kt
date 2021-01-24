package com.kagan.chatapp.repositories

import com.kagan.chatapp.db.dao.UsersDao
import javax.inject.Inject

class UserDBRepository
@Inject
constructor(private val usersDao: UsersDao) {

}