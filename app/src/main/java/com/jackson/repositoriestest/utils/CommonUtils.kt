package com.jackson.repositoriestest.utils

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

}