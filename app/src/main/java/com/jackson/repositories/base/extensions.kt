package com.jackson.repositories.base

import android.view.View
import android.view.ViewTreeObserver

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