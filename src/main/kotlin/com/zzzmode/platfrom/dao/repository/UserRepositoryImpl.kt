package com.zzzmode.platfrom.dao.repository

import com.mongodb.WriteResult
import com.zzzmode.platfrom.dto.VirtualUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

/**
 * 更多用法参考http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries
 *
 * 本来可以继承{@see org.springframework.data.mongodb.repository.support.SimpleMongoRepository}少写很多代码
 * 但是不知道什么原因找不到bean,
 * Created by zl on 16/4/9.
 */
@Repository
open class UserRepositoryImpl : UserRepository {

    companion object{

        private val logger= LoggerFactory.getLogger(UserRepositoryImpl::class.java)
    }

    @Autowired
    open var mongoTemplate: MongoTemplate?=null

    @Autowired
    open var sequenceDao: SequenceDao?=null


    override fun findOne(id: Long): VirtualUser? {
        return mongoTemplate?.findOne(Query.query(where("_id").`is`(id)), VirtualUser::class.java)
    }

    override fun findAll(): List<VirtualUser>? {
        return mongoTemplate?.findAll(VirtualUser::class.java)
    }

    override  fun findByPhone(phone: String?): VirtualUser? {
        return mongoTemplate?.findOne(Query.query(where("phone").`is`(phone)), VirtualUser::class.java)
    }

    override  fun save(entity: VirtualUser?): VirtualUser?{

        entity?.apply {
            entity.id = sequenceDao?.getNextSequenceId(SequenceDao.USER_SEQ_KEY)
            mongoTemplate?.save(entity)
        }

        return entity
    }


    open  fun exists(id:Long):Boolean{

       return  mongoTemplate?.exists(Query.query(where("_id").`is`(id)),VirtualUser::class.java)!!
    }


    override fun delete(entity: VirtualUser?) :Boolean{
        return retResult(mongoTemplate?.remove(entity))
    }

    override fun updata(entity: VirtualUser?){
       mongoTemplate?.save(entity)

    }

    override fun delete(id: Long) :Boolean{
        return retResult(mongoTemplate?.remove(Query.query(where("_id").`is`(id)),VirtualUser::class.java))
    }

    private fun retResult(result: WriteResult?):Boolean{
        result?.run {
            return n >= 1
        }
        return false
    }


}