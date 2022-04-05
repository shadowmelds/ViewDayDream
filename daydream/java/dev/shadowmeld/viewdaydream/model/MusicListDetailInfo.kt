package dev.shadowmeld.viewdaydream.model

/*
{
    "code": 200,
    "relatedVideos": null,
    "playlist": {
        "id": 7294547682,
        "name": "「共度朝夕」落日余晖的路上总是爱意弥漫",
        "coverImgId": 109951167058595710,
        "coverImgUrl": "https://p1.music.126.net/0CIFNXIfCg9x7xubzOzaMQ==/109951167058595707.jpg",
        "coverImgId_str": "109951167058595707",
        "adType": 0,
        "userId": 5088230961,
        "createTime": 1645011351687,
        "status": 0,
        "opRecommend": false,
        "highQuality": false,
        "newImported": false,
        "updateTime": 1645016641561,
        "trackCount": 45,
        "specialType": 0,
        "privacy": 0,
        "trackUpdateTime": 1645016482616,
        "commentThreadId": "A_PL_0_7294547682",
        "playCount": 9763,
        "trackNumberUpdateTime": 1645016051442,
        "subscribedCount": 40,
        "cloudTrackCount": 0,
        "ordered": true,
        "description": "我希望那个未来的你可以坚定地向我走来，我害怕一切的不确定，但我希望你是我的确定。",
        "tags": [
            "华语",
            "浪漫"
        ],
        "updateFrequency": null,
        "backgroundCoverId": 0,
        "backgroundCoverUrl": null,
        "titleImage": 0,
        "titleImageUrl": null,
        "englishTitle": null,
        "officialPlaylistType": null,
        "subscribed": null,
        "creator": {
            "defaultAvatar": false,
            "province": 110000,
            "authStatus": 0,
            "followed": false,
            "avatarUrl": "http://p1.music.126.net/cihS-kcpfR1Nutt9ZdfXRQ==/109951166179460718.jpg",
            "accountStatus": 0,
            "gender": 0,
            "city": 110101,
            "birthday": 0,
            "userId": 5088230961,
            "userType": 0,
            "nickname": "Justin爱吃水煮鱼",
            "signature": "",
            "description": "",
            "detailDescription": "",
            "avatarImgId": 109951166179460720,
            "backgroundImgId": 109951162868126480,
            "backgroundUrl": "http://p1.music.126.net/_f8R60U9mZ42sSNvdPn2sQ==/109951162868126486.jpg",
            "authority": 0,
            "mutual": false,
            "expertTags": null,
            "experts": null,
            "djStatus": 0,
            "vipType": 11,
            "remarkName": null,
            "authenticationTypes": 0,
            "avatarDetail": null,
            "backgroundImgIdStr": "109951162868126486",
            "avatarImgIdStr": "109951166179460718",
            "anchor": false,
            "avatarImgId_str": "109951166179460718"
        },
        "tracks": [
            {
                "name": "浪漫主义2.0",
                "id": 1920025919,
                "pst": 0,
                "t": 0,
                "ar": [
                    {
                        "id": 1198123,
                        "name": "姜云升",
                        "tns": [],
                        "alias": []
                    }
                ],
                "alia": [],
                "pop": 100,
                "st": 0,
                "rt": "",
                "fee": 8,
                "v": 3,
                "crbt": null,
                "cf": "",
                "al": {
                    "id": 140341308,
                    "name": "浪漫主义2.0",
                    "picUrl": "http://p4.music.126.net/OI1ZorJZneFJ8ILmzal5aA==/109951167046276957.jpg",
                    "tns": [],
                    "pic_str": "109951167046276957",
                    "pic": 109951167046276960
                },
                "dt": 174650,
                "h": {
                    "br": 320000,
                    "fid": 0,
                    "size": 6988371,
                    "vd": -37565
                },
                "m": {
                    "br": 192000,
                    "fid": 0,
                    "size": 4193060,
                    "vd": -34952
                },
                "l": {
                    "br": 128000,
                    "fid": 0,
                    "size": 2795405,
                    "vd": -33356
                },
                "a": null,
                "cd": "01",
                "no": 0,
                "rtUrl": null,
                "ftype": 0,
                "rtUrls": [],
                "djId": 0,
                "copyright": 0,
                "s_id": 0,
                "mark": 0,
                "originCoverType": 1,
                "originSongSimpleData": null,
                "single": 0,
                "noCopyrightRcmd": null,
                "rtype": 0,
                "rurl": null,
                "mst": 9,
                "cp": 1416771,
                "mv": 0,
                "publishTime": 0
            }
        ],
        "videoIds": null,
        "videos": null,
        "trackIds": [
            {
                "id": 1920025919,
                "v": 3,
                "t": 0,
                "at": 1645016051441,
                "alg": null,
                "uid": 5088230961,
                "rcmdReason": ""
            }
        ],
        "shareCount": 0,
        "commentCount": 0,
        "remixVideo": null,
        "sharedUsers": null,
        "historySharedUsers": null
    },
    "urls": null,
    "sharedPrivilege": null,
    "resEntrance": null
}
 */
data class MusicListDetailInfo(
    val code: String?,
    val relatedVideos: String?,
    val playlist: MusicListPlaylistInfo?
)

data class MusicListPlaylistInfo(
    val id: String?,
    val name: String?,
    val coverImgUrl: String?,
    val userId: String?,
    val createTime: String?,
    val status: String?,
    val updateTime: String?,
    val trackCount: Int?,
    val trackUpdateTime: String?,
    val playCount: Int?,
    val trackNumberUpdateTime: String?,
    val subscribedCount: Int?,
    val ordered: Boolean?,
    val description: String?,
    val tags: List<String>?,
    val titleImageUrl: String?,
    val creator: MusicListCreator?,
    val tracks: List<MusicListTracks>?,
    val trackIds: List<MusicListTrackIds>?
)

data class MusicListCreator(
    val defaultAvatar: Boolean?,
    val province: String?,
    val authStatus: String?,
    val followed: Boolean?,
    val avatarUrl: String?,
    val accountStatus: String?,
    val gender: String?,
    val city: String?,
    val birthday: String?,
    val userId: String?,
    val userType: String?,
    val nickname: String?,
    val signature: String?,
    val description: String?,
    val detailDescription: String?,
    val avatarImgId: String?,
    val backgroundImgId: String?,
    val backgroundUrl: String?,
    val authority: Int?,
    val mutual: Boolean?,
    val vipType: Int?,
    val anchor: Boolean?
)

data class MusicListTrackIds(
    val id: String?,
    val v: Int?,
    val t: Int?,
    val at: String?,
    val uid: String?
)

data class MusicListTracks(
    val name: String?,
    val id: String?,
    val ar: List<MusicListTracksAr>?,
    val al: MusicListTracksAl?,
    val dt: Long?
)

data class MusicListTracksAr(
    val id: String?,
    val name: String?
)

data class MusicListTracksAl(
    val id: String?,
    val name: String?,
    val picUrl: String?,
)