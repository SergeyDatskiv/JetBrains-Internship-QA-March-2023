package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

@Serializable
@SerialName("format")
class Format : Component(), InlineComponent {
    var style: String? by Properties()
    var color: String? by Properties()
    var allowedStyles: MutableSet<String> = mutableSetOf("bold", "italic", "color")

    /**
     * Validate is format specific function which checks the validity of a format.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        if (!allowedStyles.contains(style)) {
            isValid = false
            message += "This style ($style) is not supported. "
        } else if (style.equals("color")) {
            if (color.isNullOrBlank()) {
                isValid = false
                message += "The color has to be specified."
            } else if (color!!.length != 6) {
                isValid = false
                // TODO: Check that it is hexadecimal, not just length.
                message += "Current color format ($color) is not hexadecimal."
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