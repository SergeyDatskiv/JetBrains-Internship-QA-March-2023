package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.BlockComponent
import org.example.internship.data.ValidityReport

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