job('NodeJS example') {
    scm {
        git('git://github.com/Lihen52/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("cd basics && npm install")
    }
}

job('NodeJS Docker example') {
    scm {
        git('git://github.com/Lihen52/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') 
    }
    steps {
        dockerBuildAndPublish {
            buildContext('./basics')
            repositoryName('lihen52/cicd') //qa / dev
            tag('${GIT_REVISION,length=9}')
            registryCredentials('dockerhub')
            forcePull(false)
            forceTag(false)
            createFingerprints(false)
            skipDecorate()
        }
    }
}

pipelineJob('DSL_Pipeline') {

  def repo = 'https://github.com/Lihen52/docker-cicd.git'

  triggers {
    scm('H/5 * * * *')
  }
  description("Pipeline for $repo")

  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branches('master', '**/feature*')
          extensions { }  // required as otherwise it may try to tag the repo, which you may not want
        }
      }
      scriptPath('./basics/misc/Jenkinsfile')
    }
  }
}
pipelineJob('DSL_Pipeline2') {

  def repo = 'https://github.com/Lihen52/docker-cicd.git'

  triggers {
    scm('H/5 * * * *')
  }
  description("Pipeline for $repo")

  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branches('master', '**/feature*')
          extensions { }  // required as otherwise it may try to tag the repo, which you may not want
        }
      }
      scriptPath('./basics/misc/Jenkinsfile.v2')
    }
  }
}