package com.instant.message.entity

class ScanCode {
    private var id:Int=0
    private var code:String=""

    fun getId():Int {
        return id
    }

    fun setId(id:Int) {
        this.id = id
    }

    fun getCode():String {
        return code
    }

    fun setCode(code:String) {
        this.code = code
    }
}