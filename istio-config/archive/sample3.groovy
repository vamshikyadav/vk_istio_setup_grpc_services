pipeline {
    agent any

    environment {
        JSON_FILE = 'data.json'
    }

    stages {
        stage('Prompt and Update JSON') {
            steps {
                script {
                    def jsonText = readFile(env.JSON_FILE)
                    def json = new groovy.json.JsonSlurper().parseText(jsonText)

                    // Build parameters list for all keys
                    def paramsList = []
                    json.each { k, v ->
                        paramsList << string(name: k, defaultValue: "${v}", description: "Enter value for '${k}' (leave empty to skip update)")
                    }

                    // Show single input prompt for all keys
                    def userInput = input(
                        id: 'jsonInput',
                        message: 'Update JSON values (leave blank to keep existing)',
                        parameters: paramsList
                    )

                    // Clone and apply only updated values
                    def updatedJson = json.clone()
                    userInput.each { k, v ->
                        if (v?.trim() && v != json[k]) {
                            updatedJson[k] = v
                        }
                    }

                    // Save updated JSON
                    writeFile file: env.JSON_FILE, text: groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(updatedJson))

                    echo "✅ JSON Updated:"
                    echo readFile(env.JSON_FILE)
                }
            }
        }
    }
}
