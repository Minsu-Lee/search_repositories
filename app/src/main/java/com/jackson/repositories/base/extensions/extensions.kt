package com.jackson.repositories.extensions

import android.view.View
import android.view.ViewTreeObserver
import java.text.SimpleDateFormat
import java.util.*

/**
 * View의 크기값이 필요한 경우, f() 내에서 View.height으로 참조가 가능합니다.
 *
 * ex) View.afterMeasured { Log.e("test", "${View.height}") }
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * 단일 클릭 on / off
 * API 응답이 오기전 여러차례 요청을 보내는 이슈 대안
 */
fun View.safeViewLock(lock: Boolean) {
    isEnabled = !lock
    isClickable = !lock
}

/**
 * 참고 : https://meetup.toast.com/posts/130
 */
fun String.toDate(formatStr: String = "yyyy-MM-dd'T'HH:mm:ss'Z'", timeZone: TimeZone = TimeZone.getTimeZone("Asia/seoul")): Date {
    return with(SimpleDateFormat(formatStr).also { it.timeZone = timeZone }) {
        parse(this@toDate)
    }
}
fun Date.dateFormat(toFormatStr: String, locale: Locale = Locale.KOREAN) = with(SimpleDateFormat(toFormatStr, locale)) {
    this.format(this@dateFormat)
}