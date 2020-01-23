package com.jnr.buscamarvel.models

import java.util.*
import kotlin.collections.ArrayList

class Result (
    var code: Int = -1,
    var status: String = "",
    var copyright: String = "",
    var attributionText: String = "",
    var attributionHTML: String = "",
    var etag: String = "",
    var data: ResultData = ResultData()
)

class ResultData (
    var offset: Int = -1,
    var limit: Int = 0,
    var count: Int = 0,
    var results: ArrayList<Character> = ArrayList()
)

class Character (
    var id: Int = -1,
    var name: String = "",
    var description: String = "",
    var modified: String = "",
    var resourceURI: String,
    var thumbnail: Thumbnail = Thumbnail(),
    var comics: Resource = Resource(),
    var series: Resource = Resource(),
    var stories: Resource = Resource(),
    var events: Resource = Resource()
)

class Thumbnail (
    var path: String = "",
    var extension: String = ""
)

class Resource (
    var available: Int = -1,
    var returned: Int = -1,
    var collectionURI: String = "",
    var items: ArrayList<ResourceItem> = ArrayList()

)

class ResourceItem (
    var resourceURI: String = "",
    var name: String = "",
    var type: String = ""
)