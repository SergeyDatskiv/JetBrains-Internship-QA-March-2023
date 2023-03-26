package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.InlineComponent
import org.example.internship.data.ValidityReport

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
        if (href.isNullOrBlank()) {
            isValid = false
            message += "There is no href."
        } else {
            for (protocol in allowedProtocols) {
                // TODO: Check if URL is valid.
                var checkProtocol: Boolean = href!!.contains(protocol)
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