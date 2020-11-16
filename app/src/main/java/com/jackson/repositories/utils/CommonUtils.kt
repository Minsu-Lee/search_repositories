package com.jackson.repositories.utils

import com.jackson.repositories.AppConst

object CommonUtils {

    /**
     * 공백을 기준으로 키워드를 판단하고, Query 데이터 양식에 맞춰 공백 대신 '+' 기호로 치환 후 가공된 Query를 반환합니다.
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
    fun checkQueryLength(queryStr: String?, minLimit: Int = 0, maxLimit: Int = AppConst.QUERY_MAX_LENGTH): Boolean
            = queryStr?.let { it.isNotEmpty() && it.length in minLimit..maxLimit } ?: false

}