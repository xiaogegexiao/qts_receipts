package com.qtsreceipts.repository.models

import com.qtsreceipts.repository.models.Receipt

class ReceiptsResponse {
    var title = ""
    var version = ""
    var href = ""
    var results = arrayListOf<Receipt>()
}