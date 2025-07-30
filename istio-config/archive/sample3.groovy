pipeline {
    agent any

    environment {
        JSON_FILE = 'data.json'
    }

    stages {
        stage('Read JSON and Prompt User') {
            steps {
                script {
                    def jsonText = readFile(env.JSON_FILE)
                    def json = new groovy.json.JsonSlurper().parseText(jsonText)

                    // Dynamically generate input fields for each key
                    def inputs = [:]
                    json.each { k, v ->
                        inputs[k] = input(
                            id: "input_${k}",
                            message: "Enter value for ${k} (leave empty to keep current)",
                            parameters: [
                                string(name: "${k}", defaultValue: "${v}", description: "Update for ${k}")
                            ]
                        )
                    }

                    // Apply only updated fields (not empty or unchanged)
                    def updatedJson = json.clone()
                    inputs.each { k, v ->
                        if (v && v != json[k]) {
                            updatedJson[k] = v
                        }
                    }

                    // Write back updated JSON
                    writeFile file: env.JSON_FILE, text: groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(updatedJson))

                    echo "Updated JSON:"
                    echo readFile(env.JSON_FILE)
                }
            }
        }
    }
}
