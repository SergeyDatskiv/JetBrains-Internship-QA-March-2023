import org.example.internship.data.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

/*
 * Please do not modify the content of this file
 */

internal class Tests {
    @ParameterizedTest
    @ArgumentsSource(ComponentTestsProvider.SafeComponents::class)
    fun `test safe components`(result: TestResult) {
        assert(result.isSuccessful) { result }
    }

    @ParameterizedTest
    @ArgumentsSource(ComponentTestsProvider.UnsafeComponents::class)
    fun `test unsafe components`(result: TestResult) {
        assert(result.isSuccessful) { result }
    }
}
