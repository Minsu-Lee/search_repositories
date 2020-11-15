package com.jackson.repositoriestest.view.adapter.holder

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jackson.repositoriestest.extensions.dateFormat
import com.jackson.repositoriestest.extensions.toDate
import com.jackson.repositoriestest.model.RepositoriesData
import com.jackson.repositoriestest.presenter.RepositoriesAdapterConstract
import com.jackson.repositoriestest.utils.CommonUtils
import com.jackson.repositoriestest.utils.StringUtils
import com.jackson.repositoriestest.view.adapter.ui.RepositoriesItemUI
import org.jetbrains.anko.AnkoContext
import java.util.*

class RepositoriesItemViewHolder(parent: ViewGroup, val ui: RepositoriesItemUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent))) {

     @RequiresApi(Build.VERSION_CODES.O)
     fun onBind(data: RepositoriesData, position: Int, view: RepositoriesAdapterConstract.View) = with(ui) {

          /** init data */
          mRepoTitleTv.text = data.fullName
          mRepoDescTv.visibility = if (data.description.isNullOrEmpty()) View.GONE else View.VISIBLE
          mRepoDescTv.text = StringUtils.defaultStr(data.description)
          mRepoLanguageGroup.visibility = if (data.language.isNullOrEmpty()) View.GONE else View.VISIBLE
          mRepoLanguageTv.text = data.language
          mRepoStarScoreTv.text = "${CommonUtils.formatRep(data.stargazersCount)}"
          (!(data.license?.key?.isNullOrEmpty() ?: true)).let { isLicense ->
               mRepoLicenseTv.text = if (isLicense) "${data.license!!.key} license" else ""
               mRepoLicenseTv.visibility = if (isLicense) View.VISIBLE else View.GONE
          }
          data.pushedAt
               .toDate("yyyy-MM-dd'T'HH:mm:ss'Z'")
               .dateFormat("d MMM", Locale.ENGLISH).let { dateStr ->
               mRepoUpdateTv.text = "Updated on $dateStr"
          }

          /** Click Listener*/
          mViewCard.setOnClickListener {
               view.onViewClickListener(data, position)
          }

     }
}