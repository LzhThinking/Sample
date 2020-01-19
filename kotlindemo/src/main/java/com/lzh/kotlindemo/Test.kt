package com.lzh.kotlindemo

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description
 *
 */
const val HOME = "LEI"

class Test {
    companion object {
        var name = "brown"
    }


}


object TestObject {
    const val OBJECT: String = "leizhiheng"

    val name:String = "blue"

    lateinit var names: String
}

