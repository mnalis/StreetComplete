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

/** Tenant of retail or commercial rooms, e.g. a shop, an office etc.
 *  Something that can occupy (a part) of a non-purpose-built building
 *
 *  So, no larger purpose-built things like malls, cinemas, theatres, zoos, aquariums,
 *  bowling alleys...
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
        or ${p}office and ${p}office != vacant
        or ${p}healthcare and healthcare != hospital
        or ${p}craft
        or ${p}tourism = information and ${p}information = office
        or ${p}amenity = social_facility and ${p}social_facility ~ ${listOf(
            // only non-residential ones
            "ambulatory_care",
            "clothing_bank",
            "dairy_kitchen",
            "day_care",
            "food_bank",
            "healthcare",
            "outreach",
            "soup_kitchen",
            "workshop"
        ).joinToString("|")}
        or """ + mapOf(
        "leisure" to listOf(
            "adult_gaming_centre",
            "amusement_arcade",
            // "bowling_alley", // purpose-built
            "dance", // not necessarily purpose-built, see fitness centre
            "dancing_school",
            "escape_game",
            // "ice_rink", // purpose-built
            "indoor_play",
            "fitness_centre", // not necessarily purpose-built, esp. the smaller ones
            "hackerspace",
            "sauna",
            // "sports_centre", // purpose-built
            "tanning_salon",
            // "trampoline_park", // see sports centre
            // "water_park" // purpose-built
        ),
        "tourism" to listOf(
            // tourism = information only if it is an office, see above
            "gallery", // could be just an artist's show-room
            "museum" // only the larger ones are purpose-built
            // tourist accomodations are usually always purpose-built / not in something that could
            // otherwise be just a showroom, office etc.
        ),
        "amenity" to listOf(
            /* amenity, the "garbage patch in the OSM ocean" - https://media.ccc.de/v/sotm2022-18515-every-door-and-the-future-of-poi-in-openstreetmap#t=528
               sorted by occurrence on wiki page Key:amenity */
            /* sustenance */
            "bar",
            "biergarten",
            "cafe",
            "fast_food",
            "food_court",
            "ice_cream",
            "internet_cafe",
            "pub",
            "restaurant",

            /* education (all except school, college, university) */
            "childcare",
            "dive_centre", // depends, but can be in a showroom just like a diving school
            "dojo", // same as fitness_centre
            "driving_school",
            "kindergarten",
            "language_school",
            "library",
            "music_school",
            "prep_school",
            "toy_library",
            "training",

            /* transportation */
            "car_rental",
            "car_wash", // purpose-built, but see fuel
            "fuel", // purpose-built but too much of a shop that it would be weird to leave out
            "motorcycle_rental",
            "vehicle_inspection", // often similar to a car repair shop
            /* financial */
            "bank",
            "bureau_de_change",
            "money_transfer",
            "payment_centre",

            /* healthcare */
            "clinic", // sizes vary a lot, not necessarily purpose-built
            "dentist",
            "doctors",
            "hospital", // purpose-built
            "pharmacy",
            // social_facility only if it is not residential, see above
            "veterinary",

            /* entertainment, arts & culture */
            "arts_centre",
            // "brothel",
            // "casino", // as far as I know, always purpose-built
            // "cinema",
            "community_centre", // often purpose-built, but not necessarily
            // "conference_centre", // purpose-built
            "events_venue", // smaller ones are not purpose-built
            // "exhibition_centre", // purpose-built
            "gambling",
            // "love_hotel",
            // "planetarium", // like cinema
            "nightclub",
            "social_centre",
            "stripclub",
            "studio",
            // "swingerclub",
            // "theatre",

            /* public service */
            "post_office",
            // fire stations, police stations, townhalls etc. are purpose-built

            /* other */
            // "animal_boarding", // all three are usually purpose-built
            // "animal_breeding",
            // "animal_shelter",
            "coworking_space", // basically an office
            // "embassy", // usually purpose-built / not a normal commercial room
            // "place_of_worship" // usually-purpose-built
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
      or office = vacant
""".toElementFilterExpression()

/** Expression to see if an element is some kind active, non-vacant shop */
val IS_SHOP_EXPRESSION =
    "nodes, ways, relations with ${isShopExpressionFragment()}".toElementFilterExpression()

/** Expression to see if an element is some kind of vacant shop */
val IS_DISUSED_SHOP_EXPRESSION = """
    nodes, ways, relations with
      ${isShopExpressionFragment("disused")}
      or shop = vacant
""".toElementFilterExpression()
