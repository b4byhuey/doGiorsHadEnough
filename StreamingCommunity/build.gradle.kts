// use an integer for version numbers
version = 2


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "TV Shows and Movies from StreamingCommunity"
    authors = listOf("doGior")

    /**
    * Status int as the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta only
    * */
    status = 1


    requiresResources = false
    language = "it"

    iconUrl = "https://streamingcommunity.computer/icon/favicon-32x32.png?v=2"
}
