import org.example.internship.ComponentsReader
import org.example.internship.data.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream
import kotlin.collections.List
import org.example.internship.data.List as ListComponent

abstract class ComponentTestsProvider : ArgumentsProvider {
    /**
     * Performs component tests and returns a list of successful and failed tests
     */
    private fun performTests(): List<TestResult> {
        var testResults: MutableList<TestResult> = mutableListOf<TestResult>()
        for (component in components) {
            var validityReport: ValidityReport = component.validate()
            testResults.add(TestResult(validityReport.component, validityReport.description, validityReport.isValid))
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
