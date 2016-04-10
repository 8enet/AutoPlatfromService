package com.zzzmode.platfrom.dao.repository

import org.springframework.data.repository.NoRepositoryBean

/**
 * Created by zl on 16/4/9.
 */
@NoRepositoryBean
interface SequenceDao {
    fun getNextSequenceId(key:String):Long;
}