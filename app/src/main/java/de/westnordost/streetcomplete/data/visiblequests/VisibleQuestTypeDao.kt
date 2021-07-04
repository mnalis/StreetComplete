package de.westnordost.streetcomplete.data.visiblequests

import android.content.SharedPreferences
import de.westnordost.streetcomplete.Prefs
import de.westnordost.streetcomplete.data.Database

import javax.inject.Inject

import de.westnordost.streetcomplete.data.visiblequests.VisibleQuestTypeTable.Columns.QUEST_TYPE
import de.westnordost.streetcomplete.data.visiblequests.VisibleQuestTypeTable.Columns.VISIBILITY
import de.westnordost.streetcomplete.data.visiblequests.VisibleQuestTypeTable.NAME
import de.westnordost.streetcomplete.data.quest.QuestTypeRegistry

/** Stores which quest types are visible by user selection and which are not */
class VisibleQuestTypeDao @Inject constructor(private val prefs: SharedPreferences, private val db: Database) {
    @Inject internal lateinit var questTypeRegistry: QuestTypeRegistry

    fun getAll(): MutableMap<String, Boolean> {
        return getAll(prefs.getString(Prefs.QUEST_PROFILE,null)?.toIntOrNull() ?: 0)
    }

    fun getAll(questProfile: Int): MutableMap<String, Boolean> {
        val result = mutableMapOf<String,Boolean>()
        db.query(NAME) { cursor ->
            val questTypeName = cursor.getString(QUEST_TYPE)
            // working? what if entry does not exist?
            val visible = (cursor.getInt(VISIBILITY) shr(questProfile)) % 2 != 0
            result[questTypeName] = visible
        }
        return result
    }

    fun put(questTypeName: String, visible: Boolean) {
        // replace: get visibility, get profile, replace correct bit (!!)
        var oldVisibility = 0
        val questProfile = prefs.getString(Prefs.QUEST_PROFILE,null)?.toIntOrNull() ?: 0
        db.queryOne(NAME,
            columns = arrayOf(VISIBILITY),
            where = "$QUEST_TYPE = ?",
            args = arrayOf(questTypeName)
            // working? what if entry does not exist?
        ) { oldVisibility = it.getInt(VISIBILITY) }
        val oldVisBool = (oldVisibility shr(questProfile)) % 2 != 0
        if (oldVisBool == visible)
            return
        val newVisibility =
            if (oldVisBool)
                oldVisibility - (1 shl questProfile)
            else
                oldVisibility + (1 shl questProfile)
        db.replace(NAME, listOf(
            QUEST_TYPE to questTypeName,
            VISIBILITY to newVisibility
        ))
    }

    fun get(questTypeName: String): Boolean =
        db.queryOne(NAME,
            columns = arrayOf(VISIBILITY),
            where = "$QUEST_TYPE = ?",
            args = arrayOf(questTypeName)
        // working? what if entry does not exist?
        ) { (it.getInt(VISIBILITY) shr(prefs.getString(Prefs.QUEST_PROFILE,null)?.toIntOrNull() ?: 0)) % 2 != 0 } ?: true

    fun clear() {
//        db.delete(NAME)
        // geht das?
        for (questType in questTypeRegistry.all) {
            put(questType::class.simpleName!!, questType.defaultDisabledMessage <= 0)
        }
    }
}
