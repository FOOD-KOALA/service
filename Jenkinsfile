pipeline {
    agent any

    // 1. Configurare unelte (Cerința din barem)
    tools {
        jdk 'JDK 21'
        gradle 'Gradle 8'
    }

    environment {
        // ATENȚIE: Pune aici username-ul tău real de pe Docker Hub (ex: john123)
        DOCKER_USER = 'iustinnc' 
        
        // Indiciul profului: Jenkins va căuta un secret cu ID-ul "docker_password"
        DOCKER_PASSWORD = credentials("docker_password")
        
        IMAGE_NAME = 'food-koala-service'
        TAG_NAME = "v1.0.${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build & Test') {
            steps {
                echo '=== Compilam si Testam ==='
                sh 'chmod +x gradlew'
                // Folosim comanda lăsată de prof, dar adăugăm și testele noastre
                sh './gradlew clean build jacocoTestReport'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo '=== Construim si urcam imaginea pe Docker Hub ==='
                // Ne logăm automat folosind parola din environment
                sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                sh "docker build -t ${DOCKER_USER}/${IMAGE_NAME}:${TAG_NAME} ."
                sh "docker push ${DOCKER_USER}/${IMAGE_NAME}:${TAG_NAME}"
            }
        }

        stage('Create Git Tag') {
            steps {
                echo '=== Cream un tag pe GitHub ==='
                script {
                    // Jenkins va căuta un token salvat cu ID-ul "github_token"
                    withCredentials([string(credentialsId: 'github_token', variable: 'GIT_TOKEN')]) {
                        sh """
                            git config user.email "jenkins@foodkoala.com"
                            git config user.name "Jenkins CI"
                            git tag -a ${TAG_NAME} -m "Release ${TAG_NAME}"
                            git push https://${GIT_TOKEN}@github.com/FOOD-KOALA/service.git ${TAG_NAME}
                        """
                    }
                }
            }
        }
    }
}