package de.westnordost.streetcomplete.quests.building_colour

import de.westnordost.streetcomplete.view.image_select.OsmColour

enum class BuildingColour(override val osmValue: String, override val androidValue: String?) :
    OsmColour {

    // start with 16x Web CSS 1.0 colors - https://www.w3.org/TR/css-color-3/#html4
    // see https://github.com/Helium314/SCEE/issues/633#issuecomment-2603329012
    BLACK("black", "#000000"),
    SILVER("silver", "#c0c0c0"),
    GRAY("gray", "#808080"),
    WHITE("white", "#ffffff"),
    MAROON("maroon", "#800000"),
    RED("red", "#ff0000"),
    PURPLE("purple", "#800080"),
    FUCHSIA("fuchsia", "#ff00ff"),
    GREEN("green", "#008000"),
    LIME("lime", "#00ff00"),
    OLIVE("olive", "#808000"),
    YELLOW("yellow", "#ffff00"),
    NAVY("navy", "#000080"),
    BLUE("blue", "#0000ff"),
    TEAL("teal", "#008080"),
    AQUA("aqua", "#00ffff "),

    // Top used building colours
    WHITE("white", "#ffffff"),
    GREY80("#cccccc", null),
    BEIGEISH("#eecfaf", null),
    GREY("grey", "#808080"),
    BROWN("brown", "#a52a2a"),
    RED("red", "#ff0000"),
    YELLOW("yellow", "#ffff00"),
    BEIGE("beige", "#f5f5dc"),
    BLACK("black", "#000000"),
    GREEN("green", "#008000"),
    ORANGE("orange", "#ffa500"),
    BLUE("blue", "#0000ff"),
    POO("#85552e", null),
    LIGHT_GREY("lightgrey", "#d3d3d3"),
    SILVER("silver", "#c0c0c0"),
    TAN("tan", "#d2b48c"),
    YELLOWISH("#ffe0a0", null),
    LIGHT_YELLOW("lightyellow", "#ffffe0"),
    SLATE_GREY("#708090", null),
    REDDISH("#ff9e6b", null),

    // Rest of the recommended 3D palette
    MAROON("maroon", "#800000"),
    OLIVE("olive", "#808000"),
    TEAL("teal", "#008080"),
    NAVY("navy", "#000080"),
    PURPLE("purple", "#800080"),
    LIME("lime", "#00ff00"),
    AQUA("aqua", "#00ffff"),
    FUCHSIA("fuchsia", "#ff00ff"),
}
