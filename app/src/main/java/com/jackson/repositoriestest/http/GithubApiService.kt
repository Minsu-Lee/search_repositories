package com.jackson.repositoriestest.http

import com.jackson.repositoriestest.BuildConfig
import com.jackson.repositoriestest.model.RepositoriesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface GithubApiService {

    /**
     * 사용될 수 있는 Parameters
     * @Query("q") query: String,
     * @Query("sort") sort: String,
     * @Query("order") order: String,
     * @Query("page") page: Int,
     * @Query("per_page") perPage: Int
     */
    @Headers(
        "Authorization: BEARER ${BuildConfig.GIT_TOKEN}",
        "Content-Type: application/json"
    )
    @GET(ApiUrl.SEARCH_REPOSITORIES)
    fun searchRepositories(@QueryMap params: HashMap<String, Any?>): Observable<RepositoriesResponse>

}