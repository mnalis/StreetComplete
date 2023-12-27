package de.westnordost.streetcomplete.data.user.oauth

import de.westnordost.streetcomplete.data.download.ConnectionException
import de.westnordost.streetcomplete.data.user.AuthorizationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class OAuthAuthorizationTest {
    @Test fun createAuthorizationUrl() {
        val url = URL(createOAuth().createAuthorizationUrl())

        assertEquals("https", url.protocol)
        assertEquals("test.me", url.host)
        assertEquals("/auth", url.path)

        val parameters = url.queryParameters
        assertEquals("code", parameters["response_type"])
        assertEquals("ClientId %#+!", parameters["client_id"])
        assertEquals("localhost://oauth", parameters["redirect_uri"])
        assertEquals("one! 2 THREE+(1/2)", parameters["scope"])
        assertNotNull(parameters["state"])
        assertEquals("S256", parameters["code_challenge_method"])
        assertTrue(parameters["code_challenge"]!!.length <= 128)
        assertTrue(parameters["code_challenge"]!!.length >= 43)
    }

    @Test fun `generates different code challenge for each instance`() {
        val url1 = URL(createOAuth().createAuthorizationUrl())
        val url2 = URL(createOAuth().createAuthorizationUrl())
        assertTrue(url1.queryParameters["code_challenge"] != url2.queryParameters["code_challenge"])
    }

    @Test fun `generates different state for each instance`() {
        val url1 = URL(createOAuth().createAuthorizationUrl())
        val url2 = URL(createOAuth().createAuthorizationUrl())
        assertTrue(url1.queryParameters["state"] != url2.queryParameters["state"])
    }

    @Test fun `serializes correctly`() {
        val oauth1 = createOAuth()
        val oauth1String = Json.encodeToString(oauth1)
        val oauth2 = Json.decodeFromString<OAuthAuthorization>(oauth1String)
        val oauth2String = Json.encodeToString(oauth2)

        assertEquals(oauth1String, oauth2String)
    }

    @Test fun itsForMe() {
        val oauth = createOAuth()
        val state = urlEncode(URL(oauth.createAuthorizationUrl()).queryParameters["state"]!!)

        assertFalse(oauth.itsForMe(URI("localhost://oauth"))) // no state
        assertFalse(oauth.itsForMe(URI("localhost://oauth?state=abc"))) // different state
        assertTrue(oauth.itsForMe(URI("localhost://oauth?state=$state"))) // same state
        // different uri
        assertFalse(oauth.itsForMe(URI("localhost://oauth3?state=$state")))
        assertFalse(oauth.itsForMe(URI("localhost://oauth/path?state=$state")))
        assertFalse(oauth.itsForMe(URI("localboost://oauth?state=$state")))
    }

    @Test fun `extractAuthorizationCode fails with useful error messages`() {
        val oauth = createOAuth()

        // server did not respond correctly with "error"
        assertFailsWith<ConnectionException> {
            oauth.extractAuthorizationCode(URI("localhost://oauth?e=something"))
        }

        try {
            oauth.extractAuthorizationCode(URI("localhost://oauth?error=hey%2Bwhat%27s%2Bup"))
        } catch (e: AuthorizationException) {
            assertEquals("hey what's up", e.message)
        }

        try {
            oauth.extractAuthorizationCode(URI("localhost://oauth?error=A%21&error_description=B%21"))
        } catch (e: AuthorizationException) {
            assertEquals("A!: B!", e.message)
        }

        try {
            oauth.extractAuthorizationCode(URI("localhost://oauth?error=A%21&error_uri=http%3A%2F%2Fabc.de"))
        } catch (e: AuthorizationException) {
            assertEquals("A! (see http://abc.de)", e.message)
        }

        try {
            oauth.extractAuthorizationCode(URI("localhost://oauth?error=A%21&error_description=B%21&error_uri=http%3A%2F%2Fabc.de"))
        } catch (e: AuthorizationException) {
            assertEquals("A!: B! (see http://abc.de)", e.message)
        }
    }

    @Test fun extractAuthorizationCode() {
        assertEquals(
            "my code",
            createOAuth().extractAuthorizationCode(URI("localhost://oauth?code=my%20code"))
        )
    }

    // it's not properly possible to test retrieveAccessToken in isolation because the http client
    // is not injected (passed in the constructor)
}

private fun createOAuth() = OAuthAuthorization(
    "https://test.me/auth",
    "https://test.me/token",
    "ClientId %#+!",
    listOf("one!","2","THREE+(1/2)"),
    "localhost://oauth"
)

private fun urlEncode(s: String) = URLEncoder.encode(s, "US-ASCII")

private val URL.queryParameters get(): Map<String, String> =
    query.split('&').associate {
        val parts = it.split('=')
        parts[0] to URLDecoder.decode(parts[1], "US-ASCII")
    }
