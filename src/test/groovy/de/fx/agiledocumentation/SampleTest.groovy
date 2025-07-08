package de.fx.agiledocumentation

import spock.lang.Ignore
import spock.lang.Specification

class SampleTest extends Specification {

    def "Dummytest für mehrere Fachfunktionen und Kriterien. [FF-PROJ-0001#04,FF-PROJ-0003#01,FF-PROJ-0003#02]"() {
        expect:
        true
    }
    def "Dummytest für mehrere Kriterien. [FF-PROJ-0002#03,FF-PROJ-0002#05]"() {
        expect:
        true
    }

    @Ignore
    def "Ignored Test [FF-PROJ-0003#03,FF-PROJ-0003#04]"() {
        expect:
        false
    }

}
