package de.fx

import groovy.json.JsonOutput
import groovy.text.SimpleTemplateEngine
import groovy.xml.XmlSlurper
import groovy.yaml.YamlSlurper

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class FachfunktionenToAsciidoc {

    final String NAME_OF_GENERATED_ASCIIDOC_FILE = "fachfunktionen-generated.adoc"
    final String NAME_OF_GLOSSARY_FILE = "glossary.adoc"
    final String GLOSSARY_DEFAULT_TEXT = "Zum Anlegen eines Glossars bitte die Datei `glossary.adoc` im `docDirectory` anlegen.\n\n"
    final String NAME_OF_PROJECT_INFO_FILE = "project-infos.adoc"
    final String PROJECT_INFO_DEFAULT_TEXT = "Zum Anlegen von Projektinformationen bitte die Datei `project-infos.adoc` im `docDirectory` anlegen.\n\n"

    def properties = new Properties()
    String targetFolder

    Map<String, Map<String, String>> akReport = [:]
    def templateEngine = new SimpleTemplateEngine()

    /**
     * Methode kann zum direkten Starten des Skripts genutzt werden, insbesondere zum Debuggen.
     * @param args [0] targetFolder als Absoluter Pfad
     */
    static void main(String[] args) {
        new FachfunktionenToAsciidoc(args[0]).create()
    }

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
    }

    void init(String asciiDocOutputDirectory, String testResultsOutputDirectory) {

        def asciidocTarget = new File(asciiDocOutputDirectory)
        if (!asciidocTarget.exists()) {
            asciidocTarget.mkdir()
        }

        loadAkReport(testResultsOutputDirectory)

    }


    void create() {

        def asciiDocOutputDirectory = properties.get("asciidocOutputDirectory")
        def testResultsOutputDirectory = properties.get("testResultsOutputDirectory")
        def docDir = properties.get("docDirectory")

        println(asciiDocOutputDirectory)
        println(testResultsOutputDirectory)
        println(docDir)

        //Parse akReport from test-results
        init(asciiDocOutputDirectory, testResultsOutputDirectory)


        println("# Erzeuge $NAME_OF_GENERATED_ASCIIDOC_FILE in $asciiDocOutputDirectory reading from $docDir")

        def fachfunktionenDir = new File(docDir)

        //Glossar Text anlegen
        def glossaryFile = new File(fachfunktionenDir, NAME_OF_GLOSSARY_FILE)
        def glossary = glossaryFile.exists() ? glossaryFile.getText("UTF-8") : GLOSSARY_DEFAULT_TEXT
        //ProjectInfo Text anlegen
        def projectInfoFile = new File(fachfunktionenDir, NAME_OF_PROJECT_INFO_FILE)
        def projectInfo = projectInfoFile.exists() ? projectInfoFile.getText("UTF-8") : PROJECT_INFO_DEFAULT_TEXT

        //Features auslesen
        def features = fachfunktionenDir.listFiles({ File file ->
            file.name.endsWith("yaml")
        } as FileFilter).sort()


        def fachfunktionen = [:]
        def tags = [:] as Map<String, List<String>>
        def aks = [:] as Map<String, Map<String, String>>
        def ffCoverage = [:] as Map<String, String>
        def diagramme = [:] as Map<String, String>
        def overallCoverage = 0
        features.each {
            def ffId = it.name.replace(".yaml", "")
            def yaml = new YamlSlurper().parseText(it.getText("UTF-8"))
            def adoc = new File(fachfunktionenDir, ffId + ".adoc")
            if (yaml["beschreibung"] == null && adoc.exists()) {
                yaml["beschreibung"] = adoc.getText("UTF-8")
            }
            fachfunktionen.put(ffId, yaml)
            yaml["tags"].each { String tag ->
                if (tags.containsKey(tag)) {
                    tags[tag].add(ffId)
                    return
                }
                tags.put(tag, [ffId])
            }

            def aksfeature = [:] as Map<String, String>
            //aks
            yaml["akzeptanzkriterien"].each { Map<String, String> ak ->
                aksfeature.put(ak.keySet()[0], ak[ak.keySet()[0]])
            }
            aks.put(ffId, aksfeature)


            def countSuccess = 0
            aksfeature.keySet().each { String ak ->
                String key = ffId + "#" + ak
                if (akReport.containsKey(key)) {
                    if (akReport.get(key).findAll { it.value == "success" }.size() == akReport.get(key).size()) {
                        countSuccess++
                    }
                }
            }
            println("countSuccess: $countSuccess")
            def percent = aksfeature.keySet().size() > 0 ? countSuccess / aksfeature.keySet().size() * 100 : 0
            def coverage = String.format("%.2f", percent)
            ffCoverage.put(ffId, coverage)
            overallCoverage = overallCoverage + percent

            //overall Coverage


            //Diagramm
            def diagramm = new File(fachfunktionenDir, ffId + "-dia.adoc")
            if (diagramm.isFile()) {
                diagramme.put(ffId, diagramm.getText("UTF-8"))
            }

        }

        overallCoverage = overallCoverage / ffCoverage.size()
        println "ffCoverage: $ffCoverage"
        println "overallCoverage: $overallCoverage"
        write(asciiDocOutputDirectory, [
                version        : properties.version,
                projectName    : properties.artifactId,
                fachfunktionen : fachfunktionen,
                tags           : tags,
                aks            : aks,
                akReport       : akReport,
                akCoverage     : ffCoverage,
                overallCoverage: String.format("%.2f", overallCoverage),
                diagramme      : diagramme,
                glossary       : glossary,
                projectInfo    : projectInfo,
        ])

    }


    void write(String asciiDocOutputDirectory, Map binding) {
        def writer = new StringWriter()

        // Get the file from the resources folder
        InputStream inputStream = FachfunktionenToAsciidoc.class.getResourceAsStream("/asciidocTemplate.adoc")

        // Read the content of the file
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")
        String templateContent = scanner.hasNext() ? scanner.next() : ""


        templateEngine.createTemplate(templateContent).make(binding).writeTo(writer)
        def outFile = new File(asciiDocOutputDirectory, "${NAME_OF_GENERATED_ASCIIDOC_FILE}")
        println "Trying to WRITE:  ${outFile.getAbsoluteFile()}"

        outFile.setText(writer.toString(), "UTF-8")
        println("# written: ${NAME_OF_GENERATED_ASCIIDOC_FILE}")
    }

    void loadAkReport(String directory) {
        def reportsDir = new File(directory)
        def reportFiles = reportsDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith('.xml')
            }
        })

        reportFiles.each { file ->
            def xml = new XmlSlurper().parse(file)
            xml.testcase.each { testcase ->
                def testName = testcase.@name.text() as String
                def status = testcase.failure.size() > 0 ? 'failed' : 'success'
                status = testcase.skipped.size() > 0 ? 'skipped' : status

                println("$status: $testName")

                def aks = testName.findAll(/FF-\w{4}-\d{3,4}#\d{2}/)

                aks.each {
                    def values = [key: testName, value: status, color: colorFor(status), icon: iconFor(status)]
                    if (akReport.containsKey(it)) {
                        akReport.get(it).addAll(values)
                    } else {
                        akReport.put(it, [values])
                    }
                }
            }

        }


        println("*** Gefunden AKs in Tests mit Result ***")
        println(JsonOutput.prettyPrint(JsonOutput.toJson(akReport)))

    }

    static String colorFor(String status) {
        switch (status) {
            case 'success':
                return 'green'
            case 'failed':
                return 'red'
            default:
                return 'gray'
        }
    }

    static String iconFor(String status) {
        switch (status) {
            case 'success':
                return '✓'
            case 'failed':
                return '✗'
            default:
                return '//'
        }
    }

}
