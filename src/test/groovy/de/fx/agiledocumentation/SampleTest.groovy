package de.fx.agiledocumentation


import spock.lang.Ignore
import spock.lang.Specification

class SampleTest extends Specification {
    def systemUnderTest = new Sample()

    def "Dividieren von #a und #b ergibt #expected. [FF-PROJ-0001#01]"() {
        expect:
        systemUnderTest.divide(a, b) == expected

        where:
        a  | b || expected
        10 | 2 || 5.0d
        24 | 5 || 4.8d
    }

    def "Wird als Divisor 0 übergeben, wird eine IllegalArgumentException geworfen. [FF-PROJ-0001#02]"() {
        when:
        systemUnderTest.divide(10, 0)

        then:
        thrown(IllegalArgumentException)
    }

    def "Addieren von #a und #b ergibt #expected. [FF-PROJ-0002#01,FF-PROJ-0002#02,FF-PROJ-0002#03]"() {
        expect:
        systemUnderTest.add(a, b) == expected

        where:
        a  | b  || expected
        1  | 2  || 3
        -1 | -1 || -2
    }




    @Ignore
    def "Ignored Test [FF-PROJ-0002#03,FF-PROJ-0002#04]"() {
        expect:
        systemUnderTest.divide(a, b) == expected

        where:
        a | b || expected
        1 | 1 || 1
    }


    def "Beispielhafter fehlschlagender Test. [FF-PROJ-0001#02]"() {
        expect:
        systemUnderTest.divide(1, 2) == 99
    }
}
