# Kotest HTML Reporter

A [Kotest](https://kotest.io) [extension](https://kotest.io/docs/extensions/extensions.html)
for reporting test results in a single HTML file.

It is intended to be used with meaningful test descriptions (using any Kotest style)
to create useful specifications of the software under test.

For example, given these tests:

```kotlin
internal class JsonConfigurationTest : DescribeSpec({
    describe("Configuration from JSON") {
        describe("sink configuration") {
            describe("using `renderWith` and `sendTo` keys") {
                it("returns a configuration using names of built-in components") {
                    // Test code
                }
                it("returns null if names are not of built-in components") {
                    // Test code
                }
                it("returns null if `sendTo` key is missing") {
                    // Test code
                }
                it("returns null if `renderWith` key is missing") {
                    // Test code
                }
            }
            describe("using `seqServer` key") {
                it("returns a Seq configuration with RENDER_CLEF if only that key is present") {
                    // Test code
                }
                it("returns a Seq configuration with another renderer if specified") {
                    // Test code
                }
                it("returns a Seq configuration, overriding any other dispatcher") {
                    // Test code
                }
            }
        }
    }
})
```

It creates HTML something like this, when all tests pass:

▼ Configuration from JSON<br>
&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ sink configuration<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ using `renderWith` and `sendTo` keys<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ returns a configuration using names of built-in components<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ returns null if names are not of built-in components<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ returns null if `sendTo` key is missing<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▼ ✅ returns null if `renderWith` key is missing<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶ ✅ using `seqServer` key<br>
