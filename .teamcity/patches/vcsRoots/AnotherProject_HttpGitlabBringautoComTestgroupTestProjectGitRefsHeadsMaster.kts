package patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a vcsRoot with id = 'AnotherProject_HttpGitlabBringautoComTestgroupTestProjectGitRefsHeadsMaster'
in the project with id = 'AnotherProject', and delete the patch script.
*/
create(RelativeId("AnotherProject"), GitVcsRoot({
    id("AnotherProject_HttpGitlabBringautoComTestgroupTestProjectGitRefsHeadsMaster")
    name = "http://gitlab.bringauto.com/testgroup/TestProject.git#refs/heads/master"
    url = "http://gitlab.bringauto.com/testgroup/TestProject.git"
    branch = "refs/heads/master"
    authMethod = password {
        userName = "jan.kubalek"
        password = "credentialsJSON:8c37abbd-4459-468b-a934-30aa18d534af"
    }
}))

