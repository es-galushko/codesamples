import AWS from 'aws-sdk';

AWS.config.setPromisesDependency(require('bluebird'));

const stages = {
  demo: 'dev',
  staging: 'staging',
  production: 'prod',
};

class Api {
  constructor(env, region) {
    this.env = env;
    this.region = region;

    this.lambda = new AWS.Lambda({
      region,
    });
  }

  request(funcName, payload) {
    const params = {
      FunctionName: funcName,
      InvocationType: 'RequestResponse',
      LogType: 'Tail',
      Payload: JSON.stringify(payload),
    };

    return this.lambda.invoke(params).promise();
  }

  async sendLog(payload) {
    const stage = stages[this.env];

    if (!stage) {
      return null;
    }

    const funcName = `audit-serverless-${stage}-logger`;
    return this.request(funcName, payload);
  }
}

export default Api;
