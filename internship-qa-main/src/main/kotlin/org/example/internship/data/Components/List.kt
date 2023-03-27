package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

@Serializable
@SerialName("list")
class List : Component(), BlockComponent {
    var style: String? by Properties()
    var startWith: String? by Properties()
    var bulletType: String? by Properties()
    var allowedStyles: MutableSet<String> = mutableSetOf("bullet", "numerical", "plain")
    var allowedBulletType: MutableSet<String> = mutableSetOf("circle", "square")

    /**
     * Validate is list specific function which checks the validity of a list.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        var reports: MutableList<ValidityReport> = mutableListOf()
        if (style.isNullOrBlank()) {
            isValid = false
            message += "Missing list style. "
        }
        if (style.equals("numerical")) {
            var startInt: Int? = startWith?.toInt()
            if (startInt != null && startInt < 1) {
                isValid = false
                message += "Starting integer ($startWith) is invalid. "
            }
        }
        if (style.equals("bullet")) {
            if (bulletType != null && !allowedBulletType.contains(bulletType)) {
                isValid = false
                message += "This bullet type ($bulletType) is invalid."
            }
        }
        if (!allowedStyles.contains(style)) {
            isValid = false
            message += "This style ($style) is not supported for the list. "
        }
        if (children.size < 1) {
            isValid = false
            message += "A list does not have children (i.e. empty list). "
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
