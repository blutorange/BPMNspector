# BPMNspector <img align="right" src="src/main/resources/reporting/res/logo-h100.png" height="100" width="217"/>

**Static analysis for BPMN 2.0 process models**

This is a fork of https://github.com/uniba-dsg/BPMNspector with the following changes:

* Move everything into a separate package.
  * The original project contained the `api`, which may conflict with other libraries.  Everything is now inside the
    package `com.github.blutorange.bpmnspector`. This also lets us add a proper `module-info.java`.
* Remove the CLI
  * This keeps the project simple and focused on the core functionality of validating BPMN files. The original CLI
    required e.g. Apache Velocity as a dependency, which is not needed for the core functionality of the project.
    If needed, we should turn this into a multi-module project, the CLI should be a separate module (feel free to make a PR!)
* Remove `MojoValidator`
  * This depends on a library `de.jena.uni.mojo` which was apparently [not yet published](https://github.com/uniba-dsg/BPMNspector/commit/47806d9ab9adbfe5fc7460dbef969ddc6366cc03).
* Remove XSD schema validator
  * Just use the standard Java XML API to validate against the BPMN 2.0 XSD files. But I'm open to PRs that add this back.
* Use Maven instead of Gradle.
  * This is a simple project that does not need the additional configurability provided Gradle.
* Properly declare all dependencies and make sure to only use dependencies available in Maven Central.
  * The original project used dependencies that were not available in Maven Central and included some dependencies as JAR
    files instead of declaring them. The original project was also not available via Maven Central.
* Update all dependencies to their latest versions.
  * The original project used outdated dependencies, some of which have changed their package name.
