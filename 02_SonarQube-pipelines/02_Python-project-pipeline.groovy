pipeline {
    agent any

    environment {
       SCANNER_HOME= tool 'sonar-scanner'
    }
    stages {
        stage('git checkout') {
            steps {
                git 'https://github.com/Realbhi/multi-tier-python-postgres.git'
            }
        }
        
        stage('Setup Virtual Environment') {
              steps {
                   sh '''
                        rm -rf venv # Remove any existing virtual environments
                        python3 -m venv venv # Create a new virtual environment
                        chmod -R 755 venv
                        bash -c "
                        source venv/bin/activate
                        pip install --upgrade pip
                        pip install -r requirements.txt"
                        '''
                    }
         }
          
          stage('Test') {
                     steps {
                                sh '''
                                bash -c "
                                source venv/bin/activate
                                pytest --cov=app --cov-report=xml
                                pytest --cov=app --cov-report=term-missing --disable-warnings
                                "
                                '''
                         }
          }
            
            
            stage('Sonar') {
                         steps {
                            withSonarQubeEnv('sonar-server') {
                                sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=Python-project \
                                -Dsonar.projectName=Python-project \
                                -Dsonar.exclusions=venv/** \
                                -Dsonar.sources=. \
                                -Dsonar.python.coverage.reportPaths=coverage.xml'''
                            }
                        }
                    }
            
            stage('quality-gate-checks'){
            steps{
                timeout(time: 1, unit: 'HOURS') {
                          //this will take lot of time so need to add in timeout
                          //and this quality game configured here will not be able to be fetched for sonarQube,
                          //until webhooks are not set up .. so webhooks need to be set up.
                          // if webhook not set up ....this step will keep on running as its not able to fetch
                          waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                 }
            }
        }
         
    }
}
