package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

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