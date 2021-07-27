import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.XmlReport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.xmlReport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

version = "2021.1"

project {
    description = "Contains all other projects"

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(YoutrackTestRepo)
}


object YoutrackTestRepo : Project({
    name = "Youtrack Test Repo"

    vcsRoot(YoutrackTestRepo_HttpsGithubComBringautoYoutrackTestRepoRefsHeadsMaster)
    buildType(YoutrackTestRepo_Build)

    template(YoutrackTestRepo_CppTemplate)
})

object YoutrackTestRepo_Build : BuildType({
    templates(YoutrackTestRepo_CppTemplate)
    name = "Build"

    params{
        param("env.package", "OFF")
    }

    steps {
        script {
            name = "Pack"
            id = "RUNNER_3"

            conditions {
                equals("env.package", "ON")
            }
            workingDir = "%proj_path%"
            scriptContent = "(cd build && make package)"
        }
        script {
            name = "Test"
            id = "RUNNER_4"

            conditions {
                equals("env.tests", "ON")
            }
            workingDir = "%proj_path%"
            scriptContent = "(%tests_file% --gtest_output=xml:test_report.xml)"
        }
    }
})

object YoutrackTestRepo_CppTemplate : Template({
    name = "CppTemplate"

    artifactRules = "%proj_path%/build => %proj_path%/build"

    params {
        text("clear", "true",
            regex = "^(true|false)${'$'}", validationMessage = "Wrong parameter pattern (true/false expected)")
        text("env.tests", "ON", allowEmpty = true)
        param("env.package", "ON")
        param("build_path", "build")
        param("tests_file", "%build_path%/tests/runTests")
        param("proj_path", "TestCpp")
    }

    vcs {
        root(YoutrackTestRepo_HttpsGithubComBringautoYoutrackTestRepoRefsHeadsMaster)
    }

    steps {
        script {
            name = "Clear"
            id = "RUNNER_5"

            conditions {
                equals("clear", "true")
            }
            workingDir = "%proj_path%"
            scriptContent = "(rm -rf %build_path%)"
        }
        script {
            name = "Cmake exec"
            id = "RUNNER_2"
            workingDir = "%proj_path%"
            scriptContent = "(mkdir -p build && cd build && cmake .. -DBRINGAUTO_TESTS=%env.tests% -DBRINGAUTO_PACKAGE=%env.package% && make)"
        }
        script {
            name = "Pack"
            id = "RUNNER_3"

            conditions {
                equals("env.package", "ON")
            }
            workingDir = "%proj_path%"
            scriptContent = "(cd build && make package)"
        }
        script {
            name = "Test"
            id = "RUNNER_4"

            conditions {
                equals("env.tests", "ON")
            }
            workingDir = "%proj_path%"
            scriptContent = "(%tests_file% --gtest_output=xml:test_report.xml)"
        }
    }

    triggers {
        vcs {
            id = "TRIGGER_2"
        }
    }

    features {
        xmlReport {
            id = "BUILD_EXT_1"
            reportType = XmlReport.XmlReportType.GOOGLE_TEST
            rules = "**/*.xml"
        }
    }
})

object YoutrackTestRepo_HttpsGithubComBringautoYoutrackTestRepoRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/bringauto/youtrack-test-repo#refs/heads/master"
    url = "https://github.com/bringauto/youtrack-test-repo"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "hofin34"
        password = "zxx8a7c097df17b0feae4a77d74fd19e6f6"
    }
})

//object ProjectRepo : GitVcsRoot({
//    name = "${DslContext.getParameter("repoName")} Repo"
//    url = DslContext.getParameter("fetchUrl")
//    branch = "refs/heads/master"
//    branchSpec = "refs/heads/*"
//    authMethod = password {
//        userName = "hofin34"
//        password = "zxx8a7c097df17b0feae4a77d74fd19e6f6"
//    }
//
//})