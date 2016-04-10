package com.zzzmode.platfrom.dao.repository

import com.zzzmode.platfrom.dto.VirtualUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

/**
 * Created by zl on 16/4/8.
 */
@NoRepositoryBean
interface UserRepository
{

    fun save(entity: VirtualUser?): VirtualUser?;

    fun updata(entity: VirtualUser?);


    fun delete(entity: VirtualUser?):Boolean;
    fun delete(id: Long):Boolean;

    fun findByPhone(phone: String?): VirtualUser?;


    fun findAll():List<VirtualUser>?;

    fun findOne(id:Long): VirtualUser?;

}