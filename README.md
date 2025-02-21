# eshop

## Quick Links
- [Module 1 - Coding Standards](#module-1---coding-standards)
- [Module 2 - CI/CD & DevOps](#module-2---cicd--devops)

### Module 1 - Coding Standards
#### Reflection 1
During the first part of the exercise, I used meaningful and descriptive names for variables, methods, and classes to clearly describe their intended use. Controller methods were also used to route incoming HTTP requests to service methods, ensuring that service methods focused only on implementing the business logic. We then used a repository to handle the requests as needed.

I also noticed a bug when a product was created, it didn't specify the ID (null value). As a result, I needed to manually generate the ID using UUID.

To reflect on my inaccuracies, I believe that in the coming weeks, I need to learn how to write consistent commit messages. This week, it was quite varied, with me sometimes using "feat" and other times not following the convention. Improving this practice in the future will help me write cleaner code and become more corporate-ready.
#### Reflection 2
1. After writing unit tests for my code, I'm more assured that my code has handled the edge cases well. When it comes to deciding how many unit tests should be made in a class, there is not a definitive number, as it varies depending on the complexity of the method. In short, the more coverage of edge cases, the better. To ensure that our unit tests are sufficient to verify the program, we can use code coverage metrics to reassure ourselves. However, 100% code coverage does not mean that our code is free from bugs or errors if the unit tests are written poorly.
   This approach will inevitably pose problems in the future because procedures or variables that are intended to be reusable should always be reused. If not, the readability of the program will significantly decrease, making it harder for other programmers to collaborate effectively.


2. To follow the "Don't Repeat Yourself" (DRY) principle, we can extract shared logic or instance variables into a helper class to ensure reusability. Implementing such improvements will be far more beneficial compared to duplicating code. Code duplication often leads to problems in the future, such as difficulties in maintaining consistency when API routes or configurations are updated in the codebase.

### Module 2 - CI/CD & DevOps
1. I fixed an issue where the dependencies in the code were not grouped by their destination, which made it difficult to distinguish which dependencies belonged to which category. For example, `implementation`, `testImplementation`, and `testRuntimeOnly` dependencies were mixed together, leading to reduced readability and making it harder to manage the dependencies.
To address this, I reorganized the dependencies by grouping them based on their type. This reorganization makes the code much easier to read and maintain.


2. Yes, the current implementation meets the definition of CI/CD. For every push and pull request, the code is automatically built and deployed through Koyeb, ensuring changes are promptly reflected in the deployment process. Unit tests are executed with Jacoco to maintain code coverage and quality, while static code analysis is performed using SonarQube to detect potential issues. Additionally, a Scorecard analysis is conducted to ensure the pipeline is secure and adheres to best security practices, all this implementation resulted in an automated CI/CD process.