package org.example.internship.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("list")
class List : Component() {
    var style: String? by Properties()
    var startWith: String? by Properties()
    var bulletType: String? by Properties()
}

@Serializable
@SerialName("paragraph")
class Paragraph : Component()

@Serializable
@SerialName("chapter")
class Chapter : Component() {
    var title: String? by Properties()
}

@Serializable
@SerialName("image")
class Image : Component() {
    var src: String? by Properties()
    var darkSrc: String? by Properties()
}

@Serializable
@SerialName("format")
class Format : Component(), InlineComponent {
    var style: String? by Properties()
    var color: String? by Properties()
}

@Serializable
@SerialName("link")
class Link : Component(), InlineComponent {
    var href: String? by Properties()
}

@Serializable
@SerialName("text")
class Text : Component(), InlineComponent {
    var value: String? by Properties()

    operator fun String.unaryPlus() {
        value = value?.plus(this) ?: this
    }
}

/**
 * Describes validity of a generated component.
 * Fields:
 * - [component] — the generated component
 * - [description] — description of what is invalid. Leave empty if component is valid
 * - [isValid] — validity of the component.
 */
data class ValidityReport(
    val component: Component,
    val description: String,
    val isValid: Boolean
) {
    override fun toString(): String {
        return "$description: $component"
    }
}
