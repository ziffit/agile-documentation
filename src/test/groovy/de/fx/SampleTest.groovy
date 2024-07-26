package de.fx

import spock.lang.Ignore
import spock.lang.Specification

class SampleTest extends Specification {
    def systemUnderTest = new Sample()

    def "Addieren von #a und #b => #expected [FF-PROJ-0001#01,FF-PROJ-0001#02,FF-PROJ-0002#01]"() {
        expect:
        systemUnderTest.add(a, b) == expected

        where:
        a  | b  || expected
        1  | 2  || 3
        -1 | -1 || -2
    }

    def "Dividieren von #a und #b => #expected [FF-PROJ-0002#02]"() {
        expect:
        systemUnderTest.divide(a, b) == expected

        where:
        a  | b || expected
        10 | 2 || 5.0
        20 | 5 || 4.0
    }

    @Ignore
    def "Ignored Test [FF-PROJ-0002#03]"() {
        expect:
        systemUnderTest.divide(a, b) == expected

        where:
        a | b || expected
        1 | 1 || 1
    }

    def "Failed Test[FF-PROJ-0001#04]"() {
        expect:
        systemUnderTest.divide(a, b) == expected

        where:
        a | b || expected
        1 | 1 || 0
    }

    def "Wird als Divisor 0 übergeben, wird eine IllegalArgumentException geworfen. [FF-PROJ-0001#05]"() {
        when:
        systemUnderTest.divide(10, 0)

        then:
        thrown(IllegalArgumentException)
    }
}
