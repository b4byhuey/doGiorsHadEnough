import org.jetbrains.kotlin.konan.properties.Properties

// use an integer for version numbers
version = 3


cloudstream {
    // All of these properties are optional, you can safely remove them
    description =
        "Torrents from Il Corsaro Nero. If something doesn't work the torrent has probably not enough seeds"
    authors = listOf("doGior")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1

    tvTypes = listOf("Movie", "Torrent")

    requiresResources = true
    language = "it"

    iconUrl = "https://ilcorsaronero.link/assets/images/logo-b01e50fc691111643e95a98975b49be1.svg"
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("secrets.properties").inputStream())
        android.buildFeatures.buildConfig = true
        buildConfigField("String", "TMDB_API", "\"${properties.getProperty("TMDB_API")}\"")
    }
}
