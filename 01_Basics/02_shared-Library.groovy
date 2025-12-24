@Library('shared-lib')_

pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('git-checkout') {
            steps {
                git 'https://github.com/Realbhi/secretsanta-generator.git'
            }
        }

        stage('compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('run-shared-lib') {
            steps {
                myFunction("abhishek")
                pkgwithskiptest()
            }
        }
    }
}
