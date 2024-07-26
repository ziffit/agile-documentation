# Showcase für eine agile Dokumentation nach dem Prinzip FaCT (Fachfunktion, Code & Test)

## Ausführung

`mvn package` erzeugt in `target/html-out` die html-Datei `fachfunktionen-generated.html` mit der generierten Dokumentation.

## Prinzipielles

Es handelt sich um eine beispielhafte Implementierung, die fachliche Dokumentation, Code und Test in Beziehung zu 
bringt.

Vorteile: 
* Die Dokumentation ist Teil eines Pull-Requests und passt damit zum Code
* Die Dokumentation ist archiviert
* Die Verknüpfung von Fachfunktionen und Tests ermöglicht eine automatische Anforderungsabdeckung und damit auch 
  Selbstkontrolle
* Der Strukturierte Aufbau ermöglicht ein einfaches Pflegen, da keine Zeit für Formatierung aufgewendet werden muss
* Die Dokumentation ist rein Text-basiert (eingeschlossen Plant-UML-Grafiken) und kann bei einem PR gut 
  zusammengeführt werden
* Das Vorgehen kann prinzipiell in jeder Programmiersprache genutzt werden und führt zu keinem Vendor-Lock-in. 
* Der entstehende Report kann exportiert werden (z.B. Confluence oder PDF), um ihn allen Projektbeteiligten zur 
  Verfügung zu stellen
* Die YAMLs und Generierung kann angepasst werden, um weitere strukturierte Informationen zu einer Fachfunktion (FF) 
  abzulegen. Z.B. könnten optional notwendige Berechtigungen angegeben werden.

## Struktur

`src/docs` enthält die Quellen für die Dokumentation. Zum Anlegen einer neuen Fachfunktion am besten die *.yaml und 
*.adoc einer bestehen kopieren und den Zähler im Dateinamen anpassen. 

`src/main/groovy/de.fx/FachfunktionenToAsciiiDoc.groovy` ist das Skript, dass die Fachfunktionen einliest und ein gemeinsames asciidoc nach `target/asciidoc-out` generiert, welches dann mit `asciidoctor` in html (siehe `pom.xml`) umgewandelt wird.

`src/main/resources/asciidocTemplate.adoc` ist das Groovy-Template für die asciidoc-Generierung. Hier kann Einfluss auf das Aussehen des Reports genommen werden.

`src/test/groovy` enthält die Beispiel-Tests für die Fachfunktionen. Sie sind mit Spock geschrieben. Die Verknüpfung 
eines AKs (Akzeptanzkriteriums) mit einem Test erfolgt durch die Einbindung der ID des AKs in den Testnamen. Dabei 
können kommasepariert mehrere IDs angegeben werden. Das Benutzen der eckigen Klammern, um Aks vom Testnamen zu trennen, 
ist optional, hat sich aber als gute Konvention etabliert, da es die Lesbarkeit verbessert.

# Fachfunktionen

## Was macht eine Fachfunktion (kurz: FF) aus

Kurz: Eine Fachfunktion beschreibt, was eine Software leistet, aber nicht wie Sie bedient wird.

Eine fachliche Dokumentation ist kein Handbuch, sondern dient dem Nachschlagen von Features. Fragestellungen sind: 
* Was tut die SW, 
* wie (Algorithmen, Abhängigkeiten und Schnittstellen) tut Sie es und auch 
* weshalb (Fachliche Zusammenhänge).

Die Ids der Fachfunktionen und der Akzeptanzkriterien, werden einmal vergeben und dürfen danach nicht geändert werden, da Sie auch ausserhalb des Projekts (Confluence, Email, ...) verwendet werden.

Natürlich dürfen Sie im Rahmen angepasst und erweitert werden, solange die grundlegende Bedeutung erhalten bleibt, da ja die Entwicklung einer Fachfunktion auch aus der Historie ersichtlich ist.

Sind größere Änderungen notwendig, oder entfällt eine Funktion oder ein Kriterium, kann dieses einfach gelöscht werden. Die Id darf dann aber nicht neu vergeben werden. D.h. Lücken in der Zählung von Fachfunktionen oder Kriterien, weisen auf gelöschte Funktionen hin.

Das Entfallen von Kriterien oder Funktionen ist in der agilen Entwicklung normal und sollte nicht als negativ gewertet werden. Es ist ein Zeichen, dass die Entwicklung voranschreitet und sich die Anforderungen ändern.


## Zielpersonen

Die Zielpersonen einer Fachfunktion sind unter anderen:

### Entwickler	
Prüfen: Tut meine Software das, was ich von ihr erwarte? Was müssen meine Tests prüfen (Grenzwerte). Was kann beim Refactoring betroffen sein.
### PO/Fachlichkeit	
Leistet meine SW das, was ich von ihr benötige? Welchen Einfluss/Widerspruch haben neue Features zu meiner aktuellen Version.
### Tester	
Was tut die SW? Welche fachlichen Zusammenhänge gibt es? Ist das aktuelle Verhalten ein Feature oder ein Bug?

## Was ist Teil einer Fachfunktion (FF)

* Grenzwerte und Validierungen (z.B. Ganzzahlige Eingaben 0-1000, Datum muss in der Zukunft liegen, Regex-Prüfungen)
* Freitext/ Prosa-Beschreibung
* Akzeptanzkriterien
* Algorithmen (Also Prosa, Code-Auszug ...)
* Fachliche Hintergründe und Besonderheiten (z.B. Im Falle von Wert XY muss Feld ZZ befüllt werden, da es sich 
  hierbei um ... handelt)
* Fachliche Beschreibung bei Aufruf von Schnittstellen mit dem Ziel, zu vermitteln, weshalb eine Schnittstelle 
  genutzt wird.
* Tags zum Kategorisieren von FF. Der hierarchische Aufbau empfiehlt sicht nicht, da FF sich über die Zeit verändern 
  können (deshalb auch IDs und keine sprechenden Schlüssel). Tags helfen deshalb beim schnelleren auffinden von FF. 
* Optional: Beispieldaten
* Optional: Code-Auszüge

## Wie formuliere ich ein Akzeptanzkriterium (AK)

* ein Akzeptanzkriterium muss testbar formuliert werden
* Bei Formularen muss nicht für jede Validierung ein AK geschrieben werden. Es genügt ein AK, dass die Validierung 
  vor Speichern fordert. Die einzelnen Validierungen sind Teil der Fachfunktion.
* Ein AK kann sich auf Text der Fachfunktion beziehen.
* AK wird als ein Satz formuliert. Es sollten nur im Ausnahmefall zwei sein, da es sonst ein Hinweis ist, dass zwei 
  Kriterien in einem formuliert werden.

## Was ist NICHT Teil einer Fachfunktion

* Beschreibung der Oberfläche
* Beschreibung von Validierungsmeldungen. Es sollte beschrieben werden, wie bzw. was validiert wird (Feldlänge, Regex,
  Abhängigkeiten zu anderen Feldern) nicht aber wie eine explizite Fehlermeldung aussieht.
* Meinungen (Subjectives wie "Das ist eine gute Lösung")
* Namen von Entwicklern
* Kopierte Werte (z.B. Enums) aus dem Code. Wenn dann direkt den Code darstellen bzw. die Ausgabe generieren, damit 
  die Doku und Implementierung nicht auseinander laufen können. 
 
 
Durch das NICHT-Beschreiben der Oberfläche, wird die Dokumentation stabiler und es wird vermieden, dass die 
  Dokumentation und die Oberfläche auseinander laufen. Wichtig ist, DASS die Oberfläche bestimmte Möglichkeiten bietet, nicht aber genau wie. So kann ein Akzeptanzkriterium lauten: Die Möglichkeit zum Ändern der Adresse steht nur Benutzern mit der Rolle Editor zur Verfügung. Es bleibt so beispielsweise dem Frontend überlassen, ob es den beispielsweise Button ausblendet, ausgraut oder vielleicht alle Input-Felder disabled.
