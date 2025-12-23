
pipeline {
    agent {label 'slave-3'}
    
    parameters{
        choice(name:"branch",choices:["master","feature","feature-button"],description:"this is a choice for branches")
    }

    stages {
        stage('Hello') {
            steps {
                cleanWs()
                echo "${params.branch}"
                echo 'Hello World'
            }
        }
        
        stage('git-checkout'){
            steps{
                git url:"https://github.com/Realbhi/Board-Game.git",
                branch:"${params.branch}"
            }
        }
    }
}
