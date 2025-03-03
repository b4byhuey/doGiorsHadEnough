package it.dogior.hadEnough

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LiveSearchResponse
import com.lagradost.cloudstream3.LiveStreamLoadResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities

class TV2 : MainAPI() {
    override var mainUrl = "https://huhu.to"
    override var name = "TV 🏴‍☠️"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "un"
    override val hasMainPage = true

    private suspend fun getChannels(): List<Channel> {
        val response = app.get("$mainUrl/channels").body.string()
        return parseJson<List<Channel>>(response)
    }

    companion object {
        var channels = emptyList<Channel>()
        @Suppress("ConstPropertyName")
        const val posterUrl =
            "https://raw.githubusercontent.com/doGior/doGiorsHadEnough/master/TV2/tv.png"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (channels.isEmpty()) {
            channels = getChannels()
        }
        val sections = channels.groupBy { it.country }.map {
            HomePageList(
                it.key,
                it.value.map { channel -> channel.toSearchResponse(this.name) },
                false
            )
        }.sortedBy { it.name }

        return newHomePageResponse(
            sections
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (channels.isEmpty()) {
            channels = getChannels()
        }
        val matches = channels.filter { channel ->
            query.lowercase().replace(" ", "") in
                    channel.name.lowercase().replace(" ", "")
        }
        return matches.map { it.toSearchResponse(this.name) }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("TV2", url)
        val channel = parseJson<Channel>(url)
        return LiveStreamLoadResponse(
            channel.name,
            url,
            this.name,
            "https://huhu.to/play/${channel.id}/index.m3u8",
            posterUrl = posterUrl
        )
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        callback(
            ExtractorLink(
                this.name,
                this.name,
                data,
                referer = "",
                quality = Qualities.Unknown.value,
                isM3u8 = true
            )
        )
        return true
    }

    data class Channel(
        @JsonProperty("country")
        val country: String,
        @JsonProperty("id")
        val id: Long,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("p")
        val p: Int
    ) {
        fun toSearchResponse(apiName: String): LiveSearchResponse {
            return LiveSearchResponse(
                name,
                this.toJson(),
                apiName,
                posterUrl = posterUrl
            )
        }
    }
}