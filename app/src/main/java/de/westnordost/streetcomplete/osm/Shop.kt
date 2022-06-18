package de.westnordost.streetcomplete.osm

import de.westnordost.streetcomplete.data.elementfilter.toElementFilterExpression
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder

// generated by "make update" from https://github.com/mnalis/StreetComplete-taginfo-categorize/
val KEYS_THAT_SHOULD_BE_REMOVED_WHEN_SHOP_IS_REPLACED = listOf(
    "shop_?[1-9]?(:.*)?", "craft_?[1-9]?", "amenity_?[1-9]?", "old_amenity", "old_shop",
    "information", "leisure", "office", "tourism",
    // popular shop=* / craft=* subkeys
    "marketplace", "household", "swimming_pool", "telecommunication", "laundry", "golf", "sports",
    "ice_cream", "scooter", "music", "retail", "yes", "ticket", "health", "newsagent", "lighting",
    "truck", "car_repair", "car_parts", "video", "fuel", "farm", "car", "ski", "sculptor", "hearing_aids",
    "surf", "photo", "boat", "gas", "kitchen", "anime", "builder", "hairdresser", "security",
    "bakery", "bakehouse", "fishing", "doors", "kiosk", "market", "bathroom", "lamps", "vacant",
    "insurance", "caravan", "gift", "bicycle", "insulation", "communication", "mall", "model",
    "empty", "wood", "hunting", "motorcycle", "trailer", "camera", "water", "fireplace", "outdoor",
    // obsoleted information
    "abandoned(:.*)?", "disused(:.*)?", "was:.*", "damage", "source:damage", "created_by",
    "check_date", "last_checked", "checked_exists:date",
    // classifications / links to external databases
    "fhrs:.*", "old_fhrs:.*", "fvst:.*", "ncat", "nat_ref", "gnis:.*", "winkelnummer",
    "type:FR:FINESS", "type:FR:APE", "kvl_hro:amenity", "ref:DK:cvr(:.*)?", "certifications?",
    // names and identifications
    "name_?[1-9]?(:.*)?", ".*_name(:.*)?", "noname", "branch(:.*)?", "brand(:.*)?",
    "not:brand(:.*)?", "network", "operator(:.*)?", "operator_type", "ref", "ref:vatin",
    "designation",
    // contacts
    "contact(:.*)?", "phone(:.*)?", "phone_?[1-9]?", "mobile", "fax", "facebook", "instagram",
    "twitter", "youtube", "telegram", "email", "website_?[1-9]?", "url", "source_ref:url", "owner",
    // payments
    "payment(:.*)?", "currency:.*", "cash_withdrawal(:.*)?", "fee", "money_transfer",
    // generic shop/craft attributes
    "seasonal", "time", "opening_hours(:.*)?", "check_date:opening_hours", "wifi", "internet",
    "internet_access(:.*)?", "second_hand", "self_service", "automated", "license:.*",
    "bulk_purchase", ".*:covid19", "language:.*", "baby_feeding", "description(:.*)?", "min_age",
    "max_age", "supermarket(:.*)?", "social_facility(:.*)?", "operational_status", "functional",
    "trade", "wholesale", "sale", "smoking", "zero_waste", "origin", "attraction", "strapline",
    "dog", "showroom", "toilets(:.*)?", "changing_table", "wheelchair(.*)?", "stroller",
    // food and drink details
    "bar", "cafe", "coffee", "microroasting", "microbrewery", "brewery", "real_ale", "taproom",
    "training", "distillery", "drink(:.*)?", "cocktails", "alcohol", "wine([:_].*)?",
    "happy_hours", "diet:.*", "cuisine", "tasting", "breakfast", "lunch", "organic",
    "produced_on_site", "restaurant", "food", "pastry", "pastry_shop", "product", "produce",
    "chocolate", "fair_trade", "butcher", "reservation", "takeaway(:.*)?", "delivery(:.*)?",
    "caterer", "real_fire", "flour_fortified", "highchair",
    // related to repair shops/crafts
    "service(:.*)?", "motorcycle:.*", "repair", ".*:repair", "electronics_repair(:.*)?",
    "workshop",
    // shop=hairdresser, shop=clothes
    "unisex", "male", "female", "gender",
    // healthcare like optician
    "healthcare(:.*)?", "health_.*", "medical_.*",
    // accommodation & layout
    "rooms", "stars", "accommodation", "beds", "capacity(:persons)?", "laundry_service",
    // misc specific attributes
    "clothes", "shoes", "tailor", "beauty", "tobacco", "carpenter", "furniture", "lottery",
    "sport", "leisure", "dispensing", "tailor:.*", "gambling", "material", "raw_material",
    "stonemason", "studio", "scuba_diving(:.*)?", "polling_station", "club", "collector", "books",
    "agrarian", "musical_instrument", "massage", "parts", "post_office(:.*)?", "religion",
    "denomination", "rental", ".*:rental", "tickets:.*", "public_transport", "goods_supply", "pet",
    "appliance", "artwork_type", "charity", "company", "crop", "dry_cleaning", "factory",
    "feature", "air_conditioning", "atm", "drive_through", "surveillance(:.*)?", "outdoor_seating",
    "indoor_seating", "vending", "vending_machine", "recycling_type",
).map { it.toRegex() }

fun StringMapChangesBuilder.replaceShop(tags: Map<String, String>) {
    removeCheckDates()

    for (key in keys) {
        if (KEYS_THAT_SHOULD_BE_REMOVED_WHEN_SHOP_IS_REPLACED.any { it.matches(key) }) {
            remove(key)
        }
    }

    for ((key, value) in tags) {
        this[key] = value
    }
}

/** ~ tenant of a normal retail shop area.
 *  So,
 *  - no larger or purpose-built things like malls, cinemas, theatres, car washes, fuel stations,
 *    museums, galleries, zoos, aquariums, bowling alleys...
 *  - no things that are usually not found in normal retail shop areas but in offices:
 *    clinics, doctors, fitness centers, dental technicians...
 *  - nothing that is rather located in an industrial estate like car repair and other types
 *    of workshops (most craft=* other than those where people go to have something repaired or so)
 *
 *  It is possible to specify a prefix for the keys here, e.g. "disused", to find disused shops etc.
 *
 *  Note: When this function is modified, please follow update instructions in:
 *  https://github.com/mnalis/StreetComplete-taginfo-categorize/blob/master/README.md
 *  */
fun isShopExpressionFragment(prefix: String? = null): String {
    val p = if (prefix != null) "$prefix:" else ""
    return ("""
        ${p}shop and ${p}shop !~ no|vacant|mall
        or ${p}tourism = information and ${p}information = office
        or """ +
        mapOf(
            "amenity" to arrayOf(
                "restaurant", "cafe", "ice_cream", "fast_food", "bar", "pub", "biergarten", "nightclub",
                "bank", "bureau_de_change", "money_transfer", "post_office", "internet_cafe",
                "pharmacy",
                "driving_school",
                "stripclub",
            ),
            "leisure" to arrayOf(
                "amusement_arcade", "adult_gaming_centre", "tanning_salon",
            ),
            "office" to arrayOf(
                "insurance", "travel_agent", "tax_advisor", "estate_agent", "political_party",
                "lawyer", "telecommunication", "it", "accountant"
            ),
            "craft" to arrayOf(
                "shoemaker", "tailor", "photographer", "watchmaker", "optician",
                "electronics_repair", "key_cutter", "dressmaker", "jeweller", "signmaker",
                "clockmaker"
            )
        ).map { p + it.key + " ~ " + it.value.joinToString("|") }.joinToString("\n  or ") + "\n"
        ).trimIndent()
}

/** Expression to see if an element is some kind of shop, disused or not */
val IS_SHOP_OR_DISUSED_SHOP_EXPRESSION = """
    nodes, ways, relations with
      ${isShopExpressionFragment()}
      or ${isShopExpressionFragment("disused")}
      or shop = vacant
""".toElementFilterExpression()

/** Expression to see if an element is some kind active, non-vacant shop */
val IS_SHOP_EXPRESSION =
    "nodes, ways, relations with ${isShopExpressionFragment()}".toElementFilterExpression()
