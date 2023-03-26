package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.BlockComponent
import org.example.internship.data.InlineComponent
import org.example.internship.data.ValidityReport

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