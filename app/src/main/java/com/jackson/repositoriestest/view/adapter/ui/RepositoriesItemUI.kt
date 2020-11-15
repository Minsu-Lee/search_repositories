package com.jackson.repositoriestest.view.adapter.ui

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.jackson.repositoriestest.R
import org.jetbrains.anko.*

class RepositoriesItemUI: AnkoComponent<ViewGroup> {

    lateinit var mViewCard: LinearLayout

    lateinit var mRepoTitleTv: TextView

    lateinit var mRepoDescTv: TextView

    lateinit var mRepoLanguageGroup: LinearLayout

    lateinit var mRepoLanguageTv: TextView

    lateinit var mRepoStarScoreTv: TextView

    lateinit var mRepoLicenseTv: TextView

    lateinit var mRepoUpdateTv: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            mViewCard = this
            backgroundColor = Color.TRANSPARENT
            orientation = LinearLayout.HORIZONTAL
            lparams(width = matchParent, height = wrapContent) {
                verticalPadding = dip(15)
            }

            verticalLayout {

                imageView(R.drawable.ic_repository_project) {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(width = dip(24), height = dip(24)) {
                    rightMargin = dip(15)
                }

            }.lparams(width = wrapContent, height = wrapContent)

            verticalLayout {

                /** 저장소 이름 */
                mRepoTitleTv = textView {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_title_name))
                    textColor = Color.parseColor("#0366d6")
                    typeface = ResourcesCompat.getFont(context, R.font.barlow_medium)
                    gravity = Gravity.CENTER_VERTICAL
                }.lparams(width = matchParent, height = wrapContent) {
                    bottomMargin = dip(8)
                }

                /** 저장소 설명 */
                mRepoDescTv = textView {
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_title_name))
                    textColor = Color.parseColor("#24292e")
                    typeface = ResourcesCompat.getFont(context, R.font.barlow_regular)
                    gravity = Gravity.CENTER_VERTICAL
                }.lparams(width = matchParent, height = wrapContent) {
                    bottomMargin = dip(8)
                }

                /** 라이센스 */
                mRepoLicenseTv = textView {
                    R.id.repositories_license_tv
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_etc_str))
                    textColor = Color.parseColor("#9a9a9a")
                    gravity = Gravity.CENTER_VERTICAL
                }.lparams(width = wrapContent, height = wrapContent) {
                    bottomMargin = dip(8)
                }


                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL

                    /** 저장소 별점 */
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL

                        imageView(R.drawable.ic_repository_score) {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams(width = dip(20), height = dip(20)) {
                            rightMargin = dip(2)
                        }

                        mRepoStarScoreTv = textView {
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_etc_str))
                            textColor = Color.parseColor("#586069")
                            gravity = Gravity.CENTER_VERTICAL
                        }.lparams(width = wrapContent, height = wrapContent)

                    }.lparams(width = wrapContent, height = wrapContent) {
                        rightMargin = dip(10)
                    }

                    /** 저장소 프로젝트 언어 */
                    mRepoLanguageGroup = linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL

                        imageView(R.drawable.ic_repository_language) {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams(width = dip(20), height = dip(20)) {
                            rightMargin = dip(2)
                        }

                        mRepoLanguageTv = textView {
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_etc_str))
                            textColor = Color.parseColor("#586069")
                            gravity = Gravity.CENTER_VERTICAL
                        }.lparams(width = wrapContent, height = wrapContent)

                    }.lparams(width = wrapContent, height = wrapContent) {
                        rightMargin = dip(10)
                    }

                    /** 저장소 수정일자 */
                    mRepoUpdateTv = textView {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.repo_etc_str))
                        textColor = Color.parseColor("#586069")
                        gravity = Gravity.CENTER_VERTICAL
                    }.lparams(width = wrapContent, height = wrapContent)

                }.lparams(width = wrapContent, height = wrapContent)

            }.lparams(width = 0, height = wrapContent, weight = 1f)

        }
    }
}