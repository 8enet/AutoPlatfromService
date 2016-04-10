package com.zzzmode.platfrom.dto

import org.springframework.data.annotation.Id
import java.io.Serializable

/**
 * Created by zl on 16/4/9.
 */
open class AbstractDocument : Serializable {

    @Id
    var id: Long?= 1

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as AbstractDocument

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int{
        return id?.hashCode() ?: 0
    }


}