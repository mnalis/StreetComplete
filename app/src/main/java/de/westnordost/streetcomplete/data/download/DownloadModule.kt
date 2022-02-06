package de.westnordost.streetcomplete.data.download

import de.westnordost.streetcomplete.data.download.tiles.DownloadedTilesDao
import org.koin.core.qualifier.named
import org.koin.dsl.module

val downloadModule = module {
    factory<DownloadProgressSource> { get<DownloadController>() }
    factory { DownloadedTilesDao(get()) }
    factory { Downloader(get(), get(), get(), get(), get(named("SerializeSync"))) }
    factory { MobileDataAutoDownloadStrategy(get(), get()) }
    factory { WifiAutoDownloadStrategy(get(), get()) }

    single { DownloadController(get()) }
}
