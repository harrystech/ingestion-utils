# ingestion-utils
Common utilities used to implement hyppo integrations.

## Test

### Integration Testing
Common functionality for integration testing with the Hyppo ingestion platform can be found in the test package

The package contains three core components:

 * Tags
 * TestConfig
 * HyppoIntegrationTest
 
#### Tags
[Tagging](http://www.scalatest.org/user_guide/tagging_your_tests), a feature of ScalaTest, is utilized here to mark tests that require database access or a remote API call. By creating these custom tags and tagging tests one can omit running full integration tests during assembly (something which could take a long time depending on the integration).

To tag a test:
```scala
"This test does something" taggedAs Tags.RemoteAPICalls in {
 ...
}

"This other test does two things" taggedAs (Tags.RemoteAPICalls, Tags.DatabaseAccess) in {
 ...
}
```

To prevent tagged tests from running in a particular part of the build process, define it in the ```build.sbt```:

```scala
testOptions in Test += Tests.Argument("-l", "com.harrys.Tags.RemoteAPICalls", "-l", "com.harrys.Tags.DatabaseAccess")
```

#### TestConfig
The test config loads values out of the environment and packages them into a representative TypeSafe Config object to be used in testing. To use this object in your tests, you will need to add a ```testing.conf``` file with the desired substitutions to ```test/resources```.

Additionally, there is some required SBT setup. To begin, you'll need to create an integration plugin in your ```project``` directory:

```scala
import java.util.Properties

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object MyIntegrationSettingsPlugin extends AutoPlugin {
  override def requires = JvmPlugin

  override def trigger  = allRequirements

  object autoImport {
    val environmentFile       = settingKey[File]("The file to load for credentials and properties in the local environment")
    val environmentProperties = settingKey[Properties]("The values loaded from the environment file")
  }

  import autoImport._

  override lazy val buildSettings: Seq[Setting[_]] = Seq(
    environmentFile := baseDirectory.value / "credentials.properties"
  )

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    environmentProperties <<= environmentFile.apply(f => loadEnvironmentFile(f))
  )

  def loadEnvironmentFile(file: File): Properties = {
    val props = new Properties()
    if (!file.isFile){
      sys.error("Invalid environment file value: " + file.getPath)
    } else {
      IO.load(props, file)
    }
    props
  }
```