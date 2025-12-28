
pipeline {
    agent any
    
    tools{
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('git-checkout') {
            steps {
                 cleanWs()
                 git url:"https://github.com/Realbhi/Board-Game.git",
                 branch :"feature-button"
            }
        }
        
        stage('package-the-app') {
            steps {
                sh "mvn package"
            }
        }
        
        stage('build image and push') {
            steps {
              script{
               sh "docker build -t peacedockur/boardgame:latest ."
               
               withDockerRegistry(credentialsId: 'docker-cred') {
                  sh "docker push peacedockur/boardgame:latest"
               }
               
              }
            }
        }
        
        stage('start the container'){
            steps{
               script{
               sh '''
                   docker rm -f boardgame || true
                   docker run -d --name boardgame -p 8089:8080 peacedockur/boardgame:latest
                 '''
               }
            }
        }
    }
}
