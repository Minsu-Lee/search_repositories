package com.jackson.repositoriestest.utils

import android.content.Context
import android.content.res.AssetManager
import com.jackson.repositoriestest.base.AppConst
import java.io.InputStream
import java.text.DecimalFormat
import java.util.*

object CommonUtils {

    /**
     * 공백을 기준으로 단어를 나누고, Query 데이터 양식에 맞춰 '+' 기호 삽입 후 가공된 Query를 반환합니다.
     */
    fun makeQueryParams(queryStr: String): String {
        return StringBuffer().apply {
            if (queryStr.indexOf(" ") >= 0) {
                append(queryStr.replace(" ", "+"))
            } else {
                append(queryStr)
            }
        }.toString()
    }

    /**
     *  query는 연산자 포함해 전체 256자만 사용가능 ( 아닐 시, 에러 )
     */
    fun checkQueryLength(
        queryStr: String?,
        minLimit: Int = 0,
        maxLimit: Int = AppConst.QUERY_MAX_LENGTH
    ): Boolean
            = queryStr?.let { it.isNotEmpty() && it.length in minLimit..maxLimit } ?: false

    /**
     * 1000 단위 이상 줄여서 표기
     */
    fun formatRep(rep: Int): String? {
        return when {
            rep < 1000 -> "$rep"
            else -> DecimalFormat("#,###.#k").format(rep / 1000.0)
        }
    }

    /**
     * src > main > assets > properties 파일 참조 ( key : value )
     */
    fun getProperty(key: String, ctx: Context): String {
        return Properties().apply {
            ctx.assets.let { assetManager: AssetManager ->
                assetManager.open("config.properties").let { inputStream: InputStream ->
                    load(inputStream)
                }
            }
        }.getProperty(key)
    }

}