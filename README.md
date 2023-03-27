# JetBrains-Internship-QA-March-2023
A project for JetBrains internship for quality assurance (QA) in March of 2023.

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
