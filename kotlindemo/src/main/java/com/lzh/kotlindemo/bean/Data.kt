package com.lzh.kotlindemo.bean

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description
 *
 */

data class Data(var name: String, var age: Int) {
    constructor() : this("", 0) {
        print("name = $name, age = $age")
    }

    override fun equals(other: Any?): Boolean {
        println("invoked equals")
        if (other !is Data) {
            return false
        }

        return name == other.name && age == other.age
    }
}