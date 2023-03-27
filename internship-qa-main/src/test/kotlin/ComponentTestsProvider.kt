import org.example.internship.ComponentsReader
import org.example.internship.data.*
import org.example.internship.data.Components.Component
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream
import kotlin.collections.List

abstract class ComponentTestsProvider : ArgumentsProvider {
    /**
     * Performs component tests and returns a list of successful and failed tests
     */
    private fun performTests(): List<TestResult> {
        val testResults: MutableList<TestResult> = mutableListOf()
        for (component in components) {
            val validityReport: MutableList<ValidityReport> = component.validate()
            for (report in validityReport) {
                testResults.add(TestResult(report.component, report.description, report.isValid))
            }
        }
        return testResults
    }

    /*
    * Please do not edit the code below
    */

    /**
     * This method converts instances of [TestResult] to [Arguments] that can be processed by JUnit
     */
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return performTests().map { Arguments.of(it) }.stream()
    }

    /**
     * Contains the list of components to be tested
     */
    abstract val components: List<Component>

    class SafeComponents : ComponentTestsProvider() {
        override val components: List<Component>
            get() = ComponentsReader.getSafeComponents()
    }

    class UnsafeComponents : ComponentTestsProvider() {
        override val components: List<Component>
            get() = ComponentsReader.getUnsafeComponents()
    }
}
