package com.jackson.repositories.view.ui

import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jackson.repositories.R
import com.jackson.repositories.base.WrapContentLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainUI : AnkoComponent<Activity> {

    lateinit var mSearchEt: EditText

    lateinit var mSearchBtn: ImageButton

    lateinit var rv: RecyclerView

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        verticalLayout {
            backgroundColor = Color.WHITE

            linearLayout {

                backgroundColor = Color.WHITE

                mSearchEt = editText {
                    textColor = Color.BLACK
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    inputType = InputType.TYPE_CLASS_TEXT
                    gravity = Gravity.CENTER_VERTICAL
                    hint = "저장소 검색"
                    leftPadding = dip(12)
                }.lparams(width= 0, height= matchParent, weight= 1f)

                mSearchBtn = imageButton(R.drawable.ic_repositories_search) {
                    id = R.id.repositories_search_btn
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(width= dip(64), height= dip(64))

            }.lparams(width= matchParent, height= wrapContent)

            rv = recyclerView {
                backgroundColor = Color.BLUE
                layoutManager = WrapContentLayoutManager(ctx, RecyclerView.VERTICAL, false).apply {
                    isAutoMeasureEnabled = true
                }
            }.lparams(width= matchParent, height=0, weight = 1f)

        }
    }

}