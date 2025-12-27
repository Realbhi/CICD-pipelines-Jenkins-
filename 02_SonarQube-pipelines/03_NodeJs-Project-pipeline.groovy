pipeline {
    agent any
    
    environment{
        home_dir_scanner= tool "sonar-scanner"
    }

    stages {
        stage('git-checkout') {
            steps {
                cleanWs()
                git branch: 'main', url: 'https://github.com/Realbhi/NodejS-JEST.git'
            }
        }
        
       stage('install-dependencies') {
            steps {
                nodejs('nodejs') {
                   sh "npm install"
                }
            }
        }
        
        stage('run-test-cases') {
            steps {
                nodejs('nodejs') {
    // some block
                   sh "npm run test"
                }
            }
        }
        
        stage('sonar-analysis') {
            steps{
                
            withSonarQubeEnv('sonar-server') {
    // some blocksteps {
                sh '''
                $home_dir_scanner/bin/sonar-scanner \
                -Dsonar.projectName=Node-Js \
                -Dsonar.projectKey=Node-Js \
                -Dsonar.sources=. \
                -Dsonar.tests=. \
                -Dsonar.test.inclusions=**/**.test.js \
                -Dsonar.exclusions=node_modules/**
                -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info
                '''
            }
            }
            }
    }
        
    }
