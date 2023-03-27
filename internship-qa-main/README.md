# My Note
Here is what I managed to achieve with this project. I tried to make commits throughout the development process.
Therefore, if you are interested in how I developed the project, please look at the commit history.

As a quick overview of my project:
1) Most of the work is in the components package of the project.
2) I reasoned most of the logic can be implemented in the classes used for JSON deserialization. So that is where I worked.
3) I created a `ValidityReport` data class to check a component's validity and record its data.
4) I used `ValidityReport` to transfer data to the `TestResult` data class in the `performTests` method.
5) There is a lot more I can say about my project. If you have any questions, feel free to reach out. If something is unclear, consider looking at the commit history.

There is definitely room for improvement. If I had more time, I would consider rewriting many functions in the functional programming paradigm.
Furthermore, I think more work could be done on the output format of the TestResult and ValidityReport objects.
I do not know what is recommended. However, I have a couple of ideas which I think might look better.
For example, indenting the messages related to the child of a component to better convey the relationships between different components.
Moreover, it wasn't clear how many edge cases I should consider when writing the tests. For example, do I need to validate a URL and hex decimals or not?
Also, I do not know if the input is sanitised for security reasons or if I should have done it.

Thank you for the opportunity to be a part of JetBrains and for your time. Thank you very much. Have a good day. All the best, and stay healthy.

**The rest of README is from the original project.**
Source: [https://github.com/i-am-sergey/internship-qa](https://github.com/i-am-sergey/internship-qa)

# Intro

You are a QA engineer of the team that develops an application for processing user-generated content that transforms it into JSON-based documents. Later, these documents are processed by a frontend application.

The frontend application expects the data that is well-formed and contains no invalid properties, and one of the ways to achieve that is to run few automated tests on the output.

# Instructions

In this task, you'll have to implement few tests for JSON output that describes rich text.

## Few words about the project structure

The pre-generated output is located in `jsons-safe` and `jsons-unsafe` directories. The former is meant to contain output that contains no errors (at least our tests say so). On the contrary, the latter reeks of all kinds of errors might occur.

You will find components specifications in [COMPONENTS.MD](COMPONENTS.md).

In the `org.example.internship.data` package, you will find classes that you will work with.

All components classes are not much different from the base class, but provide easier access to the component-specific properties. In case you'd like to generate your own component instances for testing purposes, there is a very simple DSL that you can use.

`ComponentsReader.kt` provides read access to the JSONs in both folders. These data are later used in tests.

In `Tests.kt`, there are two parameterized tests: `test safe components` and `test unsafe components`. You can use the 'safe' test to check whether your checks work well for the output that we have already reviewed.

Values for the parameterized tests are provided from two classes: `SafeComponents` and `UnsafeComponents`. Please do not modify them, but implement your solution in the `ComponentTestsProvider` class, so that it's applied in both cases.

The `components` field of `ComponentTestsProvider` contains a list of components to test.

To provide output, create a `TestResult` object for each of the tests that you perform on the components. Essentially, we expect that your tests take components as input and provide `TestResult` objects as output.

## Task

In the `performTests` method, please, implement the logic to verify that provided components conform to the specification. We expect that the task can be solved only by injecting your code to this method. However, if you feel like there is a more elegant solution, you are free to implement it however you like.

Please keep in mind that the specs might change over time, as well as other components appear, so we would appreciate a solution that is future-proof.