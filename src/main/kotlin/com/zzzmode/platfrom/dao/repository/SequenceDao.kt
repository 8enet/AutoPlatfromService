package com.zzzmode.platfrom.dao.repository

import org.springframework.data.repository.NoRepositoryBean

/**
 * Created by zl on 16/4/9.
 */
@NoRepositoryBean
interface SequenceDao {
    companion object{
        val USER_SEQ_KEY="user_seq"
    }

    fun initSequence()

    fun getNextSequenceId(key:String):Long
}