package com.zzzmode.platfrom.dao.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

/**
 * 实现mongodb 中id自增
 * Created by zl on 16/4/9.
 */
@Repository
open class SequenceDaoImpl : SequenceDao {

    override fun initSequence() {
        mongoOperations?.run {
            if(!exists(Query.query(where("_id").`is`(SequenceDao.USER_SEQ_KEY)),SequenceId::class.java)){
                val seq=SequenceId()
                seq.id=SequenceDao.USER_SEQ_KEY
                seq.seq=0
                save(seq)
            }
        }

    }

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