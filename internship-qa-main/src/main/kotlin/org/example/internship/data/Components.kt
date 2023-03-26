package org.example.internship.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.collections.List

@Serializable
@SerialName("list")
class List : Component(), BlockComponent {
    var style: String? by Properties()
    var startWith: String? by Properties()
    var bulletType: String? by Properties()
}

@Serializable
@SerialName("paragraph")
class Paragraph : Component(), BlockComponent {
    /**
     * Validate is Paragraph specific function which checks the validity of a Paragraph.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        if (children.size >= 1) {
            for (child in children) {
                if (child !is InlineComponent) {
                    isValid = false
                    message += "A child ($child) is not an inline component. Only inline components are allowed.\n"
                }
                var childValidityReport: MutableList<ValidityReport> = child.validate()
                reports.addAll(childValidityReport)
                for (childReport in childValidityReport) {
                    if (!childReport.isValid) {
                        isValid = false
                        message += "This child component (${childReport.component}) is invalid.\n"
                    }
                }
            }
        } else {
            isValid = false
            message += "The paragraph is empty. "
        }
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }
}

@Serializable
@SerialName("chapter")
class Chapter : Component(), BlockComponent {
    var title: String? by Properties()

    /**
     * Validate is Chapter specific function which checks the validity of a Chapter.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        if (title.isNullOrBlank()) {
            isValid = false
            message += "A chapter is missing a title. "
        }
        if (children.size < 1) {
            isValid = false
            message += "A chapter does not have children (i.e. empty chapter). "
        } else {
            for (child in children) {
                if (child !is BlockComponent) {
                    isValid = false
                    message += "A child ($child) is not a block component. Only block components are allowed.\n"
                }
                var childValidityReport: MutableList<ValidityReport> = child.validate()
                reports.addAll(childValidityReport)
                for (childReport in childValidityReport) {
                    if (!childReport.isValid) {
                        isValid = false
                        message += "This child component (${childReport.component}) is invalid.\n"
                    }
                }
            }
        }
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }
}

@Serializable
@SerialName("image")
class Image : Component(), BlockComponent {
    var src: String? by Properties()
    var darkSrc: String? by Properties()
    var validImageFormats: Set<String> = setOf("png", "jpg", "jpeg", "svg")

    /**
     * Validate is Image specific function which checks the validity of an Image.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        val srcCheck: List<String>? = src?.split(".")
        val darkSrcCheck: List<String>? = darkSrc?.split(".")
        if (srcCheck != null && srcCheck.size == 2) {
            if (!validImageFormats.contains(srcCheck[1])) {
                isValid = false
                message += "Unsupported (${srcCheck[1]}) image format. "
            }
        } else {
            isValid = false
            message += "Missing or invalid name of an image ($src). "
        }
        if (children.isNotEmpty()) {
            isValid = false
            message += "An image should not have children. "
        }
        if (darkSrcCheck != null && darkSrcCheck.size == 2) {
            if (!validImageFormats.contains(darkSrcCheck[1])) {
                isValid = false
                message += "Dark version has unsupported (${darkSrcCheck[1]}) image format. "
            }
        } else if (darkSrcCheck != null) {
            if (darkSrcCheck.size != 2) {
                isValid = false
                message += "invalid name of an image ($darkSrc). "
            }
        }
        return mutableListOf<ValidityReport>(ValidityReport(this, message, isValid))
    }
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
    var allowedProtocols: MutableSet<String> = mutableSetOf<String>("http", "https")

    /**
     * Validate is link specific function which checks the validity of a link.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        var checkProtocol: Boolean = false
        if (href.isNullOrBlank()) {
            isValid = false
            message += "There is no href."
        } else {
            for (protocol in allowedProtocols) {
                // TODO: Check if URL is valid.
                checkProtocol = href!!.contains(protocol)
                if (checkProtocol) {
                    break
                }
                isValid = false
                message += "The protocol of a link is not http or https. ($href) "
            }
        }
        if (children.size >= 1) {
            for (child in children) {
                if (child !is Text) {
                    isValid = false
                    message += "A child ($child) is not a Text component. Only Text components are allowed.\n"
                }
                var childValidityReport: MutableList<ValidityReport> = child.validate()
                reports.addAll(childValidityReport)
                for (childReport in childValidityReport) {
                    if (!childReport.isValid) {
                        isValid = false
                        message += "This child component (${childReport.component}) is invalid.\n"
                    }
                }
            }
        } else {
            isValid = false
            message += "The link is empty. "
        }
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }
}

@Serializable
@SerialName("text")
class Text : Component(), InlineComponent {
    var value: String? by Properties()

    operator fun String.unaryPlus() {
        value = value?.plus(this) ?: this
    }

    /**
     * Validate is text specific function which checks the validity of a text.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        if (value.isNullOrBlank()) {
            isValid = false
            message += "There is no text."
        }
        if (children.size != 0) {
            isValid = false
            message += "This component should not have children."
        }
        reports.add(ValidityReport(this, message, isValid))
        return reports
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
