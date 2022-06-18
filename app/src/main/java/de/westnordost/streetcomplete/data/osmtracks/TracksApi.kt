package de.westnordost.streetcomplete.data.osmtracks

import de.westnordost.streetcomplete.data.download.ConnectionException
import de.westnordost.streetcomplete.data.user.AuthorizationException

/**
 * Creates GPS / GPX trackpoint histories
 */
interface TracksApi {

    /**
     * Create a new GPX track history
     *
     * @param trackpoints history of recorded trackpoints
     * @param noteText optional text appended to the track
     *
     * @throws AuthorizationException if this application is not authorized to write notes
     *                                (Permission.READ_GPS_TRACES, Permission.WRITE_GPS_TRACES)
     * @throws ConnectionException if a temporary network connection problem occurs
     *
     * @return the new track
     */
    fun create(trackpoints: List<Trackpoint>, noteText: String?): Track
}
