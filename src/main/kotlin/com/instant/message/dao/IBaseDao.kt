package com.instant.message.dao

interface IBaseDao<T,K> {
     fun insert(`object`: T): Int
     fun delete(id: K): Int
     fun update(`object`: T): Int
     fun toList(map: Map<String, Any>): List<T>
     fun selectByPrimaryKey(id: K): T
     fun selectByOnly(map: Map<String, Any>): T

}