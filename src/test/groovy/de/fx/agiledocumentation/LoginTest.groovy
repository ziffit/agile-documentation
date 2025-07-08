package de.fx.agiledocumentation

import spock.lang.Specification

class LoginTest extends Specification {

    def 'Login mit User Peter0815 und Passwort Geheim%*123 ist erfolgreich. [FF-PROJ-0001#01]'() {
        expect:
        true // Hier sollte die Logik für den Login-Test stehen, z.B. ein Aufruf einer Login-Methode
    }

    def 'Login mit User Hans4711 und Passwort FALSCHES_PASSWORT schlägt fehl. [FF-PROJ-0001#02]'() {
        expect:
        true // Hier sollte die Logik für den Login-Test stehen, z.B. ein Aufruf einer Login-Methode
    }

    def "Login mit User FALSCHER_USER und Passwort FALSCHES_PASSWORT schlägt fehl. [FF-PROJ-0001#02]"() {
        expect:
        false //Absichtlich fehlschlagender Test
    }

}
