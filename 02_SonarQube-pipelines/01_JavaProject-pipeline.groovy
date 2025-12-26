pipeline {
    agent any
    
    tools{
        jdk 'jdk17'
        maven 'maven3'
    }
    environment{
            SCANNER_HOME = tool 'sonar-scanner'
        }
    
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
    
        
        stage('Git-checkout') {
            steps {
               git branch: 'main', url: 'https://github.com/Realbhi/Board-Game.git'
            }
        }
        
        stage('compile') {
            steps {
                sh "mvn compile"
            }
        }
        
        stage('test') {
            steps {
                sh "mvn test"
            }
        }
        
        stage('package') {
            steps {
                sh "mvn package"
            }
        }
        
        stage('sonar-analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                  sh ''' $SCANNER_HOME/bin/sonar-scanner \
                  -Dsonar.projectName=Boardgame \
                  -Dsonar.projectKey=Boardgame \
                  -Dsonar.java.binaries=target'''
                  //last statment  - Dsonar.java.binaries= target is specific to java projects only
              }
            }
        }
        
        stage('quality-gate-checks'){
            steps{
                timeout(time: 1, unit: 'HOURS') {
                          //this will take lot of time so need to add wit timeout
                          // waitForQualityGate is actually a blocking statement , which is waiting for POST webhook data from SonarQube.
                          // if webhook not set up ....this step will keep on running as its not able to get the QualityGate data.
                          waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                 }
            }
        }
        
        
        
    }
}
