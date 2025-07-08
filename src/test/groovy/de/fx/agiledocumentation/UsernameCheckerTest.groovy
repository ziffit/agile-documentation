package de.fx.agiledocumentation

import spock.lang.Specification

class UsernameCheckerTest extends Specification {

    def systemUnderTest = new UsernameChecker()

    def "#username ist ein zulässiger Username. [FF-PROJ-0002#01]"() {
        expect:
        systemUnderTest.check(username)

        where:
        username << [
                'Hans4711',
                '007Peter',
                'Hansi']
    }

    def "#username ist ein unzulässiger Username. [FF-PROJ-0002#01]"() {
        expect:
        !systemUnderTest.check(username)

        where:
        username << [
                'aaaa',
                'Hans',
                '0']
    }
}
