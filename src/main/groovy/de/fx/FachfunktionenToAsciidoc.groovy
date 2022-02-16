package de.fx

import groovy.text.SimpleTemplateEngine
import groovy.yaml.YamlSlurper

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class FachfunktionenToAsciidoc {

    def properties = new Properties()
    String targetFolder
    def templateEngine = new SimpleTemplateEngine()

    FachfunktionenToAsciidoc(String targetFolder = "target") {
        this.targetFolder = targetFolder
        def path = Paths.get(targetFolder).toAbsolutePath().toString()
        println("# Path: $path")

        try (def is = Files.newInputStream(Paths.get(targetFolder, "build.properties"), StandardOpenOption.READ)) {
            properties.load(is)
        } catch (IOException e) {
            throw new RuntimeException("build.properties in $targetFolder nicht gefunden. Bitte 'mnv compile' ausführen.", e)
        }

        println("# build.properties")
        properties.sort().each {
            print("${it.key}".padLeft(40))
            print(" : ")
            print("${it.value}\n")
        }

        def asciidocTarget = new File("$targetFolder/generated-adoc")
        if (!asciidocTarget.exists()) {
            asciidocTarget.mkdir()
        }
    }

    /**
     * main kann direkt aus IntelliJ aufgerufen werden. Absoluter Pfad zum Projekt als Argument mitgeben.
     * @param args
     */
    static void main(String... args) {
        new FachfunktionenToAsciidoc(args[0] + "/target").createAsciiDocs()
    }

    void create(String sourceDir, String targetFilename) {
        println("# Erzeuge $targetFilename in $sourceDir")

        def fachfunktionenDir = new File(properties.get(sourceDir).toString())
        def features = fachfunktionenDir.listFiles({ File file ->
            file.name.endsWith("yaml")
        } as FileFilter)

        def fachfunktionen = [:]
        features.each {
            def id = it.name.replace(".yaml", "")
            fachfunktionen.put(id,new YamlSlurper().parseText(it.getText("UTF-8")))
        }

        def asciidoc = """
<% fachfunktionen.sort().each { %>

=== <%=it.key%> <%=it.value.name%>

.<%=it.key%> - <%=it.value.name%>
[#<%=it.value.name%>]
****
*Kurzbeschreibung:* <%=it.value.kurzbeschreibung%>

<%=it.value.beschreibung%>

*Status:* <%=it.value.status%>
****

<% } %>

"""

        write(asciidoc, targetFilename, [fachfunktionen: fachfunktionen])


    }

    void createAsciiDocs() {
        create("directory.fachfunktionen","fachfunktionen-generated")
        create("directory.uebergreifende-fachfunktionen","uebergreifende-fachfunktionen-generated")
    }

    void write(String asciidoc, String filename, Map binding) {
        def writer = new StringWriter()
        templateEngine.createTemplate(asciidoc).make(binding).writeTo(writer)
        def outFile = new File("$targetFolder/generated-adoc/${filename}.adoc")
        outFile.setText(writer.toString(), "UTF-8")
        println("# written: ${filename}.adoc")
    }

}
