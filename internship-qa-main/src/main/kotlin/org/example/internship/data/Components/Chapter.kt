package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val MISSING_TITLE = "A chapter is missing a title. "
private const val MISSING_CHILDREN = "A chapter does not have children (i.e. empty chapter). "
private const val AT_LEAST_ONE_CHILD = 1

@Serializable
@SerialName("chapter")
class Chapter : Component(), BlockComponent {
    var title: String? by Properties()

    /**
     * [validate] is Chapter specific function which checks the validity of a Chapter.
     * [checkTitle] checks the title requirements of a Chapter.
     * [checkChildren] check requirements related to the children of a Chapter.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkTitle(message)
        message = checkChildren(message, reports)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [invalidate] is a function that sets the [isValid] variable to false.
     * It also appends a specified [errorMessage] to the [inputMessage].
     * Returns an informative message.
     */
    private fun invalidate(inputMessage: String, errorMessage: String): String {
        isValid = false
        return inputMessage + errorMessage
    }

    /**
     * [checkTitle] is a function that checks the validity of a title.
     * If invalid sets [isValid] to false and appends a corresponding [MISSING_TITLE] message.
     * Returns an informative message.
     */
    private fun checkTitle(message: String): String {
        var outputMessage = message
        if (title.isNullOrBlank()) {
            outputMessage = invalidate(outputMessage, MISSING_TITLE)
        }
        return outputMessage
    }

    /**
     * [checkChildren] checks requirements related to the children of a Chapter.
     * [AT_LEAST_ONE_CHILD] checks if a Chapter has at least one child.
     * [checkEachChild] checks validity of individual child component.
     * Returns an informative message.
     */
    private fun checkChildren(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        outputMessage = if (children.size < AT_LEAST_ONE_CHILD) {
            invalidate(outputMessage, MISSING_CHILDREN)
        } else {
            checkEachChild(outputMessage, reports)
        }
        return outputMessage
    }

    /**
     * [checkEachChild] checks validity of individual child component.
     * [childIsBlockComponent] checks if a child is of [BlockComponent] type.
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun checkEachChild(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        for (child in children) {
            outputMessage = childIsBlockComponent(child, outputMessage)
            outputMessage = generateChildValidityReports(child, reports, outputMessage)
        }
        return outputMessage
    }

    /**
     * [childIsBlockComponent] checks if a child is of [BlockComponent] type.
     * Returns an informative message.
     */
    private fun childIsBlockComponent(child: Component, message: String): String {
        var outputMessage = message
        if (child !is BlockComponent) {
            outputMessage = invalidate(
                outputMessage,
                "A child ($child) is not a block component. Only block components are allowed.\n"
            )
        }
        return outputMessage
    }

    /**
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun generateChildValidityReports(
        child: Component,
        reports: MutableList<ValidityReport>,
        message: String
    ): String {
        var outputMessage = message
        val childValidityReport: MutableList<ValidityReport> = child.validate()
        reports.addAll(childValidityReport)
        for (childReport in childValidityReport) {
            if (!childReport.isValid) {
                outputMessage =
                    invalidate(outputMessage, "This child component (${childReport.component}) is invalid.\n")
            }
        }
        return outputMessage
    }
}
