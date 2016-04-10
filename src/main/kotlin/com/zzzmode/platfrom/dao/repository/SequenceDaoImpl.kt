package com.zzzmode.platfrom.dao.repository

import com.zzzmode.platfrom.dto.VirtualUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.stereotype.Repository

import org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

/**
 * 实现mongodb 中id自增
 * Created by zl on 16/4/9.
 */
@Repository
open class SequenceDaoImpl : SequenceDao {

    @Autowired
    val mongoOperations: MongoOperations?=null

    override fun getNextSequenceId(key: String): Long {

        mongoOperations?.findAndModify(
                Query.query(where("_id").`is`(key)),
                Update().inc("seq",1),
                FindAndModifyOptions().returnNew(true),
                SequenceId::class.java
        )?.apply {
            seq?.apply {
                return@getNextSequenceId this
            }
        }
        throw RuntimeException("Unable to get sequence id for key : $key")
    }
}