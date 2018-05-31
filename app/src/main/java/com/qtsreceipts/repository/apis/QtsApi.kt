package com.qtsreceipts.repository.apis

import com.qtsreceipts.repository.models.ReceiptsResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface QtsApi {
    @GET("https://g525204.github.io/recipes.json")
    fun listReceipts(): Observable<ReceiptsResponse>
}