pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Select Playwright Browser')
        choice(name: 'HEADLESS', choices: ['true', 'false'], description: 'Run Headless or Visible Browser?')
        string(name: 'TAGS', defaultValue: '@ui or @api or @hybrid', description: 'Cucumber Tags to Execute')
    }

    stages {
        stage('1. Checkout GitHub Repo') {
            steps {
                checkout scm
            }
        }

        stage('2. Execute Playwright BDD Tests') {
            steps {
                // Uses 'bat' for Windows Jenkins Agent (use 'sh' if Jenkins is on Linux)
                bat """
                    mvn clean test ^
                    -Dbrowser=${params.BROWSER} ^
                    -Dheadless=${params.HEADLESS} ^
                    -Dcucumber.filter.tags="${params.TAGS}"
                """
            }
        }
    }

    post {
        always {
            // Archive the standalone Cucumber HTML report for download/email
            archiveArtifacts artifacts: 'target/cucumber-report.html', allowEmptyArchive: true

            // Generate the live Allure Dashboard inside Jenkins
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}