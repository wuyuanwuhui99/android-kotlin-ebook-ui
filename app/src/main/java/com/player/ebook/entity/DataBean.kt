package com.player.ebook.entity

import java.util.ArrayList
import java.util.Random

class DataBean {
    var imageRes: Int? = null
    var imageUrl: String? = null
    var title: String? = null
    var viewType: Int = 0

    constructor(imageRes: Int?, title: String, viewType: Int) {
        this.imageRes = imageRes
        this.title = title
        this.viewType = viewType
    }

    constructor(imageUrl: String, title: String, viewType: Int) {
        this.imageUrl = imageUrl
        this.title = title
        this.viewType = viewType
    }

    companion object {

        val testData3: List<DataBean>
            get() {
                val list = ArrayList<DataBean>()
                list.add(
                    DataBean(
                        "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
                        null,
                        1
                    )
                )
                list.add(
                    DataBean(
                        "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
                        null,
                        1
                    )
                )
                list.add(
                    DataBean(
                        "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg",
                        null,
                        1
                    )
                )
                list.add(
                    DataBean(
                        "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                        null,
                        1
                    )
                )
                list.add(
                    DataBean(
                        "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
                        null,
                        1
                    )
                )
                return list
            }

        fun getColors(size: Int): List<String> {
            val list = ArrayList<String>()
            for (i in 0 until size) {
                list.add(randColor)
            }
            return list
        }

        /**
         * 获取十六进制的颜色代码.例如  "#5A6677"
         * 分别取R、G、B的随机值，然后加起来即可
         *
         * @return String
         */
        val randColor: String
            get() {
                var R: String
                var G: String
                var B: String
                val random = Random()
                R = Integer.toHexString(random.nextInt(256)).toUpperCase()
                G = Integer.toHexString(random.nextInt(256)).toUpperCase()
                B = Integer.toHexString(random.nextInt(256)).toUpperCase()

                R = if (R.length == 1) "0$R" else R
                G = if (G.length == 1) "0$G" else G
                B = if (B.length == 1) "0$B" else B

                return "#$R$G$B"
            }
    }

    constructor(s: String, nothing: Nothing?, i: Int)
}

