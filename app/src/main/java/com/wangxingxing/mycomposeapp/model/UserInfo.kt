package com.wangxingxing.mycomposeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * author : 王星星
 * date : 2025/11/13 15:54
 * email : 1099420259@qq.com
 * description :
 */
@Parcelize
data class UserInfo(
    val admin: Boolean = false,
    val chapterTops: List<String> = listOf(),
    val collectIds: MutableList<Int> = mutableListOf(),
    val email: String = "",
    val icon: String = "",
    val id: String = "",
    val nickname: String = "",
    val password: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = ""
) : Parcelable
