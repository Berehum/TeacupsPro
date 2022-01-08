# Reporting a bug

All bug reports are welcome, however it can take a while before they get fixed. Please try the latest version
of [Teacups]() and [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) (possibly a development build)
before creating an issue. Please avoid creating duplicate bug reports and put the following in your bug report:

* A clear title which describes the bug generally
* Your **server version** (For instance: git-Paper-353 (MC: 1.17.1). Just put the output of ``/version`` here.)
* The **version** of **Teacups** that you're using. (Just put the output of ``/version Teacups`` here)
*
    * The **version** of **ProtocolLib** that you're using. (Just put the output of ``/version ProtocolLib`` here)
* An **in-depth description** of the bug.
* How to reproduce the bug. (Retrace the steps you took before the bug occurred.)
* Errors/stacktraces that appeared in the console.

# Requesting a feature or change

You may request a feature and change, but that doesn't immediately mean that they're going to be made as well.

* A clear title which describes the feature or change generally
* An **in-depth description** of the feature and change.

# Contributing

### Guidance

* Ensure Java 8 is installed on your machine.
* Fork this repository and clone it.
* Make your changes to the code.
* Run ``mvn clean install`` for the **parent** pom.xml. I personally use the Maven Helper **plugin** in **IntelliJ**
  IDEA. (``Right-click the teacups project > Run Maven > Clean Install``)
* test your changes (the jar will be placed in build/libs/)
* push to your fork when ready & submit a pull request

### Guidelines

If you plan on contributing upstream please note the following:

* Discuss **large** changes first.
* Take a look at how the rest of the project is formatted and follow that.
* Do not alter the plugin's version.
* Limit the first line of commit messages to ~50 chars and leave a space below that, with an optional extended
  description
* **Test your changes** before making a pull request.

By contributing to Teacups you agree to license your code under
the [GNU General Public License v3.0](https://github.com/LMBishop/Quests/blob/master/LICENSE.txt).
