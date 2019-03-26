# CI/CD processes on mobile projects

## Tools

We use different tools to configure CI and CD on different mobile projects:
1. Scripting: gradle, fastlane
2. CI: Teamcity, Bamboo, Jenkins, VS App Studio (android/ios), Bitbucket pipelines etc 
3. CD: Fabric, HockeyApp, Google play internal testing
4. Other: codecov, repo-badges, code analysis tools (checkstyle, findbugs, lint, pmd)


## Workflow

Usually we have custom gradle scripts to configure build flavors, artifacts path and set build versin based on latest tag, commit number or date.

Debug signing keys are stored in reporsitory to be the same between all the team members and CI. They used for release and debug builds prepared on PC where release keys are not configured. 
Release keys configured on CI server, gradle script checks if they are exist to prepare release build for publication.

Basicaly the workflow on CI is the following:
1. Fetch sources by trigger (on request, new commit, new tag, schedule etc)
2. Build project
3. Run unit test
4. Run lint tools
5. Publish artifacts:
   1. To CI internal artifacts (Teamcity, Bamboo)
   2. To Fabric beta or Google play internal testing using Fastlane
6. Send a notification about buld to the team (email, slack etc)
