# Showcase für eine agile Dokumentation

`mvn compile` erzeugt im target eine html-Datei mit der generierten Dokumentation.


# Was macht eine Fachfunktion aus

## Inhalte

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
