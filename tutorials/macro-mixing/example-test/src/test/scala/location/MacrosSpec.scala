package location

class MacrosSpec extends munit.FunSuite {
  test("location") {
    assertEquals(Macros.location.line, 5)
  }
}