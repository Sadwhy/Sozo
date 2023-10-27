package com.animestudios.animeapp.model

import android.os.Parcelable
import com.animestudios.animeapp.CustomViewPagerScroll
import com.animestudios.animeapp.MediaStatusAnimity
import com.animestudios.animeapp.anilist.response.MediaSeason
import com.animestudios.animeapp.anilist.response.MediaSource
import com.animestudios.animeapp.displayInDayDateTimeFormat
import com.animestudios.animeapp.type.*
import kotlinx.parcelize.Parcelize


@Parcelize
data class AniListMedia(
    val idAniList: Int = 0,
    val idMal: Int? = null,
    val title: MediaTitle = MediaTitle(),
    val type: MediaType? = null,
    val format: MediaFormat? = null,
    val status: MediaStatus? = null,
    val nextAiringEpisode: Int? = null,
    val description: String = "",
    val startDate: FuzzyDate? = null,
    val endDate: FuzzyDate? = null,
    val season: MediaSeason? = null,
    val seasonYear: Int? = null,
    val episodes: Int? = null,
    val duration: Int? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val countryOfOrigin: String? = null,
    val isLicensed: Boolean? = null,
    val source: MediaSource? = null,
    val streamingEpisode: List<Episodes>? = null,
//    val trailer: MediaTrailer? = null,
    val coverImage: MediaCoverImage = MediaCoverImage(),
    val bannerImage: String = "",
    val genres: List<Genre> = listOf(),
    val synonyms: List<String> = listOf(),
    val averageScore: Int = 0,
    val meanScore: Int = 0,
    val popularity: Int = 0,
    val trending: Int = 0,
    val favourites: Int = 0,
//    val tags: List<MediaTag> = listOf(),
    var isFavourite: Boolean = false,
    val isAdult: Boolean = false,
//    val nextAiringEpisode: AiringSchedule? = null,
//    val externalLinks: List<MediaExternalLink> = listOf(),
    val siteUrl: String = "",
    val mediaListEntry: MediaStatusAnimity? = null
) : Parcelable {

    fun getGenresToString(): String {
        return if (genres.size < 3) {
            genres.joinToString { it.name }
        } else if (genres.size > 3) {
            genres.subList(0, 2).joinToString { it.name }
        } else {
            genres.joinToString { it.name }
        }
    }

    fun getDateTimeStringFormat(): String? {
        return nextAiringEpisode.takeIf {
            it != null
        }?.run(::displayInDayDateTimeFormat)
    }
}