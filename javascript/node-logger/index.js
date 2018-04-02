import Api from './api';
import Formatter from './formatter';

class AuditLogger {
  constructor(env, region) {
    this.api = new Api(env, region);
    this.formatter = Formatter;
  }

  async log(data) {
    const payload = this.formatter.formatLog(data);
    return this.api.sendLog(payload);
  }
}

export default AuditLogger;
