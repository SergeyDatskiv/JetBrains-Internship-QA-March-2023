package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val AT_LEAST_ONE_CHILD = 1
private const val EMPTY_PARAGRAPH = "The paragraph is empty. "

@Serializable
@SerialName("paragraph")
class Paragraph : Component(), BlockComponent {

    /**
     * [validate] is Paragraph specific function which checks the validity of a Paragraph.
     * [checkChildren] check requirements related to the children of a Paragraph.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkChildren(message, reports)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [checkChildren] checks requirements related to the children of a Paragraph.
     * [AT_LEAST_ONE_CHILD] checks if a Paragraph has at least one child.
     * [checkEachChild] checks validity of individual child component.
     * Returns an informative message.
     */
    private fun checkChildren(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        outputMessage = if (children.size >= AT_LEAST_ONE_CHILD) {
            checkEachChild(outputMessage, reports)
        } else {
            invalidate(outputMessage, EMPTY_PARAGRAPH)
        }
        return outputMessage
    }

    /**
     * [checkEachChild] checks validity of individual child component.
     * [checkChildIsInlineComponent] checks if a child is of [InlineComponent] type.
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun checkEachChild(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        for (child in children) {
            outputMessage = checkChildIsInlineComponent(child, outputMessage)
            outputMessage = generateChildValidityReports(child, reports, outputMessage)
        }
        return outputMessage
    }

    /**
     * [checkChildIsInlineComponent] checks if a child is of [InlineComponent] type.
     * Returns an informative message.
     */
    private fun checkChildIsInlineComponent(child: Component, message: String): String {
        var outputMessage = message
        if (child !is InlineComponent) {
            outputMessage = invalidate(
                outputMessage,
                "A child ($child) is not an inline component. Only inline components are allowed.\n"
            )
        }
        return outputMessage
    }
}
