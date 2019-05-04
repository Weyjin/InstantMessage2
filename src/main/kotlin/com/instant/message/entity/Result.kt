package com.instant.message.entity

class Result {

    private var id:Int=0
    private var describe:String=""
    private var name:String=""
    private var img:String=""

    fun getId():Int {
        return id;
    }

    fun setId(id:Int) {
        this.id = id
    }

    fun getDescribe():String {
        return describe
    }

    fun setDescribe(describe:String) {
        this.describe = describe
    }

    fun getName():String {
        return name
    }

    fun setName(name:String) {
        this.name = name
    }

    fun getImg():String {
        var images:Array<String> = arrayOf("images/user1-default.jpg","images/user2-default.jpg",
                "images/user3-default.jpg","images/user4-default.jpg",
                "images/user5-default.jpg","images/user6-default.jpg",
                "images/user7-default.jpg","images/user8-default.jpg",
                "images/user9-default.jpg")
        var random:Int= (Math.random()*(images.size-1)).toInt()
        return images[random]
    }

    fun setImg(img:String) {
        this.img = img
    }

}