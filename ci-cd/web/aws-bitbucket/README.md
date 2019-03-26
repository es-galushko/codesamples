# AWS + bitbucket CI/CD processes

## Overview

This process covers deployments of Node.js/MySQL/React application to demo, staging and production environments using [bitbucket pipelines](https://bitbucket.org/product/features/pipelines).

On AWS it uses Elastic Beanstalk as the main service with 3 stages(demo, staging, production).

All application artifacts are stored in AWS S3 storage.

When Pull Request is created we run linting and tests. On every merge to the master branch, it runs all the steps below and builds new release for QA to test.

Staging and production deployments are controlled manually via [bitbucket deployments](https://confluence.atlassian.com/bitbucket/bitbucket-deployments-940695276.html).

## Steps

1. Runs tests
2. Runs code linting
3. Autoincrement version and create a git tag
4. Build a production release
5. Deploy database migrations
6. Run custom deploy process that uses AWS SDK
   1. Creates ZIP archive of build artifacts
   2. Uploads ZIP archive to AWS S3 storage
   3. Creates a new Elastic Beanstalk application version that uses this artifact
   4. Updates Elastic Beanstalk applicaton to use a new version
   5. Deploys source maps to Senty and creates tagged Sentry release
7. Cleanup
