package com.jackson.repositoriestest.utils

import com.jackson.repositoriestest.AppConst

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
    fun checkQueryLength(queryStr: String?, minLimit: Int = 0, maxLimit: Int = AppConst.QUERY_MAX_LENGTH): Boolean
            = queryStr?.let { it.isNotEmpty() && it.length in minLimit..maxLimit } ?: false

}