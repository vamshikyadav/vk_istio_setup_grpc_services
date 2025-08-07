name: Env Endpoint Selector

on:
  workflow_dispatch:
    inputs:
      environment:
        description: 'Select the environment'
        required: true
        default: 'dev'
        type: choice
        options:
          - dev
          - staging
          - prod

jobs:
  set-endpoint:
    runs-on: ubuntu-latest
    env:
      SELECTED_ENV: ${{ github.event.inputs.environment }}

    steps:
      - name: Set ENDPOINT based on environment
        id: set-env
        run: |
          case "$SELECTED_ENV" in
            dev)
              echo "ENDPOINT=https://dev.com" >> $GITHUB_ENV
              ;;
            staging)
              echo "ENDPOINT=https://staging.com" >> $GITHUB_ENV
              ;;
            prod)
              echo "ENDPOINT=https://prod.com" >> $GITHUB_ENV
              ;;
            *)
              echo "Unknown environment: $SELECTED_ENV"
              exit 1
              ;;
          esac

      - name: Use the endpoint
        run: |
          echo "Selected environment: $SELECTED_ENV"
          echo "Using endpoint: $ENDPOINT"
// loop 

pipeline {
    agent any

    parameters {
        text(name: 'ITEMS', defaultValue: 'apple\nbanana\ncherry', description: 'Enter one item per line')
    }

    stages {
        stage('Loop Through Values') {
            steps {
                script {
                    // Split the text parameter by newline
                    def itemsList = params.ITEMS.readLines()

                    // Loop through each value
                    for (item in itemsList) {
                        echo "Item: ${item}"
                    }
                }
            }
        }
    }
}

