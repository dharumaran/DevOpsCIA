pipeline {
    agent any

    options {
        timestamps()
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Tools') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit(
                        testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                        allowEmptyResults: false
                    )
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts(
                    artifacts: 'course-registration-app/target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
                )
            }
        }
    }
}