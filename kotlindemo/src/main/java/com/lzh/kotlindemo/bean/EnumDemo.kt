package com.lzh.kotlindemo.bean

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description
 *
 */

enum class EnumDemo {
    RED, BLUE, GREEN
}

enum class Colors(var rgb: Int) {
    RED(0xFF00FF),
    BLUE(0xFFFF00),
    GREEN(0x00FFFF)
}

enum class Anoymous() {
    RED {
        override fun signal(): Anoymous {
            return RED
        }
    },

    GREEN {
        override fun signal(): Anoymous {
            return GREEN
        }
    };

    abstract fun signal(): Anoymous
}