package com.jackson.repositories.view.adapter.holder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jackson.repositories.extensions.dateFormat
import com.jackson.repositories.extensions.toDate
import com.jackson.repositories.model.RepositoriesData
import com.jackson.repositories.presenter.RepositoriesAdapterConstract
import com.jackson.repositories.utils.CommonUtils
import com.jackson.repositories.utils.StringUtils
import com.jackson.repositories.view.adapter.ui.RepositoriesItemUI
import org.jetbrains.anko.AnkoContext
import java.util.*

class RepositoriesItemViewHolder(parent: ViewGroup, val ui: RepositoriesItemUI):
     RecyclerView.ViewHolder(ui.createView(AnkoContext.create(parent.context, parent))) {

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