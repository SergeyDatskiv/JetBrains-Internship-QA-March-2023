package org.example.internship.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.Components.Component
import org.example.internship.data.Components.Text

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
