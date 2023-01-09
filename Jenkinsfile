node{

stage("Load Master Jenkins file"){
checkout([$class: 'GitSCM', branches: [[name: "master"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'JenkinsFile']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'securin-codecommit', url: 'https://git-codecommit.us-west-2.amazonaws.com/v1/repos/jenkins-pipeline.git']]])
jenkinsfile= load 'JenkinsFile/sl-ci-cd.pipeline'
}
}