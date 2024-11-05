pipeline{
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'f2d6c8b7-12d7-4abc-8fae-6ef40689a649', url: 'https://github.com/PodssilDev/Aplicacion_Monolitica_PEP1']]])
                dir("Tingeso"){
                    sh "mvn clean install"
                }
            }
        }
        stage("Test"){
            steps{
                dir("Tingeso"){
                    sh "mvn test"
                }
            }
        }
        stage("SonarQube Analysis"){
            steps{
                dir("Tingeso"){
                    sh "mvn sonar:sonar"
                }
            }
        }
        stage("Build Docker Image"){
            steps{
                dir("Tingeso"){
                    sh "docker build -t johnserrano159/mueblesstgopep1 ."
                }
            }
        }
        stage("Push Docker Image"){
            steps{
                dir("Tingeso"){
                    withCredentials([string(credentialsId: 'dockerhub', variable: 'dckpass')]) {
                        sh "docker login -u johnserrano159 -p ${dckpass}"
                        
                    }
                    sh "docker push johnserrano159/mueblesstgopep1"
                    
                }
                
            }
        }
    }
    post{
        always{
            dir("Tingeso"){
                sh "docker logout"
            }
        }
    }
}
