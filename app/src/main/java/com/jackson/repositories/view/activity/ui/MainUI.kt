package com.jackson.repositories.view.activity.ui

import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.jackson.repositories.R
import com.jackson.repositories.base.WrapContentLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainUI: AnkoComponent<Activity> {

    lateinit var mSearchEt: EditText

    lateinit var mSearchBtn: LinearLayout

    lateinit var rv: RecyclerView

    lateinit var mGuidePhrase: LinearLayout

    lateinit var mGuideTv: TextView

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        verticalLayout {
            backgroundColor = Color.WHITE

            relativeLayout {
                backgroundColor = Color.TRANSPARENT

                mSearchEt = editText {
                    textColor = Color.BLACK
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    inputType = InputType.TYPE_CLASS_TEXT
                    gravity = Gravity.CENTER_VERTICAL
                    hint = ctx.getString(R.string.search_repositories_hint)
                    verticalPadding = 0
                    leftPadding = dip(12)
                    rightPadding = dip(60 + 12)
                }.lparams(width= matchParent, height= wrapContent) {
                    alignParentTop()
                    alignParentLeft()
                    sameBottom(R.id.repositories_search_btn)
                }

                mSearchBtn = linearLayout {
                    id = R.id.repositories_search_btn
                    isClickable = true
                    gravity = Gravity.CENTER
                    imageView(R.drawable.ic_repositories_search) {

                        backgroundColor = Color.TRANSPARENT
                        adjustViewBounds = true
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        padding = 0
                    }.lparams(width= dip(30), height= dip(30))
                }.lparams(width= dip(60), height= dip(60)) {
                    alignParentRight()
                    centerVertically()
                }

            }.lparams(width= matchParent, height= wrapContent)

            rv = recyclerView {
                visibility = View.GONE
                backgroundColor = Color.WHITE
                layoutManager = WrapContentLayoutManager(ctx, RecyclerView.VERTICAL, false)
            }.lparams(width= matchParent, height=0, weight = 1f)

            mGuidePhrase = verticalLayout {
                visibility = View.VISIBLE
                backgroundColor = Color.WHITE
                gravity = Gravity.CENTER

                mGuideTv = textView(R.string.search_guide_phrase) {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.empty_result_hint))
                    textColor = Color.parseColor("#586069")
                    typeface = ResourcesCompat.getFont(context, R.font.barlow_medium)
                    gravity = Gravity.CENTER
                }.lparams(width= wrapContent, height= wrapContent)

            }.lparams(width= matchParent, height=0, weight = 1f)

        }
    }

}