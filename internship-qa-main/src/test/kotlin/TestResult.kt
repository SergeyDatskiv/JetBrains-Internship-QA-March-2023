import org.example.internship.data.Component

/**
 * Describes a test result.
 * Fields:
 * - [component] — the tested component
 * - [description] — description for the test case
 * - [isSuccessful] — result of test's execution
 */
data class TestResult(
    val component: Component,
    val description: String,
    val isSuccessful: Boolean
) {
    override fun toString(): String {
        return "$description: $component"
    }
}