package com.instant.message.service

interface IBaseService<T,K> {
      fun insert(`object`: T): Boolean
      fun delete(id: K): Boolean
      fun update(`object`: T): Boolean
      fun toList(map: Map<String, Any>): List<T>
      fun selectByPrimaryKey(id: K): T
      fun selectByOnly(map: Map<String, Any>): T
}