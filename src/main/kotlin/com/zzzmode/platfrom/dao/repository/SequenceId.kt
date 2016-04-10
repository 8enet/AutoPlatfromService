package com.zzzmode.platfrom.dao.repository

import com.zzzmode.platfrom.dto.AbstractDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by zl on 16/4/9.
 */
@Document(collection = "sequence")
class SequenceId {

    @Id
    var id:String?=null

    var seq:Long?=0

    override fun toString(): String{
        return "SequenceId(id=$id,seq=$seq)"
    }


}