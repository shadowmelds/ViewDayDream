package dev.shadowmeld.viewdaydream.model

/*
{
    "hasTaste": false,
    "code": 200,
    "category": 0,
    "result": [
        {
            "id": 2237551001,
            "type": 0,
            "name": "粤语传世经典，怀旧是人的本能",
            "copywriter": "热门推荐",
            "picUrl": "https://p1.music.126.net/PsONIoIzCJ-9gPCAeq9ahw==/19115009649498152.jpg",
            "canDislike": true,
            "trackNumberUpdateTime": 1641528893190,
            "playCount": 85847984,
            "trackCount": 209,
            "highQuality": false,
            "alg": "cityLevel_unknow"
        },
        ...
    ]
}
 */

data class PersonalizedMusicListInfo(
    val id: String?,
    val type: String?,
    val name: String?,
    val copywriter: String?,
    val picUrl: String?,
    val canDislike: Boolean?,
    val trackNumberUpdateTime: String?,
    val playCount: String?,
    val trackCount: Int?,
    val highQuality: String?,
    val alg: String?,
)
